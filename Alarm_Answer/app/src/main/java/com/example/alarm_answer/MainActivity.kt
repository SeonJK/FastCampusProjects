package com.example.alarm_answer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.example.alarm_answer.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    val TAG: String = "로그"

    companion object {
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ONOFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1001
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // step1 뷰를 초기화해주기
        initOnOffAlarmButton()
        initSetTimeButton()

        // step2 데이터 가져오기
        val model = fetchDataFromSharedPreferences()

        // step3 뷰에 데이터를 그려주기
        renderView(model)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun initOnOffAlarmButton() {
        binding.onOffAlarmButton.setOnClickListener {
            // 데이터 확인
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            // 데이터를 저장한다.
            val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
            // 뷰에 그려주기
            renderView(newModel)

            // 온오프 에 따라 작업을 처리한다.
            if (newModel.onOff) {
                // 온 -> 알람을 등록
                Log.d(TAG, "MainActivity - initOnOffAlarmButton() called :: onOff = ${newModel.onOff}")
                Toast.makeText(this, "알람이 ${newModel.ampmText} ${newModel.timeText} 로 설정되었습니다.",
                        Toast.LENGTH_SHORT).show()

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, newModel.hour)
                    set(Calendar.MINUTE, newModel.minute)

                    // 현재 날짜보다 전 날짜일 경우 하루 뒤로 날짜 세팅
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DATE, 1)
                    }
                }

                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intent = Intent(this, AlarmReceiver::class.java)

                // TargetAPI(S) 대응
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                            intent, PendingIntent.FLAG_MUTABLE)

                    // RTC_WAKEUP이 기본 내장
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                        pendingIntent
                    )
                } else {
                    val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            } else {
                // 오프 -> 알람을 제거
                Log.d(TAG, "MainActivity - initOnOffAlarmButton() called :: onOff = ${newModel.onOff}")
                Toast.makeText(this, "설정한 알람이 취소되었습니다.", Toast.LENGTH_SHORT).show()

                cancelAlarm()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initSetTimeButton() {
        binding.setTimeButton.setOnClickListener {
            // 현재시간을 일단 가져온다.
            val calendar = Calendar.getInstance()

            // TimePickerDialog 띄워줘서 시간 설정을 하도록 하고, 그 시간을 가져와서
            TimePickerDialog(this, { _, hour, minute ->
                // 데이터를 저장한다.
                val model = AlarmDisplayModel(hour, minute, false)
                saveAlarmModel(model.hour, model.minute, model.onOff)

                // 기존에 있던 알람을 삭제한다.
                cancelAlarm()

                // 뷰를 업데이트 한다.
                renderView(model)

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                .show()
        }
    }

    private fun saveAlarmModel(
        hour: Int,
        minute: Int,
        onOff: Boolean
    ): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        // SharedPreferences 에 데이터를 저장한다.
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ONOFF_KEY, model.onOff)
            commit()
        }
        // with 를 사용하거나 KTX 로 바로 edit()를 열면 commit 사용 안해도 됨
//        sharedPreferences.edit {
//            putString("alarm", model.makeDataForDB())
//            putBoolean("onOff", model.onOff)
//        }

        return model
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        // 저장된 값 불러오기
        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "07:30") ?: "07:30"
        val onOffDBValue = sharedPreferences.getBoolean(ONOFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val model = AlarmDisplayModel(
        hour = alarmData[0].toInt(),
        minute = alarmData[1].toInt(),
        onOff = onOffDBValue
        )

        // 예외처리
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
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
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                Intent(this, AlarmReceiver::class.java), PendingIntent.FLAG_NO_CREATE)
        }

        pendingIntent?.cancel()
    }
}