package com.example.schoolguide.extUtil

fun String?.isNotNullAndEmpty(): Boolean = (this != null && this.isNotEmpty())

fun CharSequence?.isNotNullAndEmpty(): Boolean = (this != null && this.isNotEmpty())