package my.example.jmespath

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

object JMESPathTokenTypes {
    @JvmField
    val KEYWORD = JMESPathTokenType("JMESPATH_KEYWORD")

    @JvmField
    val CURRENT_NODE = JMESPathTokenType("JMESPATH_CURRENT_NODE")

    @JvmField
    val FUNCTION = JMESPathTokenType("JMESPATH_FUNCTION")

    @JvmField
    val IDENTIFIER = JMESPathTokenType("JMESPATH_IDENTIFIER")

    @JvmField
    val STRING = JMESPathTokenType("JMESPATH_STRING")

    @JvmField
    val NUMBER = JMESPathTokenType("JMESPATH_NUMBER")

    @JvmField
    val COMPARATOR = JMESPathTokenType("JMESPATH_COMPARATOR")

    @JvmField
    val OPERATOR = JMESPathTokenType("JMESPATH_OPERATOR")

    @JvmField
    val PARENTHESES = JMESPathTokenType("JMESPATH_PARENTHESES")

    @JvmField
    val BRACKETS = JMESPathTokenType("JMESPATH_BRACKETS")

    @JvmField
    val BRACES = JMESPathTokenType("JMESPATH_BRACES")

    @JvmField
    val COMMA = JMESPathTokenType("JMESPATH_COMMA")

    @JvmField
    val COLON = JMESPathTokenType("JMESPATH_COLON")

    @JvmField
    val LITERAL_TICK = JMESPathTokenType("JMESPATH_LITERAL_TICK")

    @JvmField
    val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE

    @JvmField
    val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    val BUILTIN_FUNCTIONS: Set<String> = setOf(
        "abs",
        "avg",
        "ceil",
        "contains",
        "ends_with",
        "floor",
        "join",
        "keys",
        "length",
        "map",
        "max",
        "max_by",
        "merge",
        "min",
        "min_by",
        "not_null",
        "reverse",
        "sort",
        "sort_by",
        "starts_with",
        "sum",
        "to_array",
        "to_number",
        "to_string",
        "type",
        "values"
    )

    fun getIElementType(antlrTokenType: Int): IElementType {
        return when (antlrTokenType) {
            JmesPathLexer.JSON_CONSTANT -> KEYWORD
            JmesPathLexer.NAME -> IDENTIFIER
            JmesPathLexer.STRING, JmesPathLexer.RAW_STRING -> STRING
            JmesPathLexer.REAL_OR_EXPONENT_NUMBER, JmesPathLexer.SIGNED_INT -> NUMBER
            JmesPathLexer.COMPARATOR -> COMPARATOR
            JmesPathLexer.T__15 -> CURRENT_NODE // '@'
            JmesPathLexer.T__0,  // '.'
            JmesPathLexer.T__1,  // '!'
            JmesPathLexer.T__2,  // '&&'
            JmesPathLexer.T__3,  // '||'
            JmesPathLexer.T__6,  // '|'
            JmesPathLexer.T__7,  // '*'
            JmesPathLexer.T__16  // '&'
                -> OPERATOR
            JmesPathLexer.T__4,  // '('
            JmesPathLexer.T__5   // ')'
                -> PARENTHESES
            JmesPathLexer.T__8,  // '['
            JmesPathLexer.T__10, // ']'
            JmesPathLexer.T__14  // '[?'
                -> BRACKETS
            JmesPathLexer.T__11, // '{'
            JmesPathLexer.T__12  // '}'
                -> BRACES
            JmesPathLexer.T__9   // ','
                -> COMMA
            JmesPathLexer.T__13  // ':'
                -> COLON
            JmesPathLexer.T__17  // '`'
                -> LITERAL_TICK
            JmesPathLexer.WS -> WHITE_SPACE
            else -> BAD_CHARACTER
        }
    }
}
