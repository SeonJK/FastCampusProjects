package com.example.secretdiary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.example.secretdiary.databinding.ActivityTextBinding

class textActivity : AppCompatActivity() {

    private val binding: ActivityTextBinding by lazy {
        ActivityTextBinding.inflate(layoutInflater)
    }

    // MainLooper - 메인스레드와 연결된 핸들러 생성
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        App.prefs.diaryText
        binding.diaryContent.setText(App.prefs.diaryText)

        // 잠깐 멈출 때마다 저장하도록 쓰레드 설정
        val runnable = Runnable {
            App.prefs.diaryText = binding.diaryContent.text.toString()
            Log.d(TAG, "textActivity - onCreate() called - ${binding.diaryContent.text.toString()}")
        }

        // EditText의 text가 변경될 때마다 저장하도록 설정
        binding.diaryContent.addTextChangedListener {
            // 핸들러 설정 전에 pending 되어있는 Runnable을 지워주기 위해 사용
            handler.removeCallbacks(runnable)
            // 몇 초 이후에 쓰레드(Runnable)을 실행시키도록 함
            handler.postDelayed(runnable, 500)
        }
    }
}