package com.example.recorder_myself

import android.os.Handler
import android.os.SystemClock

/**
 * Recorder_Myself
 * Created by authorName
 * Date: 2022-04-14
 * Time: 오후 4:51
 * */
class CountUpView : Runnable {
    private var startHTime = 0L
    private val handler = Handler()

    var timeInMillis = 0L
    var timeSwapBuff = 0L
    var updatedTime = 0L

    var text = "00:00"

    override fun run() {
        timeInMillis = SystemClock.uptimeMillis() - startHTime

        updatedTime = timeSwapBuff + timeInMillis

        var seconds = (updatedTime / 1000).toInt()
        val minutes = seconds / 60
        seconds %= 60

        text = "%02d:%02d".format(minutes,seconds)

        handler.postDelayed(this, 0)
    }

    fun start() {
        startHTime = SystemClock.uptimeMillis()
        handler.postDelayed(this, 0)
    }

    fun stop() {
        timeSwapBuff += timeInMillis
        handler.removeCallbacks(this)
    }

}