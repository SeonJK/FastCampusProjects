package com.example.recorder_myself

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * Recorder_Myself
 * Created by authorName
 * Date: 2022-04-14
 * Time: 오후 4:51
 * */
class CountUpView(
    context: Context,
    attrs: AttributeSet? = null
    ) : AppCompatTextView(context, attrs) {

    companion object {
        private const val TIME_INTERVAL = 1000L
    }

    private var startHTime = 0L
//    var timeSwapBuff = 0L

    private val countUpAction: Runnable = object : Runnable {
        override fun run() {
            val timeInMillis = SystemClock.elapsedRealtime() - startHTime
//            updatedTime = timeSwapBuff + timeInMillis

            val countTimeSeconds = (timeInMillis / 1000L).toInt()
            updateCountTime(countTimeSeconds)

            handler.postDelayed(this, TIME_INTERVAL)
        }
    }

    fun startCountUp() {
        startHTime = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp() {
//        timeSwapBuff += timeInMillis
        handler?.removeCallbacks(countUpAction)
    }

    @SuppressLint("SetTextI18n")
    private fun updateCountTime(countTimeSeconds: Int) {
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }
}