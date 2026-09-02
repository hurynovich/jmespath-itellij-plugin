package my.example.jmespath

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class JMESPathFileType private constructor() : LanguageFileType(JMESPathLanguage.INSTANCE) {
    override fun getName(): String = "JMESPath"

    override fun getDescription(): String = "JMESPath file"

    override fun getDefaultExtension(): String = "jmespath"

    override fun getIcon(): Icon = JMESPathIcons.FILE

    companion object {
        @JvmField
        val INSTANCE: JMESPathFileType = JMESPathFileType()
    }
}
