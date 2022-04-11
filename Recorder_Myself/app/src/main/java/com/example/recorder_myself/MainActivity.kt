package com.example.recorder_myself

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.recorder_myself.databinding.ActivityMainBinding
import java.io.IOException

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    // 권한 요청 관련 변수
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    private var fileName: String = ""
    // 토스트 문자열 저장
    private var text: String = ""

    // 기능 실행 불리언 변수
    private var isStartRecording = false
    private var isStartPlaying = false

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mediaRecorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 권한 요청
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        // 파일이름 설정
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.mpeg"

        runRecordButton()
        runPlayButton()
    }

    // 권한 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted =
            if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            } else {
                false
            }
        if (!permissionToRecordAccepted) finish()
    }

    // 녹음버튼 클릭 처리
    @RequiresApi(Build.VERSION_CODES.S)
    private fun runRecordButton() {
        binding.recordButton.setOnClickListener {
            isStartRecording = !isStartRecording

            onRecord(isStartRecording)
            text = when(isStartRecording) {
                true -> "Start recording"
                false -> "Stop recording"
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    // 재생버튼 클릭 처리
    private fun runPlayButton() {
        binding.playButton.setOnClickListener {
            isStartPlaying = !isStartPlaying

            onPlay(isStartPlaying)
            text = when(isStartPlaying) {
                true -> "Start playing"
                false -> "Stop playing"
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun onRecord(start: Boolean) = if(start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun onPlay(start: Boolean) = if(start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    // 녹음 중지
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        // 녹음버튼을 화면에 없애고, 재생버튼 활성화
        binding.recordButton.setImageResource(R.drawable.ic_record)
        binding.recordButton.visibility = View.GONE
        binding.playButton.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        // recorder 초기화
        mediaRecorder = MediaRecorder(applicationContext).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_2_TS)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)

            // 초기화 시 예외처리
            try {
                prepare()
                // todo {start()}에서 runtimeException 호출됨
                start()
                binding.recordButton.setImageResource(R.drawable.ic_baseline_stop_24)
            } catch (e: IOException) {
                Log.d(TAG, "startRecording() - prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null

        binding.playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    private fun startPlaying() {
        // player 초기화 및 예외처리
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
                binding.playButton.setImageResource(R.drawable.ic_baseline_stop_24)
            } catch (e: IOException) {
                Log.d(TAG, "startPlaying() - prepare() failed")
            }
        }
    }

}