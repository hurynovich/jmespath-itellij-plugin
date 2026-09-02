package my.example.jmespath

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JMESPathParserTest : BasePlatformTestCase() {

    fun testStructuredAstGeneration() {
        val psiFile = myFixture.configureByText("test.jp", "a && b || c == `10` | sort_by(@, &name)")
        assertNotNull(psiFile)

        val elements = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
        assertTrue(elements.isNotEmpty())

        val binaryExprs = elements.filter { it.node.elementType == JMESPathElementTypes.BINARY_EXPRESSION || it.node.elementType == JMESPathElementTypes.PIPE_EXPRESSION }
        assertTrue("Expected binary or pipe expressions in AST", binaryExprs.isNotEmpty())

        val funcCalls = elements.filter { it.node.elementType == JMESPathElementTypes.FUNCTION_EXPRESSION }
        assertEquals(1, funcCalls.size)
    }

    fun testObjectHashAndListAst() {
        val psiFile = myFixture.configureByText("test.jp", "{name: name, tags: [1, 2, 3]}")
        val objectHashes = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.OBJECT_HASH }
        assertEquals(1, objectHashes.size)

        val keyvalPairs = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.KEYVAL_EXPR }
        assertEquals(2, keyvalPairs.size)

        val keyNames = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.KEY_NAME }
        assertEquals(2, keyNames.size)

        val multiLists = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.MULTI_SELECT_LIST }
        assertEquals(1, multiLists.size)
    }

    fun testNestedFilterAndSliceAst() {
        val psiFile = myFixture.configureByText("test.jp", "people[?age >= `18`][1:10:2]")
        val filterExprs = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.FILTER_EXPRESSION }
        assertEquals(1, filterExprs.size)

        val sliceExprs = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
            .filter { it.node.elementType == JMESPathElementTypes.SLICE_EXPRESSION }
        assertEquals(1, sliceExprs.size)
    }

    fun testSampleJpParsing() {
        val sampleCode = """
            people[?age >= `18`].{
              name: name,
              tags: tags[*]
            } | sort_by(@, &name)
        """.trimIndent()
        val psiFile = myFixture.configureByText("sample.jp", sampleCode)
        assertNotNull(psiFile)
        val children = PsiTreeUtil.findChildrenOfType(psiFile, JMESPathPsiElement::class.java)
        assertTrue(children.isNotEmpty())
    }
}
