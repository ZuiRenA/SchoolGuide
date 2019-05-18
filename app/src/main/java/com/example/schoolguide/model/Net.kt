package com.example.schoolguide.model

import com.google.gson.annotations.JsonAdapter
import java.io.File

data class Id(
    val id: Int
)

data class isSuccess <T>(
    val isSuccess: Boolean,
    val respond: T,
    var errorReason: String? = ""
)

data class SchoolInfo(
    val school_id: Int,
    val school_name: String,
    val school_address: String,
    val school_introduce: String,
    val image_show_list: List<String>
)

//data class Upload(
//    val smfile: File,
//    val ssl: Boolean = true,
//    val format: String = "json"
//)
//
//data class UploadResult(
//    val code: String,
//    val data: Data?,
//    val msg: String?
//)
//data class Data(
//    val width: Int,
//    val height: Int,
//    val filename: String,
//    val storename: String,
//    val size: Int,
//    val hash: String,
//    val delete: String,
//    val url: String,
//    val path: String
//)