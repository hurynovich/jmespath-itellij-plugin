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
            MyMessageBundle.message("jmespath.colors.current_node"),
            JMESPathSyntaxHighlighter.CURRENT_NODE
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.identifier"),
            JMESPathSyntaxHighlighter.IDENTIFIER
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.string"),
            JMESPathSyntaxHighlighter.STRING
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.number"),
            JMESPathSyntaxHighlighter.NUMBER
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.operator"),
            JMESPathSyntaxHighlighter.OPERATOR
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.comparator"),
            JMESPathSyntaxHighlighter.COMPARATOR
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.parentheses"),
            JMESPathSyntaxHighlighter.PARENTHESES
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.brackets"),
            JMESPathSyntaxHighlighter.BRACKETS
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.braces"),
            JMESPathSyntaxHighlighter.BRACES
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.comma"),
            JMESPathSyntaxHighlighter.COMMA
        ),
        AttributesDescriptor(
            MyMessageBundle.message("jmespath.colors.colon"),
            JMESPathSyntaxHighlighter.COLON
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
                "first": [0],
                "active": true,
                "disabled": false,
                "emptyValue": null,
                "count": 42
            }
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = descriptors

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String = MyMessageBundle.message("jmespath.color.settings.display.name")
}
