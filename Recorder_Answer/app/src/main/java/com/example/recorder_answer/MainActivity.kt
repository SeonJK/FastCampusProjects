package com.example.recorder_answer

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.recorder_answer.databinding.ActivityMainBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val requiredPermission = arrayOf(Manifest.permission.RECORD_AUDIO)

    private val fileName: String by lazy {
        "${externalCacheDir?.absolutePath}/recordTest.m4a"
    }

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    private var state = State.BEFORE_RECORDING
        set(value) {
            // field -> 실제 프로퍼티 값
            field = value
            binding.resetButton.isEnabled = (value == State.AFTER_RECORDING) ||
                    (value == State.ON_PLAYING)
            binding.recordButton.updateIconWithState(value)
        }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        requestAudioPermission()

        initVariables()
        initViews()
        bindViews()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val permissionToRecordAccepted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        if(!permissionToRecordAccepted) finish()
    }

    private fun requestAudioPermission() {
        requestPermissions(requiredPermission, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun initVariables() {
        state = State.BEFORE_RECORDING
    }

    private fun initViews() {
        binding.recordButton.updateIconWithState(state)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun bindViews() {
        binding.recordButton.setOnClickListener {
            when (state) {
                State.BEFORE_RECORDING -> {
                    startRecording()
                }
                State.ON_RECORDING -> {
                    stopRecording()
                }
                State.AFTER_RECORDING -> {
                    startPlaying()
                }
                State.ON_PLAYING -> {
                    stopPlaying()
                }
            }
        }

        binding.resetButton.setOnClickListener {
            stopPlaying()
            state = State.BEFORE_RECORDING
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() {
        recorder = MediaRecorder(applicationContext).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
            } catch (e: IOException) {
                Log.d(TAG, "startRecording() - prepare() failed")
            }
        }
        recorder?.start()

        state = State.ON_RECORDING
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null

        state = State.AFTER_RECORDING
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            setDataSource(fileName)

            try {
                prepare()
            } catch (e: IOException) {
                Log.d(TAG, "startPlaying() - prepare() failed")
            }
        }
        player?.start()

        state = State.ON_PLAYING
    }

    private fun stopPlaying() {
        player?.release()
        player = null

        state = State.AFTER_RECORDING
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}