package com.roadd.tmclang.tests

import com.roadd.tmclang.compiler.Token
import com.roadd.tmclang.compiler.Token.TokenType.*
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.*

class TokenTest: StringSpec() {

    init {
        "verify keyword matches" {
            val t = table(
                    headers("string", "type", "value"),
                    row("var", VARIABLE, "var"),
                    row("val", VALUE, "val"),
                    row("print", PRINT, "print"),
                    row("import", IMPORT, "import"),
                    row("as", AS, "as"),
                    row("class", CLASS, "class"),
                    row("abstract", ABSTRACT, "abstract"),
                    row("interface", INTERFACE, "interface"),
                    row("annotation", ANNOTATION, "annotation"),
                    row("data", DATA, "data"),
                    row("object", OBJECT, "object"),
                    row("def", DEF, "def")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "primitive types" {
            val t = table(
                    headers("string", "type", "value"),
                    row("12b", BYTE, "12b"),
                    row("-15B", BYTE, "-15B"),
                    row("14S", SHORT, "14S"),
                    row("-2356", INT, "-2356"),
                    row("0b001100", INT, "0b001100"),
                    row("0b0034100", INVALID, "0b0034100"),
                    row("0x345FAD", INT, "0x345FAD"),
                    row("0x3S5FAD", INVALID, "0x3S5FAD"),
                    row("3456L", LONG, "3456L"),
                    row("34f", FLOAT, "34f"),
                    row("-3.14", FLOAT, "-3.14"),
                    row("3D", DOUBLE, "3D"),
                    row("-4.16789d", DOUBLE, "-4.16789d"),
                    row("-34.45DF", INVALID, "-34.45DF"),
                    row("true", BOOL, "true"),
                    row("nil", NULL, "nil")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "valid identifiers" {
            val t = table(
                    headers("string", "type", "value"),
                    row("value", ID, "value"),
                    row("variable", ID, "variable"),
                    row("printable", ID, "printable"),
                    row("_te_st123ba", ID, "_te_st123ba"),
                    row("`abc_with_hicups`", ID, "`abc_with_hicups`"),
                    row("`val`", ID, "`val`"),
                    row("`null`", ID, "`null`"),
                    row("`false`", ID, "`false`")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "string literals" {
            val t = table(
                    headers("string", "type", "value"),
                    row("\"\"", STRING, "\"\""),
                    row("\'\'", STRING, "\'\'"),
                    row("\"asdasd\"", STRING, "\"asdasd\""),
                    row("'asdsd'", STRING, "'asdsd'"),
                    row("\"asdsdafdfffsdfsdf'sdf f'\"", STRING, "\"asdsdafdfffsdfsdf'sdf f'\""),
                    row("\"asdsdafdfffsd\\\"\\\"fs'df'sdf f'\"", STRING, "\"asdsdafdfffsd\\\"\\\"fs'df'sdf f'\"")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "assign operators" {
            val t = table(
                    headers("string", "type", "value"),
                    row("=", ASSIGN, "="),
                    row("+=", PLUS_ASSIGN, "+="),
                    row("-=", MINUS_ASSIGN, "-="),
                    row("*=", MUL_ASSIGN, "*="),
                    row("/=", DIV_ASSIGN, "/="),
                    row("%=", MOD_ASSIGN, "%="),
                    row("?=", NULL_ASSIGN, "?=")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "operators" {
            val t = table(
                    headers("string", "type", "value"),
                    row("++", INC, "++"),
                    row("--", DEC, "--"),
                    row("..", RANGE, ".."),
                    row("?.", SAFE_DOT, "?."),
                    row(".", DOT, "."),
                    row("+", PLUS, "+"),
                    row("-", MINUS, "-"),
                    row("*", MUL, "*"),
                    row("/", DIV, "/"),
                    row("||", OR, "||"),
                    row("or", OR, "or"),
                    row("&&", AND, "&&"),
                    row("and", AND, "and"),
                    row("!", NOT, "!"),
                    row("not", NOT, "not"),
                    row("&", BITWISE_AND, "&"),
                    row("|", BITWISE_OR, "|"),
                    row("^", BITWISE_XOR, "^"),
                    row("~", BITWISE_NOT, "~"),
                    row("<<", BITWISE_LSHIFT, "<<"),
                    row(">>", BITWISE_RSHIFT, ">>")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "separators" {
            val t = table(
                    headers("string", "type", "value"),
                    row("(", LPAREN, "("),
                    row("[", LBRACKET, "["),
                    row("{", LBRACE, "{"),
                    row(")", RPAREN, ")"),
                    row("]", RBRACKET, "]"),
                    row("}", RBRACE, "}"),
                    row(",", COMMA, ","),
                    row(":", COLON, ":"),
                    row(";", SEMI_COLON, ";")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "check whitespce trimming" {
            val t = table(
                    headers("string", "type", "value"),
                    row(" abc", ID, "abc")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "check eof" {
            val t = table(
                    headers("string", "type", "value"),
                    row("", EOF, "")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }

        "comment" {
            val t = table(
                    headers("string", "type", "value"),
                    row("// Dsadasdsdasd", COMMENT, "// Dsadasdsdasd"),
                    row("//", COMMENT, "//")
            )

            forAll(t) {string, type, value ->
                Token.TokenType.match(string).apply {
                    token.type shouldBe type
                    token.value shouldBe value
                    remainingStr shouldBe ""
                }

            }
        }
    }

}