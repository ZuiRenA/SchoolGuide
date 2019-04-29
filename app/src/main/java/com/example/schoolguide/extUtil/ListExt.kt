package com.example.schoolguide.extUtil

fun <T> List<T>.copyToArrayList(): ArrayList<T> {
    val temp = arrayListOf<T>()
    this.forEach {
        temp.add(it)
    }
    return temp
}

fun <T, R> MutableList<T>.copyAction(baseMutableList: MutableList<R>, block: (R) -> T): MutableList<T> {
    baseMutableList.forEach {
        this.add(block(it))
    }
    return this
}
