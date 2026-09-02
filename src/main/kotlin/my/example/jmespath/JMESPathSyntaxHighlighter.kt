package my.example.jmespath

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class JMESPathSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        val KEYWORD: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        val CURRENT_NODE: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_CURRENT_NODE",
            DefaultLanguageHighlighterColors.KEYWORD
        )
        val IDENTIFIER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_IDENTIFIER",
            DefaultLanguageHighlighterColors.IDENTIFIER
        )
        val STRING: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_STRING",
            DefaultLanguageHighlighterColors.STRING
        )
        val NUMBER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )
        val OPERATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        val COMPARATOR: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_COMPARATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        val PARENTHESES: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_PARENTHESES",
            DefaultLanguageHighlighterColors.PARENTHESES
        )
        val BRACKETS: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_BRACKETS",
            DefaultLanguageHighlighterColors.BRACKETS
        )
        val BRACES: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_BRACES",
            DefaultLanguageHighlighterColors.BRACES
        )
        val COMMA: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_COMMA",
            DefaultLanguageHighlighterColors.COMMA
        )
        val COLON: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_COLON",
            DefaultLanguageHighlighterColors.COMMA
        )
        val BAD_CHARACTER: TextAttributesKey = TextAttributesKey.createTextAttributesKey(
            "JMESPATH_BAD_CHARACTER",
            HighlighterColors.BAD_CHARACTER
        )

        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val CURRENT_NODE_KEYS = arrayOf(CURRENT_NODE)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val COMPARATOR_KEYS = arrayOf(COMPARATOR)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val COLON_KEYS = arrayOf(COLON)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }

    override fun getHighlightingLexer(): Lexer = JMESPathLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            JMESPathTokenTypes.KEYWORD -> KEYWORD_KEYS
            JMESPathTokenTypes.CURRENT_NODE -> CURRENT_NODE_KEYS
            JMESPathTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            JMESPathTokenTypes.STRING -> STRING_KEYS
            JMESPathTokenTypes.NUMBER -> NUMBER_KEYS
            JMESPathTokenTypes.OPERATOR -> OPERATOR_KEYS
            JMESPathTokenTypes.COMPARATOR -> COMPARATOR_KEYS
            JMESPathTokenTypes.PARENTHESES -> PARENTHESES_KEYS
            JMESPathTokenTypes.BRACKETS -> BRACKETS_KEYS
            JMESPathTokenTypes.BRACES -> BRACES_KEYS
            JMESPathTokenTypes.COMMA -> COMMA_KEYS
            JMESPathTokenTypes.COLON -> COLON_KEYS
            JMESPathTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
}
