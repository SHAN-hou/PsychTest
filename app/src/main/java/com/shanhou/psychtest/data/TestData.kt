package com.shanhou.psychtest.data

import com.shanhou.psychtest.model.*

object TestData {

    fun getQuestions(category: TestCategory): List<Question> {
        return when (category) {
            TestCategory.WORKPLACE -> getWorkplaceQuestions()
            TestCategory.STUDENT -> getStudentQuestions()
            TestCategory.TEACHER -> getTeacherQuestions()
            TestCategory.RELATIONSHIP -> getRelationshipQuestions()
            TestCategory.SOCIAL_ANXIETY -> getSocialAnxietyQuestions()
        }
    }

    fun evaluateResult(category: TestCategory, score: Int, maxScore: Int): TestResult {
        val percentage = if (maxScore > 0) score * 100 / maxScore else 0
        return when (category) {
            TestCategory.WORKPLACE -> evaluateWorkplace(score, maxScore, percentage)
            TestCategory.STUDENT -> evaluateStudent(score, maxScore, percentage)
            TestCategory.TEACHER -> evaluateTeacher(score, maxScore, percentage)
            TestCategory.RELATIONSHIP -> evaluateRelationship(score, maxScore, percentage)
            TestCategory.SOCIAL_ANXIETY -> evaluateSocialAnxiety(score, maxScore, percentage)
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
            Question(15, "你是否经常因为工作忽略了家人和朋友？", options),
            Question(16, "你是否觉得自己的薪资与付出不匹配？", options),
            Question(17, "你是否在工作中缺乏成就感？", options),
            Question(18, "你是否觉得公司的管理制度不合理？", options),
            Question(19, "你是否经常头痛、胃痛等身体不适与工作压力有关？", options),
            Question(20, "你是否觉得自己需要不断证明自己的价值？", options),
            Question(21, "你是否害怕在会议中发言或表达不同意见？", options),
            Question(22, "你是否觉得团队协作效率低下让你沮丧？", options),
            Question(23, "你是否经常拖延工作任务？", options),
            Question(24, "你是否觉得工作环境（噪音、空间等）影响了你的状态？", options),
            Question(25, "你是否对职业前景感到悲观？", options)
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
            Question(15, "你是否对自己的外貌或社交能力感到不自信？", options),
            Question(16, "你是否觉得课业负担过重，没有自由时间？", options),
            Question(17, "你是否经常与同学发生争执或矛盾？", options),
            Question(18, "你是否对自己的成绩感到不满意？", options),
            Question(19, "你是否觉得父母对你的期望过高？", options),
            Question(20, "你是否经常感到紧张或心跳加速？", options),
            Question(21, "你是否觉得学校的规章制度让你感到压抑？", options),
            Question(22, "你是否有时候会无缘无故地想哭？", options),
            Question(23, "你是否觉得自己没有可以倾诉的朋友？", options),
            Question(24, "你是否经常感到对什么事情都提不起兴趣？", options),
            Question(25, "你是否觉得自己的努力得不到老师或家长的肯定？", options)
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
            Question(15, "你是否想过转行或离开教育行业？", options),
            Question(16, "你是否觉得学校领导对教师不够尊重？", options),
            Question(17, "你是否经常因为学生成绩而自责？", options),
            Question(18, "你是否觉得自己与学生之间的代沟越来越大？", options),
            Question(19, "你是否经常感到嗓子不适或其他职业病困扰？", options),
            Question(20, "你是否觉得教师培训流于形式，缺乏实用性？", options),
            Question(21, "你是否因为个别问题学生而影响整体教学心情？", options),
            Question(22, "你是否觉得社会对教师职业的理解和尊重不够？", options),
            Question(23, "你是否经常需要处理学生之间的纠纷？", options),
            Question(24, "你是否觉得备课和批改作业占用了太多休息时间？", options),
            Question(25, "你是否对自己选择教师这个职业感到后悔？", options)
        )
    }

    private fun getRelationshipQuestions(): List<Question> {
        val options = listOf(
            Option("从不", 1),
            Option("偶尔", 2),
            Option("经常", 3),
            Option("总是", 4)
        )
        return listOf(
            Question(1, "你是否经常担心伴侣会离开你？", options),
            Question(2, "你是否觉得自己在感情中付出比对方多？", options),
            Question(3, "你是否经常因为小事与伴侣发生争吵？", options),
            Question(4, "你是否觉得伴侣不够理解你的感受？", options),
            Question(5, "你是否在感情中感到缺乏安全感？", options),
            Question(6, "你是否经常翻看伴侣的手机或社交媒体？", options),
            Question(7, "你是否觉得两人之间缺乏有效的沟通？", options),
            Question(8, "你是否因为感情问题而影响了工作或学习？", options),
            Question(9, "你是否觉得自己在感情中失去了自我？", options),
            Question(10, "你是否害怕表达自己的真实想法和需求？", options),
            Question(11, "你是否经常感到嫉妒或猜疑？", options),
            Question(12, "你是否觉得两人的价值观差异越来越大？", options),
            Question(13, "你是否经常感到在感情中被控制或限制？", options),
            Question(14, "你是否觉得对方不够重视你们的关系？", options),
            Question(15, "你是否经常回忆过去的感情创伤？", options),
            Question(16, "你是否害怕亲密关系或刻意保持距离？", options),
            Question(17, "你是否觉得自己不值得被爱？", options),
            Question(18, "你是否经常因为感情问题而情绪波动很大？", options),
            Question(19, "你是否在争吵后会冷战很长时间？", options),
            Question(20, "你是否觉得单身比恋爱更让你感到轻松？", options),
            Question(21, "你是否经常比较自己的感情和别人的感情？", options),
            Question(22, "你是否觉得自己很难信任他人？", options),
            Question(23, "你是否经常为了维护关系而委屈自己？", options),
            Question(24, "你是否觉得两人在一起时经常无话可说？", options),
            Question(25, "你是否对未来的感情生活感到悲观？", options)
        )
    }

    private fun getSocialAnxietyQuestions(): List<Question> {
        val options = listOf(
            Option("从不", 1),
            Option("偶尔", 2),
            Option("经常", 3),
            Option("总是", 4)
        )
        return listOf(
            Question(1, "你是否在社交场合中感到紧张或不自在？", options),
            Question(2, "你是否害怕在众人面前发言或表演？", options),
            Question(3, "你是否经常担心别人对你的看法？", options),
            Question(4, "你是否回避参加聚会或社交活动？", options),
            Question(5, "你是否在与陌生人交谈时感到极度紧张？", options),
            Question(6, "你是否害怕被别人评价或批评？", options),
            Question(7, "你是否在社交场合中出现脸红、出汗、心跳加速等症状？", options),
            Question(8, "你是否经常在社交后反复回想自己说过的话？", options),
            Question(9, "你是否觉得自己的社交能力不如他人？", options),
            Question(10, "你是否害怕打电话或接电话？", options),
            Question(11, "你是否在餐厅、商店等公共场所感到不安？", options),
            Question(12, "你是否害怕成为众人关注的焦点？", options),
            Question(13, "你是否经常为了避免社交而找借口不参加活动？", options),
            Question(14, "你是否觉得与人交往是一件很累的事情？", options),
            Question(15, "你是否害怕在别人面前吃东西或喝水？", options),
            Question(16, "你是否觉得自己说话时会被别人嘲笑？", options),
            Question(17, "你是否宁愿发消息也不愿意面对面交流？", options),
            Question(18, "你是否在进入有人的房间时感到焦虑？", options),
            Question(19, "你是否害怕与权威人士（上司、老师等）交流？", options),
            Question(20, "你是否经常因为社交焦虑而影响了日常生活？", options),
            Question(21, "你是否觉得自己在社交中表现很笨拙？", options),
            Question(22, "你是否害怕在他人面前表现出紧张的样子？", options),
            Question(23, "你是否经常独自一人待着更感到舒适？", options),
            Question(24, "你是否在社交场合后感到精疲力竭？", options),
            Question(25, "你是否因为社交恐惧而错过了重要的机会？", options)
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

    private fun evaluateRelationship(score: Int, maxScore: Int, percentage: Int): TestResult {
        return when {
            percentage <= 25 -> TestResult(
                score, maxScore,
                "情感状态健康",
                "你的情感关系非常健康！你能够在亲密关系中保持独立自我，同时与伴侣建立深厚的情感连接。你具备良好的沟通能力和情感管理能力。",
                "继续保持健康的相处模式。可以和伴侣一起尝试新的活动和体验，不断为感情注入新鲜感。也可以帮助身边的朋友改善他们的感情关系。",
                TestCategory.RELATIONSHIP
            )
            percentage <= 45 -> TestResult(
                score, maxScore,
                "情感状态良好",
                "你的情感关系总体良好。虽然偶尔会遇到一些小摩擦，但你们能够通过沟通来解决问题。你对感情有较为成熟的认知。",
                "建议定期与伴侣进行深入的心灵对话，表达彼此的需求和感受。学习'非暴力沟通'等沟通技巧，可以让关系更加融洽。",
                TestCategory.RELATIONSHIP
            )
            percentage <= 65 -> TestResult(
                score, maxScore,
                "情感状态需注意",
                "你在情感关系中正在经历一些困扰。可能存在沟通不畅、信任缺失或安全感不足等问题。这些问题如果不及时处理，可能会加剧。",
                "建议主动与伴侣坦诚沟通你的感受和需求。如果沟通困难，可以考虑寻求情感咨询师的帮助。记住，健康的关系需要双方共同经营。",
                TestCategory.RELATIONSHIP
            )
            percentage <= 80 -> TestResult(
                score, maxScore,
                "情感状态需要关注",
                "你的情感关系中存在较为明显的问题，已经影响到了你的情绪和日常生活。你可能正在经历焦虑、不安全感或情感消耗。",
                "强烈建议寻求专业情感咨询师或心理咨询师的帮助。同时要关注自己的情绪健康，不要在不健康的关系中失去自我。你的感受是重要的。",
                TestCategory.RELATIONSHIP
            )
            else -> TestResult(
                score, maxScore,
                "情感状态需要立即关注",
                "你正在经历严重的情感困扰。这种状态可能涉及不健康的依恋模式或情感创伤，需要专业的帮助和支持。",
                "请尽快寻求专业心理帮助。如果你正在经历情感暴力或控制，请拨打妇女维权热线：12338。你值得被尊重和被爱，不要独自承受痛苦。",
                TestCategory.RELATIONSHIP
            )
        }
    }

    private fun evaluateSocialAnxiety(score: Int, maxScore: Int, percentage: Int): TestResult {
        return when {
            percentage <= 25 -> TestResult(
                score, maxScore,
                "社交状态优秀",
                "你的社交能力非常出色！你在各种社交场合都能自如应对，拥有良好的人际关系和自信心。你享受与他人的互动。",
                "继续保持开放自信的社交态度。你可以帮助身边有社交困难的朋友，带他们融入社交圈子。你的正能量对周围人有积极影响。",
                TestCategory.SOCIAL_ANXIETY
            )
            percentage <= 45 -> TestResult(
                score, maxScore,
                "社交状态良好",
                "你的社交能力总体良好。虽然在某些特定场合可能会感到轻微紧张，但不影响你的正常社交活动。这是正常的。",
                "建议逐步挑战让你略感不适的社交情境，每次一小步。练习深呼吸等放松技巧，在社交前做好心理准备。",
                TestCategory.SOCIAL_ANXIETY
            )
            percentage <= 65 -> TestResult(
                score, maxScore,
                "存在社交焦虑",
                "你正在经历一定程度的社交焦虑。在部分社交场合你会感到明显的不适、紧张或回避。这已经对你的社交生活产生了影响。",
                "建议学习认知行为疗法的基本技巧，识别和挑战不合理的社交恐惧想法。可以从小的社交目标开始，逐步暴露练习。适当的运动也有助于缓解焦虑。",
                TestCategory.SOCIAL_ANXIETY
            )
            percentage <= 80 -> TestResult(
                score, maxScore,
                "社交焦虑较严重",
                "你正在经历较为严重的社交焦虑。社交场合让你感到极度不适，你可能已经开始回避许多社交活动。这已经明显影响了你的工作和生活质量。",
                "强烈建议寻求专业心理咨询师的帮助，进行系统的认知行为治疗。必要时可以考虑药物辅助治疗。记住，社交焦虑是可以治疗的，你不必独自面对。",
                TestCategory.SOCIAL_ANXIETY
            )
            else -> TestResult(
                score, maxScore,
                "社交焦虑需要立即干预",
                "你正在经历严重的社交焦虑，这已经严重影响了你的日常生活、工作和人际关系。你可能因为社交恐惧而错过了许多重要的机会。",
                "请尽快寻求专业心理医生或精神科医生的帮助。心理援助热线：400-161-9995。社交焦虑障碍是可以通过专业治疗有效改善的，请不要放弃希望。",
                TestCategory.SOCIAL_ANXIETY
            )
        }
    }
}
