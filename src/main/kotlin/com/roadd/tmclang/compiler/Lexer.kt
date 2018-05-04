package com.roadd.tmclang.compiler

import java.io.IOException
import com.roadd.tmclang.compiler.Token.TokenType.*
import java.lang.Math.min

class Lexer(text: String) {
    private val tokens = text.tokenize()
    private var pos = 0
    private var mark = 0

    fun mark() {
        mark = pos
    }

    fun reset() {
        pos = mark
    }

    val currentToken
        get() = tokens[pos]

    fun nextToken(): Token {
        pos++

        val t = try {
            tokens[pos]
        } catch (e: IndexOutOfBoundsException) {
                    throw IOException("There are no more available tokens.")
        }

        if (t.type == INVALID) {
            throw IOException("Invalid token.")
        }

        return t
    }

    fun nextNonCommentToken(): Token{
        var t = nextToken()
        while(t.type == COMMENT) {
            t = nextToken()
        }
        return t
    }
}