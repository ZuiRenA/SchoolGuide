package com.example.schoolguide.extUtil

fun <T> List<T>.copyToArrayList(): ArrayList<T> {
    val temp = arrayListOf<T>()
    this.forEach {
        temp.add(it)
    }
    return temp
}

inline fun <reified T> List<T>.copyToArray(): Array<T?> {
    val tempArray: Array<T?> = arrayOfNulls(this.size)
    this.forEachIndexed { index, t ->
        tempArray[index] = t
    }
    return tempArray
}

fun <T, R> MutableList<T>.copyAction(baseMutableList: MutableList<R>, block: (R) -> T): MutableList<T> {
    baseMutableList.forEach {
        this.add(block(it))
    }
    return this
}

fun <T> List<T>.copyToString(block:(T) -> String): List<String> {
    val temp = mutableListOf<String>()
    this.forEach {
        temp.add(block(it))
    }
    return temp
}

fun <T> MutableList<T>.addChain(element: T): MutableList<T> {
    this.add(element)
    return this
}
