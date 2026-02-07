package com.shanhou.psychtest.model

data class Question(
    val id: Int,
    val text: String,
    val options: List<Option>
)

data class Option(
    val text: String,
    val score: Int
)
