package com.shanhou.psychtest.model

data class TestResult(
    val score: Int,
    val maxScore: Int,
    val level: String,
    val description: String,
    val suggestion: String,
    val category: TestCategory
) {
    val percentage: Int
        get() = if (maxScore > 0) (score * 100 / maxScore) else 0
}
