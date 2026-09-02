package my.example.jmespath

import com.intellij.lang.LanguageBraceMatching
import com.intellij.lang.LanguageExtensionPoint
import com.intellij.lang.annotation.Annotator
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JMESPathHighlightingTest : BasePlatformTestCase() {

    fun testFileTypeRegistration() {
        val jmespathType = FileTypeManager.getInstance().getFileTypeByExtension("jmespath")
        val jpType = FileTypeManager.getInstance().getFileTypeByExtension("jp")

        assertEquals(JMESPathFileType.INSTANCE, jmespathType)
        assertEquals(JMESPathFileType.INSTANCE, jpType)
        assertEquals(JMESPathIcons.FILE, jmespathType.icon)
        assertEquals(JMESPathIcons.FILE, jpType.icon)
    }

    fun testFunctionTokens() {
        val lexer = JMESPathLexer()
        lexer.start("join(', ', tags) sort_by(@, &name) custom_func()")

        assertEquals(JMESPathTokenTypes.FUNCTION, lexer.tokenType)
        assertEquals("join", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LPAREN, lexer.tokenType)
        assertEquals("(", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.STRING, lexer.tokenType)
        assertEquals("', '", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COMMA, lexer.tokenType)
        assertEquals(",", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("tags", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RPAREN, lexer.tokenType)
        assertEquals(")", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.FUNCTION, lexer.tokenType)
        assertEquals("sort_by", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LPAREN, lexer.tokenType)
        assertEquals("(", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.CURRENT_NODE, lexer.tokenType)
        assertEquals("@", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COMMA, lexer.tokenType)
        assertEquals(",", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.OPERATOR, lexer.tokenType)
        assertEquals("&", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("name", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RPAREN, lexer.tokenType)
        assertEquals(")", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.FUNCTION, lexer.tokenType)
        assertEquals("custom_func", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LPAREN, lexer.tokenType)
        assertEquals("(", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RPAREN, lexer.tokenType)
        assertEquals(")", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testKeywordTokens() {
        val lexer = JMESPathLexer()
        lexer.start("true false null")

        assertEquals(JMESPathTokenTypes.KEYWORD, lexer.tokenType)
        assertEquals("true", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.KEYWORD, lexer.tokenType)
        assertEquals("false", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.KEYWORD, lexer.tokenType)
        assertEquals("null", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testIdentifierNotHighlightedAsKeyword() {
        val lexer = JMESPathLexer()
        lexer.start("trueValue nullField foo_bar")

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("trueValue", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("nullField", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("foo_bar", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testStringsNumbersComparatorsAndOperators() {
        val lexer = JMESPathLexer()
        lexer.start(""""foo" 'bar' 123 45.67 == != < [? @ | *""")

        assertEquals(JMESPathTokenTypes.STRING, lexer.tokenType)
        assertEquals("\"foo\"", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.STRING, lexer.tokenType)
        assertEquals("'bar'", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.NUMBER, lexer.tokenType)
        assertEquals("123", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.NUMBER, lexer.tokenType)
        assertEquals("45.67", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COMPARATOR, lexer.tokenType)
        assertEquals("==", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COMPARATOR, lexer.tokenType)
        assertEquals("!=", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COMPARATOR, lexer.tokenType)
        assertEquals("<", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LBRACKET, lexer.tokenType)
        assertEquals("[?", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.CURRENT_NODE, lexer.tokenType)
        assertEquals("@", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.OPERATOR, lexer.tokenType)
        assertEquals("|", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.OPERATOR, lexer.tokenType)
        assertEquals("*", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testBadCharacterToken() {
        val lexer = JMESPathLexer()
        lexer.start("foo # bar")

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("foo", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.BAD_CHARACTER, lexer.tokenType)
        assertEquals("#", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("bar", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testSyntaxHighlighterAttributes() {
        val highlighter = JMESPathSyntaxHighlighter()

        val keywordHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.KEYWORD)
        assertEquals(1, keywordHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.KEYWORD, keywordHighlights[0])
        assertEquals(DefaultLanguageHighlighterColors.KEYWORD, JMESPathSyntaxHighlighter.KEYWORD.fallbackAttributeKey)

        val currentNodeHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.CURRENT_NODE)
        assertEquals(1, currentNodeHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.CURRENT_NODE, currentNodeHighlights[0])
        assertEquals(DefaultLanguageHighlighterColors.KEYWORD, JMESPathSyntaxHighlighter.CURRENT_NODE.fallbackAttributeKey)

        val functionHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.FUNCTION)
        assertEquals(1, functionHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.FUNCTION, functionHighlights[0])
        assertEquals(DefaultLanguageHighlighterColors.FUNCTION_CALL, JMESPathSyntaxHighlighter.FUNCTION.fallbackAttributeKey)

        val badCharHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.BAD_CHARACTER)
        assertEquals(1, badCharHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BAD_CHARACTER, badCharHighlights[0])
        assertEquals(HighlighterColors.BAD_CHARACTER, JMESPathSyntaxHighlighter.BAD_CHARACTER.fallbackAttributeKey)

        val identifierHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.IDENTIFIER)
        assertEquals(0, identifierHighlights.size)

        assertEquals(DefaultLanguageHighlighterColors.INSTANCE_FIELD, JMESPathSyntaxHighlighter.KEY_NAME.fallbackAttributeKey)

        val stringHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.STRING)
        assertEquals(1, stringHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.STRING, stringHighlights[0])

        val numberHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.NUMBER)
        assertEquals(1, numberHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.NUMBER, numberHighlights[0])

        val lparenHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.LPAREN)
        assertEquals(1, lparenHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.PARENTHESES, lparenHighlights[0])

        val rparenHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.RPAREN)
        assertEquals(1, rparenHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.PARENTHESES, rparenHighlights[0])

        val lbracketHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.LBRACKET)
        assertEquals(1, lbracketHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BRACKETS, lbracketHighlights[0])

        val rbracketHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.RBRACKET)
        assertEquals(1, rbracketHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BRACKETS, rbracketHighlights[0])

        val lbraceHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.LBRACE)
        assertEquals(1, lbraceHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BRACES, lbraceHighlights[0])

        val rbraceHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.RBRACE)
        assertEquals(1, rbraceHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BRACES, rbraceHighlights[0])
    }

    fun testPairedBracketsAndBracesTokens() {
        val lexer = JMESPathLexer()
        lexer.start("{ a: [ ( 1 ) ] }")

        assertEquals(JMESPathTokenTypes.LBRACE, lexer.tokenType)
        assertEquals("{", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.IDENTIFIER, lexer.tokenType)
        assertEquals("a", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.COLON, lexer.tokenType)
        assertEquals(":", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LBRACKET, lexer.tokenType)
        assertEquals("[", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.LPAREN, lexer.tokenType)
        assertEquals("(", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.NUMBER, lexer.tokenType)
        assertEquals("1", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RPAREN, lexer.tokenType)
        assertEquals(")", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RBRACKET, lexer.tokenType)
        assertEquals("]", lexer.tokenText)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.WHITE_SPACE, lexer.tokenType)
        lexer.advance()

        assertEquals(JMESPathTokenTypes.RBRACE, lexer.tokenType)
        assertEquals("}", lexer.tokenText)
        lexer.advance()

        assertNull(lexer.tokenType)
    }

    fun testPairedBraceMatcherDefinition() {
        val matcher = JMESPathPairedBraceMatcher()
        val pairs = matcher.pairs
        assertEquals(3, pairs.size)

        val parenPair = pairs.first { it.leftBraceType == JMESPathTokenTypes.LPAREN }
        assertEquals(JMESPathTokenTypes.RPAREN, parenPair.rightBraceType)
        assertFalse(parenPair.isStructural)

        val bracketPair = pairs.first { it.leftBraceType == JMESPathTokenTypes.LBRACKET }
        assertEquals(JMESPathTokenTypes.RBRACKET, bracketPair.rightBraceType)
        assertFalse(bracketPair.isStructural)

        val bracePair = pairs.first { it.leftBraceType == JMESPathTokenTypes.LBRACE }
        assertEquals(JMESPathTokenTypes.RBRACE, bracePair.rightBraceType)
        assertTrue(bracePair.isStructural)

        assertTrue(matcher.isPairedBracesAllowedBeforeType(JMESPathTokenTypes.LBRACE, null))
        assertEquals(42, matcher.getCodeConstructStart(null, 42))
    }

    fun testPairedBraceMatcherRegisteredInLanguage() {
        val matcher = LanguageBraceMatching.INSTANCE.forLanguage(JMESPathLanguage.INSTANCE)
        assertNotNull(matcher)
        assertTrue(matcher is JMESPathPairedBraceMatcher)
    }

    fun testSyntaxAnnotatorRegisteredInLanguage() {
        val points = ExtensionPointName.create<LanguageExtensionPoint<Annotator>>("com.intellij.annotator").extensionList
        assertTrue(points.any { it.language == JMESPathLanguage.INSTANCE.id && it.instance is JMESPathSyntaxAnnotator })
    }

    fun testColorSettingsPage() {
        val page = JMESPathColorSettingsPage()
        assertNotNull(page.displayName)
        assertNotNull(page.icon)
        assertTrue(page.attributeDescriptors.isNotEmpty())
        assertTrue(page.attributeDescriptors.any { it.key == JMESPathSyntaxHighlighter.FUNCTION })
        assertTrue(page.attributeDescriptors.any { it.key == JMESPathSyntaxHighlighter.KEY_NAME })
        assertTrue(page.demoText.contains("true"))
        assertTrue(page.demoText.contains("false"))
        assertTrue(page.demoText.contains("null"))
    }

    fun testPsiFileParsing() {
        val psiFile = myFixture.configureByText("example.jp", "locations[?state == 'WA'].name | sort(@)")
        assertTrue(psiFile is JMESPathFile)
        assertEquals(JMESPathFileType.INSTANCE, psiFile.fileType)
    }

    fun testSampleFileTokens() {
        val lexer = JMESPathLexer()
        lexer.start("sortedNames: sort_by(projects, &name)[*].name, joinedTags: join(', ', tags)")
        val tokenTypes = mutableListOf<com.intellij.psi.tree.IElementType>()
        val tokenTexts = mutableListOf<String>()
        while (lexer.tokenType != null) {
            tokenTypes.add(lexer.tokenType!!)
            tokenTexts.add(lexer.tokenText)
            lexer.advance()
        }
        assertTrue(tokenTexts.contains("sort_by"))
        val sortByIdx = tokenTexts.indexOf("sort_by")
        assertEquals(JMESPathTokenTypes.FUNCTION, tokenTypes[sortByIdx])

        assertTrue(tokenTexts.contains("join"))
        val joinIdx = tokenTexts.indexOf("join")
        assertEquals(JMESPathTokenTypes.FUNCTION, tokenTypes[joinIdx])

        assertTrue(tokenTexts.contains("projects"))
        val projectsIdx = tokenTexts.indexOf("projects")
        assertEquals(JMESPathTokenTypes.IDENTIFIER, tokenTypes[projectsIdx])
    }
}
