package com.example.schoolguide.util

import android.util.Log
import com.example.schoolguide.AppContext
import com.example.schoolguide.R
import com.example.schoolguide.extUtil.*
import com.example.schoolguide.model.GuideTime
import java.util.*

object SearchTimeUtil {

    fun <T> searchTime(
        selectTime: String,
        searchTime: T,
        firstTime: (T) -> MutableList<GuideTime>,
        secondTime: (T) -> MutableList<GuideTime>
    ): List<Int> {
        val year = selectTime.year
        val month = selectTime.month
        val day = selectTime.day
        Log.d("time", "$year.$month.$day")
        val select = GuideTime(year, month, day)
        val first = firstTime(searchTime)
        val second = secondTime(searchTime)
        return action(select, first, second)
    }

    private fun action(select: GuideTime, start: MutableList<GuideTime>, end: MutableList<GuideTime>): List<Int> {
        val temp = mutableListOf<Int>()
        end.forEachIndexed { index, guideTime ->
            if (select.year <= guideTime.year) {
                if (select.month < guideTime.month) {
                    if (select.month == guideTime.month) {
                        if (select.day > guideTime.day)
                            return@forEachIndexed
                    }

                    if (select.year >= start[index].year) {
                        if (select.month >= start[index].month) {
                            if(select.day >= start[index].day) {
                                temp.add(index)
                            }
                        }
                    }
                }
            }
        }

        return temp
    }

    fun rightIndexAction(index: List<Int>, block:(Int) -> Unit) {
        index.isEmpty().yes {
            AppContext.toast(AppContext.getString(R.string.search_time_error))
            return
        }.otherwise {
            index.forEach {
                block(it)
            }
        }
    }
}