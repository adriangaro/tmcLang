@file:Suppress("RemoveSingleExpressionStringTemplate")

package com.roadd.tmclang.compiler

import com.roadd.tmclang.compiler.ast.Node
import java.util.regex.Pattern

fun String.tokenize(): List<Token> {
    val ret = mutableListOf<Token>()
    var matchResult = Token.TokenType.match(this)
    while(matchResult.token.type != Token.TokenType.EOF) {
        if(matchResult.token.type == Token.TokenType.INVALID) throw Exception("Invalid token: ${matchResult.token.value}")
        ret.add(matchResult.token)
        matchResult = Token.TokenType.match(matchResult.remainingStr)
    }
    ret.add(matchResult.token)
    return ret
}

data class Token(val type: TokenType, val value: String = ""): Node {
    override fun children(): List<Node> = arrayListOf()


    enum class TokenType(pattern: String) {
        COMMENT("^\\/\\/.*"),
        VARIABLE("^var\\b"),
        VALUE("^val\\b"),
        PRINT("^print\\b"),
        IMPORT("^import\\b"),
        PACKAGE("^package\\b"),
        AS("^as\\b"),
        CLASS("^class\\b"),
        INTERFACE("^interface\\b"),
        ABSTRACT("^abstract\\b"),
        ANNOTATION("^annotation\\b"),
        DATA("^data\\b"),
        OBJECT("^object\\b"),
        DEF("^def\\b"),
        ASSIGN("^\\="),
        PLUS_ASSIGN("^\\+\\="),
        MINUS_ASSIGN("^\\-\\="),
        MUL_ASSIGN("^\\*\\="),
        DIV_ASSIGN("^\\/\\="),
        MOD_ASSIGN("^\\%\\="),
        NULL_ASSIGN("^\\?\\="),
        INC("^\\+\\+"),
        DEC("^\\-\\-"),
        RANGE("^\\.\\."),
        SAFE_DOT("^\\?\\."),
        DOT("^\\."),
        COMMA("^\\,"),
        COLON("^\\:"),
        SEMI_COLON("^\\;"),
        PLUS("^\\+"),
        MINUS("^\\-(?!\\d)"),
        MUL("^\\*"),
        DIV("^\\/"),
        MOD("^\\%"),
        AND("^(&&|and$boolOperatorLookAhead)"),
        OR("^(\\|\\||or$boolOperatorLookAhead)"),
        NOT("^(\\!|not$boolOperatorLookAhead)"),
        BITWISE_AND("^\\&"),
        BITWISE_OR("^\\|"),
        BITWISE_XOR("^\\^"),
        BITWISE_NOT("^\\~"),
        BITWISE_LSHIFT("^<<"),
        BITWISE_RSHIFT("^>>"),
        LPAREN("^\\("),
        RPAREN("^\\)"),
        LBRACKET("^\\["),
        RBRACKET("^\\]"),
        LBRACE("^\\{"),
        RBRACE("^\\}"),
        DOUBLE("^-?\\d+(\\.\\d+)?[Dd]\\b$numberLookAhead"),
        FLOAT("^(-?\\d+(\\.\\d+)|-?\\d+(\\.\\d+)?[Ff])\\b$numberLookAhead"),
        BYTE("^$number[Bb]\\b$numberLookAhead"),
        SHORT("^$number[Ss]\\b$numberLookAhead"),
        LONG("^$number[Ll]\\b$numberLookAhead"),
        INT("^((0b[01]+)|(0x[0-9a-fA-F]+)|$number)\\b$numberLookAhead"),
        BOOL("^(true|false)\\b"),
        NULL("^(nil|null)\\b"),
        ID("^(([a-zA-Z_][a-zA-Z0-9_]*\\b)|(`[a-zA-Z_][a-zA-Z0-9_ ]*`))"),
        STRING("^([\"'])(?:(?=(\\\\?))\\2.)*?\\1"),
        EOF("^\\Z"),
        INVALID(".*");

        val pattern = Pattern.compile(pattern)!!

        companion object {
            fun match(str: String): MatchResult {
                val str = str.trim()
                values().forEach {
                    val matcher = it.pattern.matcher(str)
                    if(matcher.find()) {
                         return MatchResult(Token(it, matcher.group()), str.substring(matcher.end()).trim())
                    }
                }
                throw Exception()
            }
        }
    }

    data class MatchResult(val token: Token, val remainingStr: String)

    companion object {
        private const val number = "-?\\d+"
        private const val numberLookAhead = "(?![\\s\\.]\\d+)"
        private const val boolOperatorLookAhead = "((?= )|(?=\\z))"

        @JvmStatic
        fun main(objs: Array<String>) {
            println(("val value=45D\n" +
                    "var variable = 46.5f\n" +
                    "var float /= 46.2\n" +
                    "var hex += 0x23456A\n" +
                    "val str |= \"Something'sdf'`bmk`\"\n" +
                    "val byte = 23b").tokenize())
        }
    }
}