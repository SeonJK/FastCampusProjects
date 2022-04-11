package com.example.pomodorotimer_myself

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.pomodorotimer_myself.Util.StatusbarTransparent.Companion.setStatusBarTransparent
import com.example.pomodorotimer_myself.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var currentCountDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 상태바 투명으로 설정
        this.setStatusBarTransparent()
        setContentView(binding.root)

        SoundPlayer.initSounds(applicationContext)

        bindViews()
    }

    override fun onResume() {
        super.onResume()
        SoundPlayer.soundPool.autoResume()
    }

    override fun onPause() {
        super.onPause()
        SoundPlayer.soundPool.autoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 메모리 해제
        SoundPlayer.soundPool.release()
    }

    private fun bindViews() {
        binding.seekbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // fromUser - 유저가 프로그레스바로 바꾸었는지 / 코드 상에서 바꾸었는지 알려주는 값
                    if(fromUser) {
                        remainTime(progress * 60 * 1000L)
                    }
                    SoundPlayer.soundPool.autoResume()
                }

                override fun onStartTrackingTouch(seeekBar: SeekBar?) {
                    // 기존의 타이머를 멈추고 새로운 타이머 구동
                    stopCountDown()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    // seekBar가 null이면 진행하는게 무의미 함. 그래서 null이면 return
                    seekBar ?: return

                    if(seekBar.progress == 0) { // 사용자가 0으로 설정했을 경우
                        stopCountDown()
                    } else {
                        currentCountDownTimer = countDown(seekBar.progress * 60 * 1000L)
                        currentCountDownTimer?.start()
                        // 째깍째깍 소리
                        SoundPlayer.play(SoundPlayer.TICKING)
                    }


                }
            }
        )
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
        SoundPlayer.soundPool.autoPause()
    }

    private fun countDown(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 남은 시간을 Long형에서 Int형으로 변환
                remainTime(millisUntilFinished)
                // seekBar 진행
                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                remainTime(0)
                updateSeekBar(0)

                // 알람 소리
                SoundPlayer.soundPool.autoPause()
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