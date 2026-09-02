package my.example.jmespath

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class JMESPathPairedBraceMatcher : PairedBraceMatcher {
    companion object {
        private val PAIRS = arrayOf(
            BracePair(JMESPathTokenTypes.LPAREN, JMESPathTokenTypes.RPAREN, false),
            BracePair(JMESPathTokenTypes.LBRACKET, JMESPathTokenTypes.RBRACKET, false),
            BracePair(JMESPathTokenTypes.LBRACE, JMESPathTokenTypes.RBRACE, true)
        )
    }

    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(
        lbraceType: IElementType,
        contextType: IElementType?
    ): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
}
