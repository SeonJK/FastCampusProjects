package com.example.pomodorotimer_myself.Util

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import androidx.core.view.WindowCompat

/**
 * PomodoroTimer_Myself
 * Created by authorName
 * Date: 2022-04-11
 * Time: 오후 4:27
 * */
class StatusbarTransparent {
    // 정적 메소드를 만들기 위해 companion object 사용
    companion object {
        fun Activity.setStatusBarTransparent() {
            window.apply {
                setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }
            // API 30 이상에 적용
            if (Build.VERSION.SDK_INT >= 30) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }
        }
    }
}