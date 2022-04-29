package com.example.alaram

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Alaram
 * Created by SeonJK
 * Date: 2022-04-28
 * Time: 오후 5:14
 * */
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val NOTIFICATION_ID = 101
        private const val CHANNEL_ID = "alarm_noti"
        private const val CHANNEL_NAME = "알람"
        private const val CHANNEL_DESCRIPTION = "Notificaiton for Alarm"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // 알림 채널 생성
        createNotificationChannel(context)
        // 알림 생성
        createNotification(context)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION

            NotificationManagerCompat.from(context).createNotificationChannel(channel)
        }
    }

    private fun createNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("알람")
                .setContentText("일어날 시간 입니다!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            notify(NOTIFICATION_ID, builder.build())
        }
    }
}