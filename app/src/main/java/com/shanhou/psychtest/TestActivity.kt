package com.shanhou.psychtest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.shanhou.psychtest.data.TestData
import com.shanhou.psychtest.databinding.ActivityTestBinding
import com.shanhou.psychtest.model.Option
import com.shanhou.psychtest.model.Question
import com.shanhou.psychtest.model.TestCategory

class TestActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }

    private lateinit var binding: ActivityTestBinding
    private lateinit var category: TestCategory
    private lateinit var questions: List<Question>

    private var currentIndex = 0
    private val answers = mutableMapOf<Int, Int>() // questionId -> selectedOptionIndex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra(EXTRA_CATEGORY) ?: TestCategory.WORKPLACE.name
        category = TestCategory.valueOf(categoryName)
        questions = TestData.getQuestions(category)

        setupToolbar()
        setupButtons()
        showQuestion(0)
    }

    private fun setupToolbar() {
        binding.toolbar.title = category.title
        binding.toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupButtons() {
        binding.btnPrev.setOnClickListener {
            if (currentIndex > 0) {
                currentIndex--
                showQuestion(currentIndex)
            }
        }

        binding.btnNext.setOnClickListener {
            if (!answers.containsKey(questions[currentIndex].id)) {
                Toast.makeText(this, "请先选择一个选项", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (currentIndex < questions.size - 1) {
                currentIndex++
                showQuestion(currentIndex)
            } else {
                submitTest()
            }
        }
    }

    private fun showQuestion(index: Int) {
        val question = questions[index]

        // Update progress
        binding.tvProgress.text = getString(R.string.test_progress, index + 1, questions.size)
        binding.progressBar.max = questions.size
        binding.progressBar.progress = index + 1

        // Update question text
        binding.tvQuestion.text = "${index + 1}. ${question.text}"

        // Update buttons
        binding.btnPrev.visibility = if (index > 0) View.VISIBLE else View.INVISIBLE
        binding.btnNext.text = if (index == questions.size - 1) {
            getString(R.string.btn_submit)
        } else {
            getString(R.string.btn_next)
        }

        // Build options
        binding.optionsContainer.removeAllViews()
        val selectedOptionIndex = answers[question.id]

        question.options.forEachIndexed { optionIndex, option ->
            val button = createOptionButton(option, optionIndex, selectedOptionIndex)
            binding.optionsContainer.addView(button)
        }
    }

    private fun createOptionButton(
        option: Option,
        optionIndex: Int,
        selectedIndex: Int?
    ): MaterialButton {
        val isSelected = selectedIndex == optionIndex

        val button = MaterialButton(
            this,
            null,
            if (isSelected) {
                com.google.android.material.R.attr.materialButtonOutlinedStyle
            } else {
                com.google.android.material.R.attr.materialButtonOutlinedStyle
            }
        ).apply {
            text = option.text
            isAllCaps = false
            textSize = 15f
            cornerRadius = dpToPx(12)
            minimumHeight = dpToPx(52)

            val params = android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(8)
            }
            layoutParams = params

            if (isSelected) {
                setBackgroundColor(getColor(R.color.primary_light))
                setTextColor(getColor(R.color.primary_dark))
                strokeWidth = dpToPx(2)
                setStrokeColorResource(R.color.primary)
            } else {
                setBackgroundColor(getColor(R.color.white))
                setTextColor(getColor(R.color.on_surface))
                strokeWidth = dpToPx(1)
                setStrokeColorResource(R.color.divider)
            }
        }

        button.setOnClickListener {
            answers[questions[currentIndex].id] = optionIndex
            showQuestion(currentIndex)
        }

        return button
    }

    private fun submitTest() {
        var totalScore = 0
        var maxScore = 0

        for (question in questions) {
            val selectedIndex = answers[question.id]
            if (selectedIndex != null) {
                totalScore += question.options[selectedIndex].score
            }
            maxScore += question.options.maxOf { it.score }
        }

        val result = TestData.evaluateResult(category, totalScore, maxScore)

        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_SCORE, result.score)
            putExtra(ResultActivity.EXTRA_MAX_SCORE, result.maxScore)
            putExtra(ResultActivity.EXTRA_LEVEL, result.level)
            putExtra(ResultActivity.EXTRA_DESCRIPTION, result.description)
            putExtra(ResultActivity.EXTRA_SUGGESTION, result.suggestion)
            putExtra(ResultActivity.EXTRA_CATEGORY, category.name)
        }
        startActivity(intent)
        finish()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}
