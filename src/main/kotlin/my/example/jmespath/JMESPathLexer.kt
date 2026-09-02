package my.example.jmespath

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class JMESPathLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0
    private var currentPosition: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentTokenType: IElementType? = null

    companion object {
        private val KEYWORDS = setOf("true", "false", "null")
    }

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentPosition = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentTokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        if (currentPosition >= endOffset) {
            currentTokenType = null
            tokenStart = currentPosition
            tokenEnd = currentPosition
            return
        }

        tokenStart = currentPosition
        val c = buffer[currentPosition]

        when {
            Character.isWhitespace(c) -> {
                while (currentPosition < endOffset && Character.isWhitespace(buffer[currentPosition])) {
                    currentPosition++
                }
                tokenEnd = currentPosition
                currentTokenType = JMESPathTokenTypes.WHITE_SPACE
            }
            c == '_' || Character.isLetter(c) -> {
                while (currentPosition < endOffset && (buffer[currentPosition] == '_' || Character.isLetterOrDigit(buffer[currentPosition]))) {
                    currentPosition++
                }
                tokenEnd = currentPosition
                val text = buffer.subSequence(tokenStart, tokenEnd).toString()
                currentTokenType = if (KEYWORDS.contains(text)) {
                    JMESPathTokenTypes.KEYWORD
                } else {
                    JMESPathTokenTypes.IDENTIFIER
                }
            }
            else -> {
                currentPosition++
                tokenEnd = currentPosition
                currentTokenType = JMESPathTokenTypes.BAD_CHARACTER
            }
        }
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset
}
