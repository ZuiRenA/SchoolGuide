package com.example.schoolguide.model

data class GuideTime(
    var time: String?,
    var year: Long?,
    var day: Long?,
    var hour: Long?,
    var minute: Long?
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