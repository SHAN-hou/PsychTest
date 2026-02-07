package com.shanhou.psychtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shanhou.psychtest.databinding.ActivityResultBinding
import com.shanhou.psychtest.model.TestCategory

class ResultActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_MAX_SCORE = "extra_max_score"
        const val EXTRA_LEVEL = "extra_level"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_SUGGESTION = "extra_suggestion"
        const val EXTRA_CATEGORY = "extra_category"
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getIntExtra(EXTRA_SCORE, 0)
        val maxScore = intent.getIntExtra(EXTRA_MAX_SCORE, 60)
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: ""
        val description = intent.getStringExtra(EXTRA_DESCRIPTION) ?: ""
        val suggestion = intent.getStringExtra(EXTRA_SUGGESTION) ?: ""
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY) ?: TestCategory.WORKPLACE.name
        val category = TestCategory.valueOf(categoryName)

        displayResult(score, maxScore, level, description, suggestion, category)
        setupButtons()
    }

    private fun displayResult(
        score: Int,
        maxScore: Int,
        level: String,
        description: String,
        suggestion: String,
        category: TestCategory
    ) {
        val percentage = if (maxScore > 0) score * 100 / maxScore else 0

        binding.tvScore.text = score.toString()
        binding.tvLevel.text = level
        binding.tvDescription.text = description
        binding.tvSuggestion.text = suggestion

        // Set circular progress
        binding.circularProgress.max = 100
        binding.circularProgress.setProgressCompat(percentage, true)

        // Set color based on percentage
        val colorRes = when {
            percentage <= 25 -> R.color.result_excellent
            percentage <= 45 -> R.color.result_good
            percentage <= 65 -> R.color.result_normal
            percentage <= 80 -> R.color.result_warning
            else -> R.color.result_danger
        }
        binding.circularProgress.setIndicatorColor(getColor(colorRes))
        binding.tvScore.setTextColor(getColor(colorRes))
    }

    private fun setupButtons() {
        binding.btnBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }

        binding.btnShare.setOnClickListener {
            val level = binding.tvLevel.text
            val score = binding.tvScore.text
            val shareText = "我刚完成了【心理测试】，我的心理状态是：$level（$score 分）。\n下载「心理测试」APP，了解自己的心理状态吧！"

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(shareIntent, "分享结果"))
        }
    }
}
