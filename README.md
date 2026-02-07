# 心理测试 APP (PsychTest)

一款针对职场人士、学生和教师的心理健康测试 Android 应用。

## 功能特点

- **三大测试分类**：职场心理测试、学生心理测试、教师心理测试
- **专业题目**：每个分类包含 15 道精心设计的心理测试题
- **智能评估**：根据得分自动分析心理状态等级并给出专业建议
- **应用内更新**：通过 GitHub Releases 自动检测新版本，支持应用内下载安装
- **Material Design 3**：现代化 UI 设计，流畅的用户体验

## 技术栈

- **语言**：Kotlin
- **最低 SDK**：Android 7.0 (API 24)
- **目标 SDK**：Android 14 (API 34)
- **UI**：Material Design 3 + ViewBinding
- **网络**：OkHttp3
- **CI/CD**：GitHub Actions 自动构建 APK

## 项目结构

```
app/src/main/java/com/shanhou/psychtest/
├── SplashActivity.kt          # 启动页
├── MainActivity.kt             # 主页（分类选择）
├── TestActivity.kt             # 测试页面
├── ResultActivity.kt           # 结果展示页
├── model/
│   ├── TestCategory.kt         # 测试分类枚举
│   ├── Question.kt             # 问题数据模型
│   └── TestResult.kt           # 结果数据模型
├── data/
│   └── TestData.kt             # 测试题库和评估逻辑
└── update/
    └── UpdateManager.kt        # GitHub Releases 更新管理器
```

## 构建与运行

### 本地构建
```bash
./gradlew assembleDebug
```

### 发布新版本
1. 更新 `app/build.gradle` 中的 `versionCode` 和 `versionName`
2. 提交代码并打 tag：
```bash
git tag v1.0.1
git push origin v1.0.1
```
3. GitHub Actions 将自动构建并发布 Release

## 应用内更新配置

应用内更新功能基于 GitHub Releases API：
- 每次启动 APP 时自动检查最新版本
- 发现新版本后弹出更新提示对话框
- 支持查看更新日志
- 直接在应用内下载并安装 APK

### GitHub Actions 配置

需要在仓库 Settings > Secrets 中添加以下密钥：
- `SIGNING_KEY`：Base64 编码的签名密钥
- `ALIAS`：密钥别名
- `KEY_STORE_PASSWORD`：密钥库密码
- `KEY_PASSWORD`：密钥密码

## 许可证

MIT License
