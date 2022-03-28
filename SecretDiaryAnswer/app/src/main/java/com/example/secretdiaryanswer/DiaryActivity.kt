package com.example.secretdiaryanswer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import com.example.secretdiaryanswer.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {

    private val binding: ActivityDiaryBinding by lazy {
        ActivityDiaryBinding.inflate(layoutInflater)
    }

    // MainLooper - 메인스레드와 연결된 핸들러 생성
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // SharedPreferences 생성
        val detailPreferences = getSharedPreferences("diary", Context.MODE_PRIVATE)

        binding.diaryContent.setText(detailPreferences.getString("detail", ""))

        // 잠깐 멈출 때마다 저장하도록 쓰레드 설정
        val runnable = Runnable {
            // commit==false 이기 때문에 지속적으로 sharedPreferences에 저장
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("detail", binding.diaryContent.text.toString())
            }
        }

        // EditText의 text가 변경될 때마다 저장하도록 설정
        binding.diaryContent.addTextChangedListener {
            // 핸들러 실행 전에 pending되어 있는 Runnable을 지워주기 위해 사용
            handler.removeCallbacks(runnable)
            // 몇 초 이후에 쓰레드(runnable)을 실행시키도록 함
            handler.postDelayed(runnable, 500)
        }
    }
}