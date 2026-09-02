package my.example.jmespath

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType

class JMESPathPsiParser : PsiParser {
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        while (!builder.eof()) {
            if (!parseExpression(builder, 0)) {
                builder.advanceLexer()
            }
        }
        rootMarker.done(root)
        return builder.treeBuilt
    }

    private fun parseExpression(builder: PsiBuilder, minPrecedence: Int): Boolean {
        var marker = parsePrefix(builder) ?: return false

        while (!builder.eof()) {
            val tokenType = builder.tokenType ?: break
            val tokenText = builder.tokenText

            if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == ".") {
                if (minPrecedence > 6) break
                marker = marker.precede()
                builder.advanceLexer()
                val next = parsePrefix(builder)
                if (next == null && !builder.eof() && builder.tokenType != JMESPathTokenTypes.RPAREN && builder.tokenType != JMESPathTokenTypes.RBRACE && builder.tokenType != JMESPathTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                }
                marker.done(JMESPathElementTypes.EXPRESSION)
                continue
            }

            if (tokenType == JMESPathTokenTypes.LBRACKET) {
                if (minPrecedence > 6) break
                marker = marker.precede()
                if (tokenText == "[?") {
                    builder.advanceLexer()
                    parseExpression(builder, 0)
                    if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                    }
                    marker.done(JMESPathElementTypes.FILTER_EXPRESSION)
                } else {
                    builder.advanceLexer()
                    if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                        marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    } else if (isSliceAt(builder)) {
                        parseSlice(builder)
                        if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                            builder.advanceLexer()
                        }
                        marker.done(JMESPathElementTypes.SLICE_EXPRESSION)
                    } else if (builder.tokenType == JMESPathTokenTypes.OPERATOR && builder.tokenText == "*") {
                        builder.advanceLexer()
                        if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                            builder.advanceLexer()
                        }
                        marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    } else if (builder.tokenType == JMESPathTokenTypes.NUMBER && builder.lookAhead(1) == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                        builder.advanceLexer()
                        marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    } else {
                        while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.RBRACKET) {
                            parseExpression(builder, 0)
                            if (builder.tokenType == JMESPathTokenTypes.COMMA) {
                                builder.advanceLexer()
                            } else {
                                break
                            }
                        }
                        if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                            builder.advanceLexer()
                        }
                        marker.done(JMESPathElementTypes.MULTI_SELECT_LIST)
                    }
                }
                continue
            }

            if (tokenType == JMESPathTokenTypes.COMPARATOR) {
                if (minPrecedence > 4) break
                marker = marker.precede()
                builder.advanceLexer()
                parseExpression(builder, 5)
                marker.done(JMESPathElementTypes.BINARY_EXPRESSION)
                continue
            }

            if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "&&") {
                if (minPrecedence > 3) break
                marker = marker.precede()
                builder.advanceLexer()
                parseExpression(builder, 4)
                marker.done(JMESPathElementTypes.BINARY_EXPRESSION)
                continue
            }

            if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "||") {
                if (minPrecedence > 2) break
                marker = marker.precede()
                builder.advanceLexer()
                parseExpression(builder, 3)
                marker.done(JMESPathElementTypes.BINARY_EXPRESSION)
                continue
            }

            if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "|") {
                if (minPrecedence > 1) break
                marker = marker.precede()
                builder.advanceLexer()
                parseExpression(builder, 2)
                marker.done(JMESPathElementTypes.PIPE_EXPRESSION)
                continue
            }

            break
        }

        return true
    }

    private fun parsePrefix(builder: PsiBuilder): PsiBuilder.Marker? {
        val tokenType = builder.tokenType ?: return null
        val tokenText = builder.tokenText

        if (tokenType == JMESPathTokenTypes.LPAREN) {
            val marker = builder.mark()
            builder.advanceLexer()
            parseExpression(builder, 0)
            if (builder.tokenType == JMESPathTokenTypes.RPAREN) {
                builder.advanceLexer()
            }
            marker.done(JMESPathElementTypes.PAREN_EXPRESSION)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.LBRACE) {
            val marker = builder.mark()
            builder.advanceLexer()
            while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.RBRACE) {
                val pairMarker = builder.mark()
                if (builder.tokenType == JMESPathTokenTypes.IDENTIFIER ||
                    builder.tokenType == JMESPathTokenTypes.STRING ||
                    builder.tokenType == JMESPathTokenTypes.KEYWORD
                ) {
                    builder.advanceLexer()
                }
                if (builder.tokenType == JMESPathTokenTypes.COLON) {
                    builder.advanceLexer()
                    parseExpression(builder, 0)
                }
                pairMarker.done(JMESPathElementTypes.KEYVAL_EXPR)
                if (builder.tokenType == JMESPathTokenTypes.COMMA) {
                    builder.advanceLexer()
                } else {
                    break
                }
            }
            if (builder.tokenType == JMESPathTokenTypes.RBRACE) {
                builder.advanceLexer()
            }
            marker.done(JMESPathElementTypes.OBJECT_HASH)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.LBRACKET) {
            val marker = builder.mark()
            if (tokenText == "[?") {
                builder.advanceLexer()
                parseExpression(builder, 0)
                if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                }
                marker.done(JMESPathElementTypes.FILTER_EXPRESSION)
                return marker
            } else {
                builder.advanceLexer()
                if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                    marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    return marker
                } else if (isSliceAt(builder)) {
                    parseSlice(builder)
                    if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                    }
                    marker.done(JMESPathElementTypes.SLICE_EXPRESSION)
                    return marker
                } else if (builder.tokenType == JMESPathTokenTypes.OPERATOR && builder.tokenText == "*") {
                    builder.advanceLexer()
                    if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                    }
                    marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    return marker
                } else if (builder.tokenType == JMESPathTokenTypes.NUMBER && builder.lookAhead(1) == JMESPathTokenTypes.RBRACKET) {
                    builder.advanceLexer()
                    builder.advanceLexer()
                    marker.done(JMESPathElementTypes.INDEX_EXPRESSION)
                    return marker
                } else {
                    while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.RBRACKET) {
                        parseExpression(builder, 0)
                        if (builder.tokenType == JMESPathTokenTypes.COMMA) {
                            builder.advanceLexer()
                        } else {
                            break
                        }
                    }
                    if (builder.tokenType == JMESPathTokenTypes.RBRACKET) {
                        builder.advanceLexer()
                    }
                    marker.done(JMESPathElementTypes.MULTI_SELECT_LIST)
                    return marker
                }
            }
        }

        if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "!") {
            val marker = builder.mark()
            builder.advanceLexer()
            parseExpression(builder, 5)
            marker.done(JMESPathElementTypes.NOT_EXPRESSION)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "&") {
            val marker = builder.mark()
            builder.advanceLexer()
            parseExpression(builder, 5)
            marker.done(JMESPathElementTypes.EXPRESSION)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.LITERAL_TICK) {
            val marker = builder.mark()
            builder.advanceLexer()
            while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.LITERAL_TICK) {
                builder.advanceLexer()
            }
            if (builder.tokenType == JMESPathTokenTypes.LITERAL_TICK) {
                builder.advanceLexer()
            }
            marker.done(JMESPathElementTypes.LITERAL_EXPRESSION)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.FUNCTION ||
            (tokenType == JMESPathTokenTypes.IDENTIFIER && builder.lookAhead(1) == JMESPathTokenTypes.LPAREN)
        ) {
            val marker = builder.mark()
            builder.advanceLexer()
            if (builder.tokenType == JMESPathTokenTypes.LPAREN) {
                val argsMarker = builder.mark()
                builder.advanceLexer()
                while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.RPAREN) {
                    if (builder.tokenType == JMESPathTokenTypes.OPERATOR && builder.tokenText == "&") {
                        val refMarker = builder.mark()
                        builder.advanceLexer()
                        parseExpression(builder, 0)
                        refMarker.done(JMESPathElementTypes.EXPRESSION)
                    } else {
                        parseExpression(builder, 0)
                    }
                    if (builder.tokenType == JMESPathTokenTypes.COMMA) {
                        builder.advanceLexer()
                    } else {
                        break
                    }
                }
                if (builder.tokenType == JMESPathTokenTypes.RPAREN) {
                    builder.advanceLexer()
                }
                argsMarker.done(JMESPathElementTypes.ARGUMENT_LIST)
            }
            marker.done(JMESPathElementTypes.FUNCTION_EXPRESSION)
            return marker
        }

        if (tokenType == JMESPathTokenTypes.IDENTIFIER ||
            tokenType == JMESPathTokenTypes.KEYWORD ||
            tokenType == JMESPathTokenTypes.NUMBER ||
            tokenType == JMESPathTokenTypes.STRING ||
            tokenType == JMESPathTokenTypes.CURRENT_NODE ||
            (tokenType == JMESPathTokenTypes.OPERATOR && tokenText == "*")
        ) {
            val marker = builder.mark()
            builder.advanceLexer()
            marker.done(JMESPathElementTypes.EXPRESSION)
            return marker
        }

        return null
    }

    private fun isSliceAt(builder: PsiBuilder): Boolean {
        val t0 = builder.tokenType
        if (t0 == JMESPathTokenTypes.COLON) return true
        if (t0 == JMESPathTokenTypes.NUMBER) {
            return builder.lookAhead(1) == JMESPathTokenTypes.COLON
        }
        return false
    }

    private fun parseSlice(builder: PsiBuilder) {
        while (!builder.eof() && builder.tokenType != JMESPathTokenTypes.RBRACKET) {
            if (builder.tokenType == JMESPathTokenTypes.NUMBER || builder.tokenType == JMESPathTokenTypes.COLON) {
                builder.advanceLexer()
            } else {
                break
            }
        }
    }
}
