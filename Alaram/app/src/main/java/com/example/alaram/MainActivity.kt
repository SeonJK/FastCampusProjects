package com.example.alaram

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.alaram.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
// 모델을 만들지 않고 구현하게 되면 hour, minute, onOff를 한번에 리턴하여 사용 불가

    companion object {
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val REQUEST_CODE = 1001
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰를 초기화해주기
        initOnOffAlarmButton()
        initSetTimeButton()

        // 데이터 가져오기
        val model = loadData()

        // 뷰에 데이터 그려주기
        renderView(model)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun initOnOffAlarmButton() {
        binding.onOffAlarmButton.setOnClickListener {
            // 데이터 확인
            val model = loadData()
            // 데이터 저장
            val newModel = saveDataInSharedPreferences(model.hour, model.minute, model.onOff.not())
            renderView(newModel)

            if (newModel.onOff) {
                // 알람 등록 및 기존 알람 제거
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)

                val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
                } else {
                    PendingIntent.getBroadcast(this, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            } else {
                // 알람 끄기
                cancelAlarm()
            }


        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSetTimeButton() {
        binding.setTimeButton.setOnClickListener {
            // 현재 시간 가져오기
            val calendar = Calendar.getInstance()

            TimePickerDialog(this,
                { _, hourOfDay, minute ->

                    val model = AlarmDisplayModel(hourOfDay, minute, false)

                    // 데이터 저장
                    saveDataInSharedPreferences(model.hour, model.minute, model.onOff)

                    // 기존 알람 제거
                    cancelAlarm()

                    // 시간이 설정되면 텍스트뷰에 변화 적용
                    renderView(model)
                },
                // 다이얼로그에 현재시간 세팅
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun saveDataInSharedPreferences(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }

        return model
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun loadData(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        // 저장된 데이터 가져오기
        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "07:30") ?: "07:30"
        val alarmData = timeDBValue.split(":")
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)

        val model = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )


        // 예외처리
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getBroadcast(this, REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        }

        if ((pendingIntent == null) and model.onOff) {
            // 알람은 꺼져있는데, 데이터는 켜져있다고 저장되어 있는 경우
            model.onOff = false
        } else if ((pendingIntent != null) and model.onOff.not()) {
            // 알람은 켜져있는데, 데이터는 꺼져있다고 저장되어 있는 경우
            // 알람 취소
            pendingIntent.cancel()
        }

        return model
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.ampmTextView.text = model.ampmText

        binding.timeTextView.text = model.timeText

        binding.onOffAlarmButton.apply {
            text = model.onOffText
            tag = model
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun cancelAlarm() {
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getBroadcast(this, REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getBroadcast(this, REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        }

        pendingIntent.cancel()
    }
}