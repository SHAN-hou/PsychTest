package com.shanhou.psychtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shanhou.psychtest.chat.AiChatManager
import com.shanhou.psychtest.chat.ChatAdapter
import com.shanhou.psychtest.databinding.ActivityChatBinding
import com.shanhou.psychtest.model.ChatMessage
import com.shanhou.psychtest.model.TestCategory
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_LEVEL = "extra_level"
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    private lateinit var binding: ActivityChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var aiChatManager: AiChatManager
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra(EXTRA_CATEGORY) ?: TestCategory.WORKPLACE.name
        val category = TestCategory.valueOf(categoryName)
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: ""
        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""

        setupToolbar(category)
        setupRecyclerView()
        setupInput()
        initAiChat(category, level, score, description)
    }

    private fun setupToolbar(category: TestCategory) {
        binding.toolbar.title = "AI 谈心"
        binding.toolbar.subtitle = category.title
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                stackFromEnd = true
            }
            adapter = chatAdapter
        }
    }

    private fun setupInput() {
        binding.btnSend.setOnClickListener { sendMessage() }

        binding.etMessage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage()
                true
            } else {
                false
            }
        }
    }

    private fun initAiChat(category: TestCategory, level: String, score: Int, description: String) {
        aiChatManager = AiChatManager()
        aiChatManager.initConversation(category, level, score, description)

        val welcomeMessage = buildWelcomeMessage(category, level)
        chatAdapter.addMessage(ChatMessage(welcomeMessage, isUser = false))
        scrollToBottom()
    }

    private fun buildWelcomeMessage(category: TestCategory, level: String): String {
        val greeting = when (category) {
            TestCategory.WORKPLACE -> "你好！我是你的职场心理咨询师。"
            TestCategory.STUDENT -> "你好！我是你的学习心理咨询师。"
            TestCategory.TEACHER -> "你好！我是你的教育心理咨询师。"
            TestCategory.RELATIONSHIP -> "你好！我是你的情感关系咨询师。"
            TestCategory.SOCIAL_ANXIETY -> "你好！我是你的社交心理咨询师。"
        }
        return "$greeting\n\n我看到你的测试结果显示「$level」。我在这里陪你聊聊，你可以告诉我最近让你困扰的事情，或者任何你想倾诉的内容。\n\n一切谈话内容都是安全的，请放心表达。😊"
    }

    private fun sendMessage() {
        val message = binding.etMessage.text?.toString()?.trim() ?: return
        if (message.isEmpty() || isLoading) return

        binding.etMessage.text?.clear()
        hideKeyboard()

        chatAdapter.addMessage(ChatMessage(message, isUser = true))
        scrollToBottom()

        setLoading(true)

        // Load API config from SharedPreferences
        val prefs = getSharedPreferences("ai_config", Context.MODE_PRIVATE)
        val apiKey = prefs.getString("api_key", "") ?: ""
        val apiUrl = prefs.getString("api_url", "https://api.deepseek.com/chat/completions") ?: ""
        val model = prefs.getString("model", "deepseek-chat") ?: ""

        lifecycleScope.launch {
            val result = aiChatManager.sendMessage(message, apiKey, apiUrl, model)
            result.onSuccess { response ->
                chatAdapter.addMessage(ChatMessage(response, isUser = false))
                scrollToBottom()
            }
            result.onFailure {
                chatAdapter.addMessage(ChatMessage(
                    "抱歉，我暂时无法回复。请检查网络连接后重试。",
                    isUser = false
                ))
                scrollToBottom()
            }
            setLoading(false)
        }
    }

    private fun setLoading(loading: Boolean) {
        isLoading = loading
        binding.progressLoading.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnSend.isEnabled = !loading
    }

    private fun scrollToBottom() {
        binding.rvMessages.post {
            val itemCount = chatAdapter.itemCount
            if (itemCount > 0) {
                binding.rvMessages.smoothScrollToPosition(itemCount - 1)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etMessage.windowToken, 0)
    }
}
