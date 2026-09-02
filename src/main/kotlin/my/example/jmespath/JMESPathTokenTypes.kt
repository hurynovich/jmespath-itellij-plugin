package my.example.jmespath

import com.intellij.psi.TokenType

object JMESPathTokenTypes {
    @JvmField
    val KEYWORD = JMESPathTokenType("JMESPATH_KEYWORD")

    @JvmField
    val IDENTIFIER = JMESPathTokenType("JMESPATH_IDENTIFIER")

    @JvmField
    val WHITE_SPACE = TokenType.WHITE_SPACE

    @JvmField
    val BAD_CHARACTER = TokenType.BAD_CHARACTER
}
