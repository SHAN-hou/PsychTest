package com.shanhou.psychtest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shanhou.psychtest.databinding.ActivityMainBinding
import com.shanhou.psychtest.model.TestCategory
import com.shanhou.psychtest.update.UpdateManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var updateManager: UpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        checkUpdate()
    }

    private fun setupUI() {
        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: Exception) {
            "1.0.0"
        }
        binding.tvVersion.text = getString(R.string.version_info, versionName)

        binding.btnWorkplace.setOnClickListener { startTest(TestCategory.WORKPLACE) }
        binding.cardWorkplace.setOnClickListener { startTest(TestCategory.WORKPLACE) }

        binding.btnStudent.setOnClickListener { startTest(TestCategory.STUDENT) }
        binding.cardStudent.setOnClickListener { startTest(TestCategory.STUDENT) }

        binding.btnTeacher.setOnClickListener { startTest(TestCategory.TEACHER) }
        binding.cardTeacher.setOnClickListener { startTest(TestCategory.TEACHER) }

        binding.btnRelationship.setOnClickListener { startTest(TestCategory.RELATIONSHIP) }
        binding.cardRelationship.setOnClickListener { startTest(TestCategory.RELATIONSHIP) }

        binding.btnSocialAnxiety.setOnClickListener { startTest(TestCategory.SOCIAL_ANXIETY) }
        binding.cardSocialAnxiety.setOnClickListener { startTest(TestCategory.SOCIAL_ANXIETY) }
    }

    private fun startTest(category: TestCategory) {
        val intent = Intent(this, TestActivity::class.java).apply {
            putExtra(TestActivity.EXTRA_CATEGORY, category.name)
        }
        startActivity(intent)
    }

    private fun checkUpdate() {
        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
        updateManager = UpdateManager(this)
        updateManager.checkForUpdate(versionName)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::updateManager.isInitialized) {
            updateManager.cancelDownload()
        }
    }
}
