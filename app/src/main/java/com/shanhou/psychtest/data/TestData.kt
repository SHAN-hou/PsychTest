package com.shanhou.psychtest.data

import com.shanhou.psychtest.model.*

object TestData {

    fun getQuestions(category: TestCategory): List<Question> {
        return when (category) {
            TestCategory.WORKPLACE -> getWorkplaceQuestions()
            TestCategory.STUDENT -> getStudentQuestions()
            TestCategory.TEACHER -> getTeacherQuestions()
        }
    }

    fun evaluateResult(category: TestCategory, score: Int, maxScore: Int): TestResult {
        val percentage = if (maxScore > 0) score * 100 / maxScore else 0
        return when (category) {
            TestCategory.WORKPLACE -> evaluateWorkplace(score, maxScore, percentage)
            TestCategory.STUDENT -> evaluateStudent(score, maxScore, percentage)
            TestCategory.TEACHER -> evaluateTeacher(score, maxScore, percentage)
        }
    }

    private fun getWorkplaceQuestions(): List<Question> {
        val options = listOf(
            Option("从不", 1),
            Option("偶尔", 2),
            Option("经常", 3),
            Option("总是", 4)
        )
        return listOf(
            Question(1, "你是否觉得工作压力让你喘不过气来？", options),
            Question(2, "你是否经常因为工作而失眠或睡眠质量差？", options),
            Question(3, "你是否对目前的工作感到厌倦或缺乏热情？", options),
            Question(4, "你是否觉得自己在工作中得不到应有的认可？", options),
            Question(5, "你是否经常与同事或上司发生冲突？", options),
            Question(6, "你是否感觉工作与生活难以平衡？", options),
            Question(7, "你是否担心自己会被裁员或降职？", options),
            Question(8, "你是否觉得自己的职业发展遇到了瓶颈？", options),
            Question(9, "你是否经常在下班后仍然想着工作的事情？", options),
            Question(10, "你是否觉得工作让你变得焦虑或易怒？", options),
            Question(11, "你是否对上班产生抵触情绪？", options),
            Question(12, "你是否觉得工作中的人际关系让你感到疲惫？", options),
            Question(13, "你是否经常感到精力不足，难以集中注意力？", options),
            Question(14, "你是否觉得自己的工作能力在下降？", options),
            Question(15, "你是否经常因为工作忽略了家人和朋友？", options)
        )
    }

    private fun getStudentQuestions(): List<Question> {
        val options = listOf(
            Option("从不", 1),
            Option("偶尔", 2),
            Option("经常", 3),
            Option("总是", 4)
        )
        return listOf(
            Question(1, "你是否觉得学习压力很大，难以承受？", options),
            Question(2, "你是否经常因为考试而感到焦虑不安？", options),
            Question(3, "你是否对学习失去了兴趣和动力？", options),
            Question(4, "你是否觉得自己与同学的关系不太融洽？", options),
            Question(5, "你是否经常感到孤独或被孤立？", options),
            Question(6, "你是否对自己的未来感到迷茫或担忧？", options),
            Question(7, "你是否经常因为学业问题与父母产生矛盾？", options),
            Question(8, "你是否觉得自己的记忆力和注意力在下降？", options),
            Question(9, "你是否经常感到疲倦，即使休息后也无法恢复？", options),
            Question(10, "你是否有过不想上学的念头？", options),
            Question(11, "你是否觉得自己不如其他同学优秀？", options),
            Question(12, "你是否经常因为一些小事就情绪低落？", options),
            Question(13, "你是否觉得老师对你不够关注或不公平？", options),
            Question(14, "你是否经常使用手机或游戏来逃避现实？", options),
            Question(15, "你是否对自己的外貌或社交能力感到不自信？", options)
        )
    }

    private fun getTeacherQuestions(): List<Question> {
        val options = listOf(
            Option("从不", 1),
            Option("偶尔", 2),
            Option("经常", 3),
            Option("总是", 4)
        )
        return listOf(
            Question(1, "你是否觉得教学工作让你身心疲惫？", options),
            Question(2, "你是否对教学工作失去了最初的热情？", options),
            Question(3, "你是否觉得学生越来越难管教？", options),
            Question(4, "你是否经常因为教学评估而感到焦虑？", options),
            Question(5, "你是否觉得教师的付出与回报不成正比？", options),
            Question(6, "你是否经常因为家长的要求而感到压力？", options),
            Question(7, "你是否觉得行政工作占据了太多教学时间？", options),
            Question(8, "你是否对自己的教学效果感到不满意？", options),
            Question(9, "你是否经常因为工作而忽略了自己的健康？", options),
            Question(10, "你是否觉得自己在职业发展上缺乏支持？", options),
            Question(11, "你是否对教育改革的频繁变动感到困惑？", options),
            Question(12, "你是否觉得与同事之间存在竞争压力？", options),
            Question(13, "你是否经常在课余时间仍然要处理工作事务？", options),
            Question(14, "你是否觉得自己的专业知识需要更新但无暇学习？", options),
            Question(15, "你是否想过转行或离开教育行业？", options)
        )
    }

    private fun evaluateWorkplace(score: Int, maxScore: Int, percentage: Int): TestResult {
        return when {
            percentage <= 25 -> TestResult(
                score, maxScore,
                "心理状态优秀",
                "你的职场心理状态非常健康！你能够很好地应对工作压力，保持积极乐观的态度。你在人际关系处理、情绪管理方面表现出色。",
                "继续保持良好的心态，适当关注同事的心理健康，帮助他们一起成长。可以尝试分享你的积极心态和应对策略。",
                TestCategory.WORKPLACE
            )
            percentage <= 45 -> TestResult(
                score, maxScore,
                "心理状态良好",
                "你的职场心理状态总体良好，偶尔会感受到一些压力，但基本能够自我调节。你具备较好的抗压能力。",
                "建议定期进行自我放松，培养工作之外的兴趣爱好，保持规律的运动习惯，增强心理韧性。",
                TestCategory.WORKPLACE
            )
            percentage <= 65 -> TestResult(
                score, maxScore,
                "心理状态一般",
                "你正在经历一定程度的职场压力，可能会出现焦虑、疲倦等症状。需要引起注意并采取措施。",
                "建议学会合理分配工作任务，避免过度加班。可以尝试冥想、深呼吸等减压方法。必要时与信任的朋友或家人倾诉。",
                TestCategory.WORKPLACE
            )
            percentage <= 80 -> TestResult(
                score, maxScore,
                "心理状态需要关注",
                "你的职场压力较大，已经影响到了你的情绪和生活质量。可能出现失眠、易怒、注意力不集中等症状。",
                "强烈建议寻求专业心理咨询帮助。同时要学会说'不'，设定工作边界，保证充足的休息时间。考虑与上级沟通工作负荷问题。",
                TestCategory.WORKPLACE
            )
            else -> TestResult(
                score, maxScore,
                "心理状态需要立即干预",
                "你正在经历严重的职场压力和心理困扰。这种状态如果持续下去，可能会对身心健康造成严重影响。",
                "请尽快寻求专业心理咨询师或心理医生的帮助。如有需要，可以拨打心理援助热线：400-161-9995。你的健康比任何工作都重要。",
                TestCategory.WORKPLACE
            )
        }
    }

    private fun evaluateStudent(score: Int, maxScore: Int, percentage: Int): TestResult {
        return when {
            percentage <= 25 -> TestResult(
                score, maxScore,
                "心理状态优秀",
                "你的心理状态非常健康！你能够积极面对学习和生活中的挑战，拥有良好的社交能力和自信心。",
                "继续保持阳光积极的心态！可以帮助身边的同学，一起成长进步。记得保持良好的作息和运动习惯。",
                TestCategory.STUDENT
            )
            percentage <= 45 -> TestResult(
                score, maxScore,
                "心理状态良好",
                "你的心理状态总体良好，偶尔会感到学习压力或社交困扰，但能较好地自我调节。",
                "建议培养多样化的兴趣爱好，合理规划学习时间，避免过度焦虑。与父母和朋友保持良好沟通。",
                TestCategory.STUDENT
            )
            percentage <= 65 -> TestResult(
                score, maxScore,
                "心理状态一般",
                "你正在经历一定程度的心理压力，可能在学习、社交或自我认知方面遇到了一些困难。",
                "建议主动与老师或学校心理咨询师沟通。学会制定合理的学习目标，不要过度与他人比较。适当运动和社交有助于缓解压力。",
                TestCategory.STUDENT
            )
            percentage <= 80 -> TestResult(
                score, maxScore,
                "心理状态需要关注",
                "你的心理压力较大，学习和生活可能受到了明显影响。你需要及时寻求帮助和支持。",
                "强烈建议向学校心理咨询中心寻求帮助。同时与父母坦诚沟通自己的感受。记住，成绩不是衡量你价值的唯一标准。",
                TestCategory.STUDENT
            )
            else -> TestResult(
                score, maxScore,
                "心理状态需要立即关注",
                "你正在经历较为严重的心理困扰，这需要引起重视。请记住，寻求帮助是勇敢的表现。",
                "请尽快告诉父母或信任的老师你的感受，并寻求专业心理帮助。青少年心理援助热线：12355。你不是一个人，有很多人愿意帮助你。",
                TestCategory.STUDENT
            )
        }
    }

    private fun evaluateTeacher(score: Int, maxScore: Int, percentage: Int): TestResult {
        return when {
            percentage <= 25 -> TestResult(
                score, maxScore,
                "心理状态优秀",
                "你对教学工作充满热情，能够很好地平衡工作与生活。你的积极心态对学生有着正面的影响。",
                "继续保持对教育事业的热爱！你可以将自己的积极经验分享给同事，共同营造良好的教学氛围。",
                TestCategory.TEACHER
            )
            percentage <= 45 -> TestResult(
                score, maxScore,
                "心理状态良好",
                "你的职业心理状态总体良好，虽然偶尔会感到疲倦或压力，但基本能保持教学热情和专业态度。",
                "建议合理安排工作节奏，给自己留出充电的时间。参加教师交流活动，分享经验和困惑。保持规律运动。",
                TestCategory.TEACHER
            )
            percentage <= 65 -> TestResult(
                score, maxScore,
                "心理状态一般",
                "你正在经历职业倦怠的初期症状。教学压力、行政工作和人际关系可能让你感到力不从心。",
                "建议参加教师心理健康培训，学习压力管理技巧。尝试创新教学方法来重燃教学热情。与同事互相支持，分担压力。",
                TestCategory.TEACHER
            )
            percentage <= 80 -> TestResult(
                score, maxScore,
                "心理状态需要关注",
                "你正在经历较为严重的职业倦怠。教学工作可能已经对你的身心健康产生了明显影响。",
                "强烈建议寻求学校心理健康支持或专业心理咨询。适当减轻工作量，学会设定工作边界。必要时可以申请调休或休假。",
                TestCategory.TEACHER
            )
            else -> TestResult(
                score, maxScore,
                "心理状态需要立即干预",
                "你正在经历严重的职业倦怠和心理困扰。这种状态需要立即引起重视和采取行动。",
                "请尽快寻求专业心理帮助。心理援助热线：400-161-9995。你的身心健康是最重要的，不要独自承受这些压力。学校和社会都有资源可以帮助你。",
                TestCategory.TEACHER
            )
        }
    }
}
