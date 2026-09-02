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
    val LPAREN = JMESPathTokenType("JMESPATH_LPAREN")

    @JvmField
    val RPAREN = JMESPathTokenType("JMESPATH_RPAREN")

    @JvmField
    val LBRACKET = JMESPathTokenType("JMESPATH_LBRACKET")

    @JvmField
    val RBRACKET = JMESPathTokenType("JMESPATH_RBRACKET")

    @JvmField
    val LBRACE = JMESPathTokenType("JMESPATH_LBRACE")

    @JvmField
    val RBRACE = JMESPathTokenType("JMESPATH_RBRACE")

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
            JmesPathLexer.T__4   // '('
                -> LPAREN
            JmesPathLexer.T__5   // ')'
                -> RPAREN
            JmesPathLexer.T__8,  // '['
            JmesPathLexer.T__14  // '[?'
                -> LBRACKET
            JmesPathLexer.T__10  // ']'
                -> RBRACKET
            JmesPathLexer.T__11  // '{'
                -> LBRACE
            JmesPathLexer.T__12  // '}'
                -> RBRACE
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
