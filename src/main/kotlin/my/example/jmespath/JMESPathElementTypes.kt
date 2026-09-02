package my.example.jmespath

import com.intellij.psi.tree.IElementType

class JMESPathElementType(debugName: String) : IElementType(debugName, JMESPathLanguage.INSTANCE)

object JMESPathElementTypes {
    @JvmField val EXPRESSION = JMESPathElementType("EXPRESSION")
    @JvmField val OBJECT_HASH = JMESPathElementType("OBJECT_HASH")
    @JvmField val KEYVAL_EXPR = JMESPathElementType("KEYVAL_EXPR")
    @JvmField val OBJECT_PAIR = KEYVAL_EXPR
    @JvmField val MULTI_SELECT_LIST = JMESPathElementType("MULTI_SELECT_LIST")
    @JvmField val FILTER_EXPRESSION = JMESPathElementType("FILTER_EXPRESSION")
    @JvmField val FILTER_PROJECTION = FILTER_EXPRESSION
    @JvmField val FUNCTION_EXPRESSION = JMESPathElementType("FUNCTION_EXPRESSION")
    @JvmField val FUNCTION_CALL = FUNCTION_EXPRESSION
    @JvmField val ARGUMENT_LIST = JMESPathElementType("ARGUMENT_LIST")
    @JvmField val BINARY_EXPRESSION = JMESPathElementType("BINARY_EXPRESSION")
    @JvmField val PAREN_EXPRESSION = JMESPathElementType("PAREN_EXPRESSION")
    @JvmField val PARENTHESIZED_EXPRESSION = PAREN_EXPRESSION
    @JvmField val SLICE_EXPRESSION = JMESPathElementType("SLICE_EXPRESSION")
    @JvmField val INDEX_EXPRESSION = JMESPathElementType("INDEX_EXPRESSION")
    @JvmField val PIPE_EXPRESSION = JMESPathElementType("PIPE_EXPRESSION")
    @JvmField val NOT_EXPRESSION = JMESPathElementType("NOT_EXPRESSION")
    @JvmField val LITERAL_EXPRESSION = JMESPathElementType("LITERAL_EXPRESSION")
}
