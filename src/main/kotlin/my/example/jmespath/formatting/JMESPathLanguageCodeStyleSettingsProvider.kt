package my.example.jmespath.formatting

import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider
import my.example.jmespath.JMESPathLanguage

class JMESPathLanguageCodeStyleSettingsProvider : LanguageCodeStyleSettingsProvider() {
    override fun getLanguage(): Language = JMESPathLanguage.INSTANCE

    override fun customizeDefaults(
        commonSettings: CommonCodeStyleSettings,
        indentOptions: CommonCodeStyleSettings.IndentOptions
    ) {
        indentOptions.INDENT_SIZE = 2
        indentOptions.TAB_SIZE = 2
        indentOptions.CONTINUATION_INDENT_SIZE = 2
        commonSettings.SPACE_AROUND_LOGICAL_OPERATORS = true
        commonSettings.SPACE_AROUND_EQUALITY_OPERATORS = true
        commonSettings.SPACE_AROUND_RELATIONAL_OPERATORS = true
        commonSettings.SPACE_AFTER_COMMA = true
        commonSettings.SPACE_BEFORE_COMMA = false
        commonSettings.SPACE_AFTER_COLON = true
        commonSettings.SPACE_BEFORE_COLON = false
        commonSettings.KEEP_LINE_BREAKS = true
    }

    override fun customizeSettings(consumer: CodeStyleSettingsCustomizable, settingsType: SettingsType) {
        when (settingsType) {
            SettingsType.SPACING_SETTINGS -> {
                consumer.showStandardOptions(
                    "SPACE_AROUND_LOGICAL_OPERATORS",
                    "SPACE_AROUND_EQUALITY_OPERATORS",
                    "SPACE_AROUND_RELATIONAL_OPERATORS",
                    "SPACE_AFTER_COMMA",
                    "SPACE_BEFORE_COMMA",
                    "SPACE_AFTER_COLON",
                    "SPACE_BEFORE_COLON"
                )
            }
            SettingsType.WRAPPING_AND_BRACES_SETTINGS -> {
                consumer.showStandardOptions(
                    "KEEP_LINE_BREAKS",
                    "WRAP_LONG_LINES",
                    "RIGHT_MARGIN"
                )
            }
            else -> {}
        }
    }

    override fun getCodeSample(settingsType: SettingsType): String {
        return """
            people[?
              age >= `18` &&
              active == `true`
            ].{
              name: name,
              tags: tags[*],
              scores: scores[1:10:2],
              joined: join(', ', tags)
            } | sort_by(@, &name)
        """.trimIndent()
    }
}
