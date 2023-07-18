package com.terrencealuda.tcardio.services

import android.util.Log

interface ExerciseLogger {
    fun log(message: String)
}

class AndroidLogExerciseLogger : ExerciseLogger {
    override fun log(message: String) {
        Log.i("ExerciseSample", message)
    }
}