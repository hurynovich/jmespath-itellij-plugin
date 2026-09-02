package my.example.jmespath.formatting

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import my.example.jmespath.JMESPathLanguage
import my.example.jmespath.JMESPathTokenTypes

class JMESPathFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val codeStyleSettings = formattingContext.codeStyleSettings
        val spacingBuilder = createSpacingBuilder(codeStyleSettings)
        val rootBlock = JMESPathBlock(
            node = formattingContext.node,
            wrap = null,
            alignment = null,
            spacingBuilder = spacingBuilder,
            settings = codeStyleSettings,
            indent = Indent.getNoneIndent()
        )
        return FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.containingFile,
            rootBlock,
            codeStyleSettings
        )
    }

    companion object {
        fun createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder {
            val common = settings.getCommonSettings(JMESPathLanguage.INSTANCE)
            return SpacingBuilder(settings, JMESPathLanguage.INSTANCE)
                .around(JMESPathTokenTypes.COMPARATOR).spaceIf(common.SPACE_AROUND_EQUALITY_OPERATORS || common.SPACE_AROUND_RELATIONAL_OPERATORS)
                .after(JMESPathTokenTypes.COMMA).spaceIf(common.SPACE_AFTER_COMMA, common.KEEP_LINE_BREAKS)
                .before(JMESPathTokenTypes.COMMA).spaceIf(common.SPACE_BEFORE_COMMA)
                .after(JMESPathTokenTypes.COLON).spaceIf(common.SPACE_AFTER_COLON)
                .before(JMESPathTokenTypes.COLON).spaceIf(common.SPACE_BEFORE_COLON)
        }
    }
}
