package com.example.schoolguide.model

data class GuideTime(
    var year: Int,
    var month: Int,
    var day: Int
)


data class School(
    var school_id: Int,
    var school_name: String
)


data class SchoolGuideTime(
    val school_id: Int?,
    val guide_college: String,
    var guide_time_one: String,
    var guide_time_two: String
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