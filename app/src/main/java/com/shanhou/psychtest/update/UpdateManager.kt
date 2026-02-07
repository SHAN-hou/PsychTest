package com.shanhou.psychtest.update

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.shanhou.psychtest.R
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class UpdateManager(private val activity: Activity) {

    companion object {
        private const val GITHUB_API_URL =
            "https://api.github.com/repos/SHAN-hou/PsychTest/releases/latest"
        private const val TAG = "UpdateManager"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private var downloadJob: Job? = null

    data class GitHubRelease(
        @SerializedName("tag_name") val tagName: String,
        @SerializedName("name") val name: String,
        @SerializedName("body") val body: String?,
        @SerializedName("assets") val assets: List<GitHubAsset>
    )

    data class GitHubAsset(
        @SerializedName("name") val name: String,
        @SerializedName("browser_download_url") val downloadUrl: String,
        @SerializedName("size") val size: Long
    )

    fun checkForUpdate(currentVersion: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder()
                    .url(GITHUB_API_URL)
                    .header("Accept", "application/vnd.github.v3+json")
                    .build()

                val response = client.newCall(request).execute()
                if (!response.isSuccessful) return@launch

                val body = response.body?.string() ?: return@launch
                val release = gson.fromJson(body, GitHubRelease::class.java)

                val latestVersion = release.tagName.removePrefix("v")
                if (isNewerVersion(latestVersion, currentVersion)) {
                    val apkAsset = release.assets.firstOrNull { it.name.endsWith(".apk") }
                    if (apkAsset != null) {
                        withContext(Dispatchers.Main) {
                            showUpdateDialog(release, apkAsset)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun isNewerVersion(latest: String, current: String): Boolean {
        try {
            val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
            val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

            for (i in 0 until maxOf(latestParts.size, currentParts.size)) {
                val l = latestParts.getOrElse(i) { 0 }
                val c = currentParts.getOrElse(i) { 0 }
                if (l > c) return true
                if (l < c) return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun showUpdateDialog(release: GitHubRelease, asset: GitHubAsset) {
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_update, null)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvUpdateMessage)
        val tvChangeLog = dialogView.findViewById<TextView>(R.id.tvChangeLog)
        val progressBar = dialogView.findViewById<LinearProgressIndicator>(R.id.downloadProgress)
        val tvStatus = dialogView.findViewById<TextView>(R.id.tvDownloadStatus)

        tvMessage.text = activity.getString(R.string.update_message, release.tagName)

        if (!release.body.isNullOrBlank()) {
            tvChangeLog.visibility = View.VISIBLE
            tvChangeLog.text = release.body
        }

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setPositiveButton(R.string.update_download, null)
            .setNegativeButton(R.string.update_later, null)
            .setCancelable(false)
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = false
            progressBar.visibility = View.VISIBLE
            tvStatus.visibility = View.VISIBLE
            tvStatus.text = activity.getString(R.string.update_downloading)

            downloadAndInstall(asset.downloadUrl, asset.name, progressBar, tvStatus, dialog)
        }
    }

    private fun downloadAndInstall(
        url: String,
        fileName: String,
        progressBar: LinearProgressIndicator,
        tvStatus: TextView,
        dialog: AlertDialog
    ) {
        downloadJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        tvStatus.text = activity.getString(R.string.update_download_failed)
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = true
                    }
                    return@launch
                }

                val downloadDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    ?: activity.cacheDir
                val file = File(downloadDir, fileName)

                val totalBytes = response.body?.contentLength() ?: -1
                var downloadedBytes = 0L

                response.body?.byteStream()?.use { input ->
                    FileOutputStream(file).use { output ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead
                            if (totalBytes > 0) {
                                val progress = (downloadedBytes * 100 / totalBytes).toInt()
                                withContext(Dispatchers.Main) {
                                    progressBar.progress = progress
                                    tvStatus.text = "下载中... $progress%"
                                }
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    tvStatus.text = activity.getString(R.string.update_download_complete)
                    dialog.dismiss()
                    installApk(file)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    tvStatus.text = activity.getString(R.string.update_download_failed)
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = true
                }
            }
        }
    }

    private fun installApk(file: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val uri = FileProvider.getUriForFile(
                        activity,
                        "${activity.packageName}.fileprovider",
                        file
                    )
                    setDataAndType(uri, "application/vnd.android.package-archive")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    setDataAndType(
                        Uri.fromFile(file),
                        "application/vnd.android.package-archive"
                    )
                }
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "安装失败，请手动安装", Toast.LENGTH_LONG).show()
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
    }
}
