package com.example.schoolguide.extUtil

val String.year: Int
    get() = this.substring(0, 4).toInt()

val String.month: Int
    get() = substring(5, 7).toInt()

val String.day: Int
    get() = substring(8, 10).toInt()