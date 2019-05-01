package com.example.schoolguide.model

data class GuideTime(
    var year: Int,
    var month: Int,
    var day: Int
)

data class Guide(
    var school: String?,
    var studentId: Long?,
    var studentName: String?,
    var studentInstitution: String?,
    var studentClassId: Long?,
    var GuideData: GuideData?
)

data class GuideChoice(
    var studentClassId: Long?,
    var studentDormitory: Long?
)

data class GuideData(
    var studentId: Long?
)

data class School(
    var school_id: Int,
    var school_name: String
)

data class College(
    var school_id: Int,
    var guide_college: String
)

data class SchoolGuideTime(
    val school_id: Int?,
    val guide_college: String,
    val guide_time_one: String,
    val guide_time_two: String
)

data class Dormitory(
    val school_id: Int?,
    val dormitory_id: Int,
    val dormitory_name: String,
    val dormitory_student_list: List<String>
)

data class SelectDor(
    val phone_number: Long,
    val id: Int,
    val index: Int
)