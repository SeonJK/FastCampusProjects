package com.example.pomodorotimer_myself

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import com.example.pomodorotimer_myself.databinding.ActivityMainBinding
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SoundPlayer.initSounds(applicationContext)

        bindViews()
    }

    override fun onStop() {
        super.onStop()
        currentCountDownTimer?.cancel()
    }

    private fun bindViews() {
        binding.seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // fromUser - 유저가 프로그레스바로 바꾸었는지 / 코드 상에서 바꾸었는지 알려주는 값
                    if(fromUser) {
                        remainTime(progress * 60 * 1000L)
                    }
                }

                override fun onStartTrackingTouch(seeekBar: SeekBar?) {
                    // 기존의 타이머를 멈추고 새로운 타이머 구동
                    currentCountDownTimer?.cancel()
                    currentCountDownTimer = null
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // seekBar가 null이면 진행하는게 무의미 함. 그래서 null이면 return
                    seekBar ?: return

                    currentCountDownTimer = countDown(seekBar.progress * 60 * 1000L)
                    currentCountDownTimer?.start()
                }
            }
        )
    }

    private fun countDown(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 남은 시간을 Long형에서 Int형으로 변환
                remainTime(millisUntilFinished)
                // seekBar 진행
                updateSeekBar(millisUntilFinished)
                // 째깍째깍 소리
                SoundPlayer.play(SoundPlayer.TICKING)
            }

            override fun onFinish() {
                remainTime(0)
                updateSeekBar(0)

                // 알람 소리
                SoundPlayer.alarmPlay(SoundPlayer.DONE)
            }
        }.start()


    @SuppressLint("SetTextI18n")
    private fun remainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        binding.tvMinutes.text = "%02d'".format(remainSeconds / 60)
        binding.tvSeconds.text = "%02d\"".format(remainSeconds % 60)
    }

    private fun updateSeekBar(remainMillis: Long) {
        binding.seekbar.progress = (remainMillis / 1000 / 60).toInt()
    }
}