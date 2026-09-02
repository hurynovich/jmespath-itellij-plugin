package my.example.jmespath.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock
import my.example.jmespath.JMESPathElementTypes
import my.example.jmespath.JMESPathLanguage
import my.example.jmespath.JMESPathTokenTypes
import java.util.ArrayList

class JMESPathBlock(
    node: ASTNode,
    wrap: Wrap?,
    alignment: Alignment?,
    private val spacingBuilder: SpacingBuilder,
    private val settings: CodeStyleSettings,
    private val indent: Indent
) : AbstractBlock(node, wrap, alignment) {

    override fun buildChildren(): List<Block> {
        if (isLeaf) return emptyList()
        val blocks = ArrayList<Block>()
        val defaultWrap = createDefaultWrap()
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType != TokenType.WHITE_SPACE &&
                child.elementType != JMESPathTokenTypes.WHITE_SPACE &&
                child.textLength > 0
            ) {
                val childIndent = computeChildIndent(child)
                val childWrap = computeChildWrap(child, defaultWrap)
                blocks.add(JMESPathBlock(child, childWrap, null, spacingBuilder, settings, childIndent))
            }
            child = child.treeNext
        }
        return blocks
    }

    private fun createDefaultWrap(): Wrap? {
        val parentType = node.elementType
        return when (parentType) {
            JMESPathElementTypes.OBJECT_HASH,
            JMESPathElementTypes.MULTI_SELECT_LIST,
            JMESPathElementTypes.ARGUMENT_LIST,
            JMESPathElementTypes.PIPE_EXPRESSION,
            JMESPathElementTypes.BINARY_EXPRESSION -> Wrap.createWrap(WrapType.NORMAL, false)
            else -> null
        }
    }

    private fun computeChildWrap(child: ASTNode, defaultWrap: Wrap?): Wrap? {
        val childType = child.elementType
        if (childType == JMESPathTokenTypes.LBRACE || childType == JMESPathTokenTypes.RBRACE ||
            childType == JMESPathTokenTypes.LBRACKET || childType == JMESPathTokenTypes.RBRACKET ||
            childType == JMESPathTokenTypes.LPAREN || childType == JMESPathTokenTypes.RPAREN ||
            childType == JMESPathTokenTypes.COMMA
        ) {
            return null
        }
        return defaultWrap
    }

    override fun getIndent(): Indent = indent

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 !is AbstractBlock || child2 !is AbstractBlock) return null

        val node1 = child1.node
        val node2 = child2.node
        val type1 = node1.elementType
        val type2 = node2.elementType
        val text1 = node1.text
        val text2 = node2.text
        val commonSettings = settings.getCommonSettings(JMESPathLanguage.INSTANCE)

        // Slice colons: [1:10:2] - no spaces around colons in slice
        if (node.elementType == JMESPathElementTypes.SLICE_EXPRESSION) {
            if (type1 == JMESPathTokenTypes.COLON || type2 == JMESPathTokenTypes.COLON) {
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        // Unary operators ! and & (expression type)
        if (type1 == JMESPathTokenTypes.OPERATOR && (text1 == "!" || text1 == "&")) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        // Dot operator: expr.name, .{}
        if ((type1 == JMESPathTokenTypes.OPERATOR && text1 == ".") ||
            (type2 == JMESPathTokenTypes.OPERATOR && text2 == ".")
        ) {
            return Spacing.createSpacing(0, 0, 0, commonSettings.KEEP_LINE_BREAKS, 0)
        }

        // Pipe operator: expr | expr
        if ((type1 == JMESPathTokenTypes.OPERATOR && text1 == "|") ||
            (type2 == JMESPathTokenTypes.OPERATOR && text2 == "|") ||
            type1 == JMESPathElementTypes.PIPE_EXPRESSION ||
            type2 == JMESPathElementTypes.PIPE_EXPRESSION
        ) {
            val spaces = if (commonSettings.SPACE_AROUND_LOGICAL_OPERATORS) 1 else 0
            return Spacing.createSpacing(spaces, spaces, 0, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_IN_CODE)
        }

        // Logical operators && and ||
        if ((type1 == JMESPathTokenTypes.OPERATOR && (text1 == "&&" || text1 == "||")) ||
            (type2 == JMESPathTokenTypes.OPERATOR && (text2 == "&&" || text2 == "||"))
        ) {
            val spaces = if (commonSettings.SPACE_AROUND_LOGICAL_OPERATORS) 1 else 0
            return Spacing.createSpacing(spaces, spaces, 0, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_IN_CODE)
        }

        // Comparators: ==, != (equality) vs <, <=, >, >= (relational)
        if (type1 == JMESPathTokenTypes.COMPARATOR || type2 == JMESPathTokenTypes.COMPARATOR) {
            val compText = if (type1 == JMESPathTokenTypes.COMPARATOR) text1 else text2
            val hasSpace = if (compText == "==" || compText == "!=") {
                commonSettings.SPACE_AROUND_EQUALITY_OPERATORS
            } else {
                commonSettings.SPACE_AROUND_RELATIONAL_OPERATORS
            }
            val spaces = if (hasSpace) 1 else 0
            return Spacing.createSpacing(spaces, spaces, 0, commonSettings.KEEP_LINE_BREAKS, commonSettings.KEEP_BLANK_LINES_IN_CODE)
        }

        // Function call opening parenthesis: func(args)
        if (type2 == JMESPathTokenTypes.LPAREN || type2 == JMESPathElementTypes.ARGUMENT_LIST) {
            if (type1 == JMESPathTokenTypes.FUNCTION || type1 == JMESPathTokenTypes.IDENTIFIER) {
                return Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        // Bracket indexing or projections: items[0], people[?...]
        if (type2 == JMESPathTokenTypes.LBRACKET ||
            type2 == JMESPathElementTypes.INDEX_EXPRESSION ||
            type2 == JMESPathElementTypes.FILTER_EXPRESSION ||
            type2 == JMESPathElementTypes.SLICE_EXPRESSION ||
            type2 == JMESPathElementTypes.MULTI_SELECT_LIST
        ) {
            if (type1 == JMESPathTokenTypes.IDENTIFIER ||
                type1 == JMESPathElementTypes.EXPRESSION ||
                type1 == JMESPathTokenTypes.RBRACKET ||
                type1 == JMESPathTokenTypes.RPAREN ||
                type1 == JMESPathElementTypes.FILTER_EXPRESSION ||
                type1 == JMESPathElementTypes.INDEX_EXPRESSION ||
                type1 == JMESPathElementTypes.SLICE_EXPRESSION ||
                type1 == JMESPathElementTypes.PAREN_EXPRESSION
            ) {
                return Spacing.createSpacing(0, 0, 0, commonSettings.KEEP_LINE_BREAKS, 0)
            }
        }

        // Empty delimiters: {}, [], ()
        if ((type1 == JMESPathTokenTypes.LBRACE && type2 == JMESPathTokenTypes.RBRACE) ||
            (type1 == JMESPathTokenTypes.LBRACKET && type2 == JMESPathTokenTypes.RBRACKET) ||
            (type1 == JMESPathTokenTypes.LPAREN && type2 == JMESPathTokenTypes.RPAREN)
        ) {
            return Spacing.createSpacing(0, 0, 0, false, 0)
        }

        // Delimiters inner spacing
        if (type1 == JMESPathTokenTypes.LBRACE || type1 == JMESPathTokenTypes.LBRACKET || type1 == JMESPathTokenTypes.LPAREN) {
            return if (commonSettings.KEEP_LINE_BREAKS) {
                Spacing.createDependentLFSpacing(0, 0, node.textRange, true, commonSettings.KEEP_BLANK_LINES_IN_CODE)
            } else {
                Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }
        if (type2 == JMESPathTokenTypes.RBRACE || type2 == JMESPathTokenTypes.RBRACKET || type2 == JMESPathTokenTypes.RPAREN) {
            return if (commonSettings.KEEP_LINE_BREAKS) {
                Spacing.createDependentLFSpacing(0, 0, node.textRange, true, commonSettings.KEEP_BLANK_LINES_IN_CODE)
            } else {
                Spacing.createSpacing(0, 0, 0, false, 0)
            }
        }

        return spacingBuilder.getSpacing(this, child1, child2)
    }

    override fun isLeaf(): Boolean {
        return node.firstChildNode == null ||
            node.elementType == JMESPathElementTypes.LITERAL_EXPRESSION ||
            node.elementType == JMESPathTokenTypes.STRING
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val parentType = node.elementType
        return if (parentType == JMESPathElementTypes.OBJECT_HASH ||
            parentType == JMESPathElementTypes.MULTI_SELECT_LIST ||
            parentType == JMESPathElementTypes.FILTER_EXPRESSION ||
            parentType == JMESPathElementTypes.PAREN_EXPRESSION ||
            parentType == JMESPathElementTypes.ARGUMENT_LIST
        ) {
            ChildAttributes(Indent.getNormalIndent(), null)
        } else {
            ChildAttributes(Indent.getNoneIndent(), null)
        }
    }

    private fun computeChildIndent(child: ASTNode): Indent {
        val parentType = node.elementType
        val childType = child.elementType

        if (parentType == JMESPathElementTypes.OBJECT_HASH) {
            if (childType == JMESPathTokenTypes.LBRACE || childType == JMESPathTokenTypes.RBRACE) {
                return Indent.getNoneIndent()
            }
            return Indent.getNormalIndent()
        }

        if (parentType == JMESPathElementTypes.MULTI_SELECT_LIST) {
            if (childType == JMESPathTokenTypes.LBRACKET || childType == JMESPathTokenTypes.RBRACKET) {
                return Indent.getNoneIndent()
            }
            return Indent.getNormalIndent()
        }

        if (parentType == JMESPathElementTypes.FILTER_EXPRESSION) {
            // In expr[?condition], the base expression has None indent, while condition inside [?...] has Normal indent
            if (child == node.firstChildNode && childType != JMESPathTokenTypes.LBRACKET) {
                return Indent.getNoneIndent()
            }
            if (childType == JMESPathTokenTypes.LBRACKET || childType == JMESPathTokenTypes.RBRACKET) {
                return Indent.getNoneIndent()
            }
            return Indent.getNormalIndent()
        }

        if (parentType == JMESPathElementTypes.PAREN_EXPRESSION) {
            if (childType == JMESPathTokenTypes.LPAREN || childType == JMESPathTokenTypes.RPAREN) {
                return Indent.getNoneIndent()
            }
            return Indent.getNormalIndent()
        }

        if (parentType == JMESPathElementTypes.ARGUMENT_LIST) {
            if (childType == JMESPathTokenTypes.LPAREN || childType == JMESPathTokenTypes.RPAREN) {
                return Indent.getNoneIndent()
            }
            return Indent.getNormalIndent()
        }

        return Indent.getNoneIndent()
    }
}
