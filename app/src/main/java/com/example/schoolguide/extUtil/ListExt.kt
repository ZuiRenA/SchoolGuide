package com.example.schoolguide.extUtil

fun <T> List<T>.copyToArrayList(): ArrayList<T> {
    val temp = arrayListOf<T>()
    this.forEach {
        temp.add(it)
    }
    return temp
}