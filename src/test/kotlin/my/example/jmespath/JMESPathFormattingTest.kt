package my.example.jmespath

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JMESPathFormattingTest : BasePlatformTestCase() {

    private fun doFormatTest(unformatted: String, expected: String) {
        val file = myFixture.configureByText("test.jp", unformatted)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }
        assertEquals(expected, file.text)
    }

    fun testOperatorAndComparatorSpacing() {
        val input = "a&&b||c==`10`|sort_by(@,&name)"
        val expected = "a && b || c == `10` | sort_by(@, &name)"
        doFormatTest(input, expected)
    }

    fun testMultiSelectHashAndListFormatting() {
        val inputSingleLine = "{name:name,tags:tags[*]}"
        val expectedSingleLine = "{name: name, tags: tags[*]}"
        doFormatTest(inputSingleLine, expectedSingleLine)

        val inputMultiLine = """
            {
            name: name,
            tags: [
            1,
            2,
            3
            ]
            }
        """.trimIndent()

        val expectedMultiLine = """
            {
              name: name,
              tags: [
                1,
                2,
                3
              ]
            }
        """.trimIndent()
        doFormatTest(inputMultiLine, expectedMultiLine)
    }

    fun testFunctionArgumentSpacing() {
        val input = "join(',',sort_by(items,&price))"
        val expected = "join(',', sort_by(items, &price))"
        doFormatTest(input, expected)
    }

    fun testSliceSyntaxNoSpaces() {
        val input = "people[?age>=`18`&&active==`true`][1:10:2]"
        val expected = "people[?age >= `18` && active == `true`][1:10:2]"
        doFormatTest(input, expected)
    }

    fun testLiteralPreservation() {
        val input = "foo == 'foo   bar' && bar == `{\"a\":   1, \"b\":  [1,  2]}`"
        val expected = "foo == 'foo   bar' && bar == `{\"a\":   1, \"b\":  [1,  2]}`"
        doFormatTest(input, expected)
    }

    fun testSampleJpIdempotency() {
        val sampleCode = """
            (
              people[?
                (
                  age >= `18` &&
                  age <= `65` &&
                  active == `true` &&
                  deleted != `true`
                ) ||
                (
                  role == 'admin' &&
                  !banned
                )
              ][]
              .{
                name: name,
                quotedKey: "display-name",
                current: @,
                firstTag: tags[0],
                lastScore: scores[-1],
                middleScores: scores[1:10:2],
                allTags: tags[*],
                flattenedProjects: projects[],
                projectNames: projects[*].name,
                filteredProjects: projects[?
                  status == 'active' &&
                  metrics.score > `75.5` &&
                  metrics.rank <= `10`
                ].{
                  id: id,
                  name: name,
                  tags: tags[*],
                  owner: owner.name,
                  literalObject: `{"kind":"project","enabled":true,"limits":[1,2,3],"meta":{"x":null}}`
                },
                computed: {
                  hasEmail: email != null,
                  notSuspended: !suspended,
                  rawString: 'raw string with spaces and \' escaped quote',
                  literalArray: `[true, false, null, 123, -45, 6.78, {"a":"b"}]`
                },
                sortedNames: sort_by(projects, &name)[*].name,
                maxScoreProject: max_by(projects, &metrics.score).name,
                joinedTags: join(', ', tags),
                fallbackName: displayName || name || 'unknown'
              }
              | [?computed.hasEmail && filteredProjects[0].metrics.score >= `80`]
              | sort_by(@, &name)
              | reverse(@)
              | [0:20]
            )
        """.trimIndent()

        val file = myFixture.configureByText("sample.jp", sampleCode)
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }
        val firstPass = file.text
        assertEquals(sampleCode, firstPass)

        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(file)
        }
        assertEquals(sampleCode, file.text)
    }

    fun testKeepLineBreaksFalse() {
        val settings = com.intellij.application.options.CodeStyle.getSettings(project)
        val common = settings.getCommonSettings(JMESPathLanguage.INSTANCE)
        val origKeepLineBreaks = common.KEEP_LINE_BREAKS
        try {
            common.KEEP_LINE_BREAKS = false

            val inputObject = """
                {
                  name: name,
                  tags: tags[*]
                }
            """.trimIndent()
            val expectedObject = "{name: name, tags: tags[*]}"
            doFormatTest(inputObject, expectedObject)

            val inputArray = """
                [
                  1,
                  2,
                  3
                ]
            """.trimIndent()
            val expectedArray = "[1, 2, 3]"
            doFormatTest(inputArray, expectedArray)
        } finally {
            common.KEEP_LINE_BREAKS = origKeepLineBreaks
        }
    }

    fun testMultiLineFunctionArguments() {
        val input = """
            join(
            ', ',
            tags
            )
        """.trimIndent()
        val expected = """
            join(
              ', ',
              tags
            )
        """.trimIndent()
        doFormatTest(input, expected)
    }

    fun testEmptyDelimiters() {
        val input = "foo() && bar[] && {}"
        val expected = "foo() && bar[] && {}"
        doFormatTest(input, expected)
    }

    fun testNestedMultiLineObjectsAndArrays() {
        val input = """
            {
            outer: {
            inner: [
            1,
            2
            ]
            }
            }
        """.trimIndent()
        val expected = """
            {
              outer: {
                inner: [
                  1,
                  2
                ]
              }
            }
        """.trimIndent()
        doFormatTest(input, expected)
    }

    fun testMultiLineFilterExpression() {
        val input = """
            people[?
            age >= `18` &&
            active == `true`
            ]
        """.trimIndent()
        val expected = """
            people[?
              age >= `18` &&
              active == `true`
            ]
        """.trimIndent()
        doFormatTest(input, expected)
    }

    fun testPartialSelectionFormatting() {
        val file = myFixture.configureByText("test.jp", "a==`1`&&b==`2`")
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformatText(file, 0, 6)
        }
        assertEquals("a == `1`&&b==`2`", file.text)
    }

    fun testEmptyAndWhitespaceFiles() {
        doFormatTest("", "")
        val wsFile = myFixture.configureByText("test.jp", "   \n\n  ")
        WriteCommandAction.runWriteCommandAction(project) {
            CodeStyleManager.getInstance(project).reformat(wsFile)
        }
        assertNotNull(wsFile.text)
    }

    fun testCustomCodeStyleSettingsSpacing() {
        val settings = com.intellij.application.options.CodeStyle.getSettings(project)
        val common = settings.getCommonSettings(JMESPathLanguage.INSTANCE)

        val origLogical = common.SPACE_AROUND_LOGICAL_OPERATORS
        val origEquality = common.SPACE_AROUND_EQUALITY_OPERATORS
        val origAfterComma = common.SPACE_AFTER_COMMA
        val origAfterColon = common.SPACE_AFTER_COLON

        try {
            common.SPACE_AROUND_LOGICAL_OPERATORS = false
            common.SPACE_AROUND_EQUALITY_OPERATORS = false
            common.SPACE_AFTER_COMMA = false
            common.SPACE_AFTER_COLON = false

            val input = "{name: name, tags: [1, 2]} | a && b || c == `10`"
            val expected = "{name:name,tags:[1,2]}|a&&b||c==`10`"
            doFormatTest(input, expected)
        } finally {
            common.SPACE_AROUND_LOGICAL_OPERATORS = origLogical
            common.SPACE_AROUND_EQUALITY_OPERATORS = origEquality
            common.SPACE_AFTER_COMMA = origAfterComma
            common.SPACE_AFTER_COLON = origAfterColon
        }
    }

    fun testCustomIndentSettings() {
        val settings = com.intellij.application.options.CodeStyle.getSettings(project)
        val common = settings.getCommonSettings(JMESPathLanguage.INSTANCE)
        val indentOptions = common.indentOptions ?: settings.getIndentOptions(JMESPathFileType.INSTANCE)

        val origIndent = indentOptions.INDENT_SIZE
        try {
            indentOptions.INDENT_SIZE = 4

            val input = """
                {
                name: name
                }
            """.trimIndent()

            val expected = """
                {
                    name: name
                }
            """.trimIndent()

            doFormatTest(input, expected)
        } finally {
            indentOptions.INDENT_SIZE = origIndent
        }
    }
}
