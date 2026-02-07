package com.shanhou.psychtest.chat

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.shanhou.psychtest.model.TestCategory
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

class AiChatManager {

    companion object {
        private const val API_URL = "https://api.deepseek.com/chat/completions"
        private const val API_KEY = "" // User needs to set their API key
        private const val MODEL = "deepseek-chat"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val conversationHistory = mutableListOf<MessagePayload>()

    data class ChatRequest(
        @SerializedName("model") val model: String,
        @SerializedName("messages") val messages: List<MessagePayload>,
        @SerializedName("temperature") val temperature: Double = 0.8,
        @SerializedName("max_tokens") val maxTokens: Int = 1024
    )

    data class MessagePayload(
        @SerializedName("role") val role: String,
        @SerializedName("content") val content: String
    )

    data class ChatResponse(
        @SerializedName("choices") val choices: List<Choice>?
    )

    data class Choice(
        @SerializedName("message") val message: MessagePayload?
    )

    fun initConversation(category: TestCategory, level: String, score: Int, description: String, welcomeMessage: String) {
        val systemPrompt = buildSystemPrompt(category, level, score, description)
        conversationHistory.clear()
        conversationHistory.add(MessagePayload("system", systemPrompt))
        conversationHistory.add(MessagePayload("assistant", welcomeMessage))
    }

    private fun buildSystemPrompt(
        category: TestCategory,
        level: String,
        score: Int,
        description: String
    ): String {
        val roleDesc = when (category) {
            TestCategory.WORKPLACE -> "职场心理咨询师，擅长处理职场压力、职业倦怠、人际关系和职业规划等问题"
            TestCategory.STUDENT -> "青少年心理咨询师，擅长处理学习压力、同伴关系、自我认知和成长困惑等问题"
            TestCategory.TEACHER -> "教育心理咨询师，擅长处理教师职业倦怠、教学压力、师生关系和职业发展等问题"
            TestCategory.RELATIONSHIP -> "情感关系咨询师，擅长处理亲密关系、依恋问题、情感创伤和沟通障碍等问题"
            TestCategory.SOCIAL_ANXIETY -> "社交焦虑专业咨询师，擅长处理社交恐惧、自信心建设、认知行为治疗和暴露练习等"
        }

        return """你是一位专业的$roleDesc。你正在和一位刚完成心理测试的用户进行一对一的心理咨询对话。

用户刚刚完成了"${category.title}"，结果如下：
- 心理状态评级：$level
- 得分：${score}分
- 评估描述：$description

【最重要的对话原则】
1. 你必须认真阅读并回应用户说的每一句话的具体内容，不要泛泛而谈
2. 当用户描述了一个具体的事情或感受时，你要针对那件事来回应，而不是给通用建议
3. 用自然的人类对话方式回复，就像一个真正关心对方的朋友在聊天
4. 先共情和回应用户说的内容，再适当追问或给建议
5. 不要每次都重复"我理解你的感受"这类套话，要具体说出你理解的是什么

【对话风格】
- 语气温暖自然，像朋友聊天，不要像机器人
- 回复控制在100-200字，简洁但有温度
- 适当使用口语化表达，避免过于书面化
- 可以分享实用的小建议，但要结合用户的具体情况
- 如发现用户有严重心理问题倾向，温和地建议寻求专业线下帮助
- 使用中文回复"""
    }

    suspend fun sendMessage(
        userMessage: String,
        apiKey: String = API_KEY,
        apiUrl: String = API_URL,
        model: String = MODEL
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                if (apiKey.isBlank()) {
                    return@withContext Result.success(getOfflineResponse(userMessage))
                }

                conversationHistory.add(MessagePayload("user", userMessage))

                val request = ChatRequest(
                    model = model,
                    messages = conversationHistory.toList()
                )

                val json = gson.toJson(request)
                val body = json.toRequestBody("application/json".toMediaType())

                val httpRequest = Request.Builder()
                    .url(apiUrl)
                    .header("Authorization", "Bearer $apiKey")
                    .header("Content-Type", "application/json")
                    .post(body)
                    .build()

                val response = client.newCall(httpRequest).execute()

                if (!response.isSuccessful) {
                    conversationHistory.removeLastOrNull()
                    return@withContext Result.success(getOfflineResponse(userMessage))
                }

                val responseBody = response.body?.string()
                val chatResponse = gson.fromJson(responseBody, ChatResponse::class.java)
                val assistantMessage = chatResponse.choices?.firstOrNull()?.message?.content
                    ?: "抱歉，我暂时无法回复。请稍后再试。"

                conversationHistory.add(MessagePayload("assistant", assistantMessage))

                Result.success(assistantMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.success(getOfflineResponse(userMessage))
            }
        }
    }

    private fun getOfflineResponse(userMessage: String): String {
        val msg = userMessage.lowercase()

        val response = when {
            msg.contains("压力") || msg.contains("累") || msg.contains("疲") || msg.contains("忙") ->
                "听起来你最近承受了不少压力。能具体说说是哪方面让你觉得最累吗？是工作量太大，还是心理上的消耗？有时候区分压力的来源，能帮我们找到更有针对性的应对方式。"

            msg.contains("焦虑") || msg.contains("担心") || msg.contains("害怕") || msg.contains("紧张") ->
                "你说到的这种焦虑感，我能理解。你最担心的那件事，如果真的发生了，最坏的结果是什么？很多时候我们焦虑的其实是对未知的恐惧，真正面对时反而没那么可怕。你觉得呢？"

            msg.contains("失眠") || msg.contains("睡不着") || msg.contains("睡眠") ->
                "睡眠问题确实很影响生活质量。你一般是躺下后脑子停不下来，还是半夜容易醒？可以试试睡前做个'身体扫描'——从脚趾到头顶，逐个部位感受放松，很多人反馈挺管用的。"

            msg.contains("关系") || msg.contains("吵架") || msg.contains("朋友") || msg.contains("同事") || msg.contains("相处") ->
                "人际关系的困扰确实让人心累。你提到的这个情况，你觉得对方是有意为之，还是可能只是沟通方式不同导致的误解？有时候换个角度看，可能会发现新的解决思路。"

            msg.contains("自卑") || msg.contains("不自信") || msg.contains("不够好") || msg.contains("差") ->
                "你能说出这些，本身就需要勇气。我想问你一个问题：如果你最好的朋友遇到同样的情况，你会怎么安慰ta？试着用同样的方式对待自己。你比你以为的要好得多。"

            msg.contains("开心") || msg.contains("高兴") || msg.contains("好了") || msg.contains("谢谢") || msg.contains("感谢") ->
                "很高兴听到你这么说！能感受到你的状态在变好。记住，遇到困难的时候随时可以来聊聊，照顾好自己的情绪和照顾身体一样重要。😊"

            msg.contains("工作") || msg.contains("上班") || msg.contains("老板") || msg.contains("领导") ->
                "工作上的事情确实容易影响心情。你说的这个情况，是最近才开始的，还是已经持续一段时间了？如果持续比较久，可能需要认真想想是环境的问题还是自己需要调整应对方式。"

            msg.contains("学习") || msg.contains("考试") || msg.contains("成绩") || msg.contains("作业") ->
                "学业压力我太理解了。你现在最困扰的是哪个部分？是感觉学不进去，还是学了但效果不好？不同的情况需要不同的调整策略，我们可以一起想想办法。"

            msg.contains("难过") || msg.contains("伤心") || msg.contains("哭") || msg.contains("难受") || msg.contains("痛苦") ->
                "听到你这么说，我真的很心疼。难过的时候不需要强撑，想哭就哭出来也没关系。你愿意告诉我，是什么事情让你这么难受吗？说出来可能会舒服一些。"

            msg.contains("不想") || msg.contains("没意思") || msg.contains("无聊") || msg.contains("没动力") ->
                "感觉什么都提不起劲的时候确实挺难熬的。这种状态持续多久了？有时候这是身心在发出需要休息的信号。不如先给自己放个小假，做一件哪怕很小但你曾经喜欢的事情试试？"

            msg.length < 10 ->
                "嗯，我在听。能再多说一些吗？你说得越具体，我越能理解你的感受，也能给出更贴合你情况的建议。"

            else ->
                "谢谢你告诉我这些。你提到的情况，让我感受到你现在确实不太容易。能再具体说说，在这件事里最让你困扰的是哪个部分吗？这样我们可以一起想想怎么应对。"
        }

        return response
    }
}
