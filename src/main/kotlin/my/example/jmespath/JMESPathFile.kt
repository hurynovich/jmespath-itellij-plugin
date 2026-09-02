package my.example.jmespath

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class JMESPathFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JMESPathLanguage.INSTANCE) {
    override fun getFileType(): FileType = JMESPathFileType.INSTANCE
    override fun toString(): String = "JMESPath File"
}
