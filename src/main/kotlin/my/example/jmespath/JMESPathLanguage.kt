package my.example.jmespath

import com.intellij.lang.Language

class JMESPathLanguage private constructor() : Language("JMESPath") {
    companion object {
        @JvmField
        val INSTANCE: JMESPathLanguage = JMESPathLanguage()
    }
}
