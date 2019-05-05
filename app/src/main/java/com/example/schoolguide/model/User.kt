package com.example.schoolguide.model

data class Mine(
    var icon: Int,
    var name: String
)

data class PanP(
    val phone_number: Long,
    val password: String
)

data class User(
    val id: Int? = null,
    val name: String,
    val phone_number: Long,
    val password: String,
    val user_avatar: String? = null,
    val user_school: String? = null,
    val user_college: String? = null,
    val user_name: String? = null,
    val user_id_card: String? = null,
    val user_dormitory: String? = null,
    val user_letter: String? = null,
    val user_permission: String? = null
)

data class Letter(
    val phone_number: Long,
    val uri: String
)

data class Password(
    val phone_number: Long,
    val password: String
)