package com.example.schoolguide.extUtil

sealed class BooleanExt<out T>
object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun <T : Any?> Boolean.yes(block: () -> T) = when {
    this -> WithData(block())
    else -> Otherwise
}

inline fun <T : Any?> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> WithData(block())
}

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T = when (this) {
    is Otherwise -> block()
    is WithData -> this.data
}

///**
// * example how to use it
// */
//fun main(args: Array<String>) {
//    val result = false.yes { 1 }.otherwise { 2 }
//    val resultNo = true.no { 1 }.otherwise { 2 }
//    println("$result |$resultNo")
//    true.yes {
//        println(1)
//    }
//}

