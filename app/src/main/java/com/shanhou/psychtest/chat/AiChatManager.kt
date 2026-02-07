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

    fun initConversation(category: TestCategory, level: String, score: Int, description: String) {
        val systemPrompt = buildSystemPrompt(category, level, score, description)
        conversationHistory.clear()
        conversationHistory.add(MessagePayload("system", systemPrompt))
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

        return """你是一位专业的$roleDesc。

用户刚刚完成了"${category.title}"，结果如下：
- 心理状态评级：$level
- 得分：${score}分
- 评估描述：$description

请基于以上测试结果，以温暖、专业、共情的态度与用户进行心理咨询对话。

注意事项：
1. 使用温暖亲切的语气，让用户感到被理解和支持
2. 根据用户的测试结果，有针对性地提供心理疏导
3. 适时使用开放性问题引导用户表达感受
4. 提供实用的心理调适建议和技巧
5. 如发现用户有严重心理问题，建议寻求专业线下帮助
6. 回复控制在200字以内，简洁有力
7. 使用中文回复"""
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
        val responses = listOf(
            "我理解你的感受。能告诉我更多关于这种情况的细节吗？这样我可以更好地帮助你。",
            "谢谢你愿意和我分享。每个人都会经历这样的时刻，你并不孤单。你觉得什么时候这种感觉最强烈？",
            "你说的这些让我很感触。面对压力时，试着做几次深呼吸，关注当下的感受。你平时有什么放松的方式吗？",
            "我能感受到你现在的困扰。试着把你的想法写下来，有时候把感受具象化可以帮助我们更清晰地认识自己。",
            "你愿意谈谈是什么让你有这种感觉的吗？有时候说出来本身就是一种释放。我在这里倾听你。",
            "每个人的成长路上都会遇到这样的挑战。重要的是，你已经在正视自己的感受了，这本身就是勇气的表现。",
            "我建议你可以尝试'54321'接地练习：看5样东西、摸4样东西、听3种声音、闻2种气味、尝1种味道。这能帮助你回到当下。",
            "你的感受是完全合理的。不要因为有这些情绪而责怪自己。我们可以一起探索适合你的应对方式。"
        )
        return responses.random()
    }
}
