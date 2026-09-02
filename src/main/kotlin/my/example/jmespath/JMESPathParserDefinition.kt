package my.example.jmespath

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet

class JMESPathParserDefinition : ParserDefinition {
    companion object {
        val FILE = IFileElementType(JMESPathLanguage.INSTANCE)
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, JMESPathTokenTypes.WHITE_SPACE)
        val COMMENTS = TokenSet.EMPTY
        val STRINGS = TokenSet.create(JMESPathTokenTypes.STRING)
    }

    override fun createLexer(project: Project?): Lexer = JMESPathLexer()

    override fun createParser(project: Project?): PsiParser = JMESPathPsiParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRINGS

    override fun createElement(node: ASTNode): PsiElement = JMESPathPsiElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = JMESPathFile(viewProvider)
}
