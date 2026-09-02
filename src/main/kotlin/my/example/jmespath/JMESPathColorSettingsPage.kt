package my.example.jmespath

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import my.example.MyMessageBundle
import javax.swing.Icon

class JMESPathColorSettingsPage : ColorSettingsPage {
    private val descriptors = arrayOf(
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.keyword"),
            JMESPathSyntaxHighlighter.KEYWORD
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.bad_character"),
            JMESPathSyntaxHighlighter.BAD_CHARACTER
        )
    )

    override fun getIcon(): Icon = JMESPathIcons.FILE

    override fun getHighlighter(): SyntaxHighlighter = JMESPathSyntaxHighlighter()

    override fun getDemoText(): String {
        return """
            locations[?state == 'WA'].name | sort(@) | {
                first: [0],
                active: true,
                disabled: false,
                emptyValue: null
            }
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = MyMessageBundle.message("jmespath.color.settings.display.name")
}
