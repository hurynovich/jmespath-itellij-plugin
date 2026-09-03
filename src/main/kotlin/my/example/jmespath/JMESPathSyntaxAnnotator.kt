package my.example.jmespath

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement

class JMESPathSyntaxAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.node.elementType == JMESPathElementTypes.KEY_NAME) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(JMESPathSyntaxHighlighter.KEY_NAME)
                .create()
        }

        if (element.node.elementType == JMESPathElementTypes.GLOBAL_NODE) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(JMESPathSyntaxHighlighter.GLOBAL_NODE)
                .create()
        }
    }
}
