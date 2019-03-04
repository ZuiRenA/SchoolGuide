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