package com.shanhou.psychtest.model

enum class TestCategory(val title: String, val description: String) {
    WORKPLACE("职场心理测试", "评估你的职场压力、人际关系和职业倦怠程度"),
    STUDENT("学生心理测试", "了解你的学习压力、社交适应和心理健康状况"),
    TEACHER("教师心理测试", "测试你的教学压力、职业满意度和心理状态"),
    RELATIONSHIP("情感关系测试", "评估你的亲密关系质量、依恋风格和情感健康"),
    SOCIAL_ANXIETY("社交焦虑测试", "了解你的社交焦虑程度、社交回避和自信水平")
}
