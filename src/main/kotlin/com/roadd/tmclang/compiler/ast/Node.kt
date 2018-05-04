package com.roadd.tmclang.compiler.ast

interface Node {
    fun children(): List<Node>
}