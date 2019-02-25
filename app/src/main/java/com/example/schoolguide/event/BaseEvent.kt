package com.example.schoolguide.event

open class BaseEvent(val action: Int)

class BaseActionEvent(action: Int) : BaseEvent(action) {
    var message: String? = null

    companion object {
        val SHOW_LOADING_DIALOG = 1

        val DISMISS_LOADING_DIALOG = 2

        val SHOW_TOAST = 3

        val FINISH = 4

        val FINISH_WITH_RESULT_OK = 5
    }
}