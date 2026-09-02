package my.example.jmespath

import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.Token

class JMESPathLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var startOffset: Int = 0
    private var endOffset: Int = 0

    private var currentPosition: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentTokenType: IElementType? = null

    private var antlrLexer: JmesPathLexer? = null
    private var pendingToken: Token? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset
        this.currentPosition = startOffset

        val textToLex = if (startOffset < endOffset) buffer.subSequence(startOffset, endOffset).toString() else ""
        val charStream = CharStreams.fromString(textToLex)
        val lexer = JmesPathLexer(charStream)
        lexer.removeErrorListeners()
        this.antlrLexer = lexer
        this.pendingToken = if (startOffset < endOffset) lexer.nextToken() else null

        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentTokenType

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        if (currentPosition >= endOffset) {
            currentTokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        val token = pendingToken
        if (token == null || token.type == Token.EOF) {
            val gapStart = currentPosition
            val isWs = Character.isWhitespace(buffer[gapStart])
            var gapEnd = gapStart + 1
            while (gapEnd < endOffset && Character.isWhitespace(buffer[gapEnd]) == isWs) {
                gapEnd++
            }
            tokenStart = gapStart
            tokenEnd = gapEnd
            currentPosition = gapEnd
            currentTokenType = if (isWs) JMESPathTokenTypes.WHITE_SPACE else TokenType.BAD_CHARACTER
            return
        }

        val tokenStartInDoc = startOffset + token.startIndex
        val tokenEndInDoc = (startOffset + token.stopIndex + 1).coerceAtMost(endOffset)

        if (currentPosition < tokenStartInDoc) {
            val gapStart = currentPosition
            val isWs = Character.isWhitespace(buffer[gapStart])
            var gapEnd = gapStart + 1
            while (gapEnd < tokenStartInDoc && Character.isWhitespace(buffer[gapEnd]) == isWs) {
                gapEnd++
            }
            tokenStart = gapStart
            tokenEnd = gapEnd
            currentPosition = gapEnd
            currentTokenType = if (isWs) JMESPathTokenTypes.WHITE_SPACE else TokenType.BAD_CHARACTER
            return
        }

        tokenStart = tokenStartInDoc
        tokenEnd = tokenEndInDoc
        currentPosition = tokenEndInDoc
        val nextToken = antlrLexer?.nextToken()
        if (token.type == JmesPathLexer.NAME &&
            (nextToken?.type == JmesPathLexer.T__4 || JMESPathTokenTypes.BUILTIN_FUNCTIONS.contains(token.text))
        ) {
            currentTokenType = JMESPathTokenTypes.FUNCTION
        } else {
            currentTokenType = JMESPathTokenTypes.getIElementType(token.type)
        }
        pendingToken = nextToken
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = endOffset
}
