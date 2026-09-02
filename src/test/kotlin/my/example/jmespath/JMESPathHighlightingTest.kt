package my.example.jmespath

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JMESPathHighlightingTest : BasePlatformTestCase() {

    fun testFileTypeRegistration() {
        val jmespathType = FileTypeManager.getInstance().getFileTypeByExtension("jmespath")
        val jpType = FileTypeManager.getInstance().getFileTypeByExtension("jp")

        assertEquals(JMESPathFileType.INSTANCE, jmespathType)
        assertEquals(JMESPathFileType.INSTANCE, jpType)
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

    fun testSyntaxHighlighterAttributes() {
        val highlighter = JMESPathSyntaxHighlighter()

        val keywordHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.KEYWORD)
        assertEquals(1, keywordHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.KEYWORD, keywordHighlights[0])
        assertEquals(DefaultLanguageHighlighterColors.KEYWORD, JMESPathSyntaxHighlighter.KEYWORD.fallbackAttributeKey)

        val badCharHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.BAD_CHARACTER)
        assertEquals(1, badCharHighlights.size)
        assertEquals(JMESPathSyntaxHighlighter.BAD_CHARACTER, badCharHighlights[0])
        assertEquals(HighlighterColors.BAD_CHARACTER, JMESPathSyntaxHighlighter.BAD_CHARACTER.fallbackAttributeKey)

        val identifierHighlights = highlighter.getTokenHighlights(JMESPathTokenTypes.IDENTIFIER)
        assertEquals(0, identifierHighlights.size)
    }

    fun testColorSettingsPage() {
        val page = JMESPathColorSettingsPage()
        assertNotNull(page.displayName)
        assertNotNull(page.icon)
        assertTrue(page.attributeDescriptors.isNotEmpty())
        assertTrue(page.demoText.contains("true"))
        assertTrue(page.demoText.contains("false"))
        assertTrue(page.demoText.contains("null"))
    }
}
