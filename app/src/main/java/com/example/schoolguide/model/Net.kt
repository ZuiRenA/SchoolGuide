package com.example.schoolguide.model

import com.google.gson.annotations.JsonAdapter

data class Id(
    val id: Int
)

data class isSuccess <T>(
    val isSuccess: Boolean,
    val respond: T,
    val errorReason: String? = ""
)

data class SchoolInfo(
    val school_id: Int,
    val school_name: String,
    val school_address: String,
    val school_introduce: String,
    val image_show_list: List<String>
)