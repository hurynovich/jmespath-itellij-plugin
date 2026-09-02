package my.example.jmespath

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class JMESPathTokenType(@NonNls debugName: String) : IElementType(debugName, JMESPathLanguage.INSTANCE) {
    override fun toString(): String = "JMESPathTokenType." + super.toString()
}
