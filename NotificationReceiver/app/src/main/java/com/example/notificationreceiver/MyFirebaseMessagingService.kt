package com.example.notificationreceiver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * NotificationReceiver
 * Created by authorName
 * Date: 2022-04-20
 * Time: 오후 10:15
 * */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG: String = "로그"

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }

    // 앱이 복원, 재설치, 데이터 삭제 됐을 경우
    // 서버에 Token이 갱신됨을 알려주는 처리를 하는 메소드
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "MyFirebaseMessagingService - onNewToken() called - token created: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        val from = remoteMessage.from
        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        type ?: return

        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        Log.d(TAG,
            "MyFirebaseMessagingService - onMessageReceived() called - message received: $remoteMessage")
        Log.d(TAG, "from: ${from}, title: ${title}, message: $message")

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESCIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    @SuppressLint("InlinedApi")
    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?,
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        // pendingIntent -> 직접 다루지 않는 특정 경우에 작동하는 권한
        // type.id로 리퀘스트 코드를 설정하면 type별로 인텐트가 구분됨
        val pendingIntent =
            PendingIntent.getActivity(this, type.id, intent, FLAG_MUTABLE)
            Log.d(TAG, "MyFirebaseMessagingService - createNotification() - pendingIntent called")

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "\uD83D\uDE00 \uD83D\uDE03 \uD83D\uDE04 \uD83D\uDE01 \uD83D\uDE06 "
                                    + "\uD83D\uDE05 \uD83D\uDE02 \uD83E\uDD23 ☺️ \uD83D\uDE0A \uD83D\uDE07"
                                    + " \uD83D\uDE42 \uD83D\uDE43 \uD83D\uDE09 \uD83D\uDE0C \uD83D\uDE0D "
                                    + "\uD83E\uDD70 \uD83D\uDE18 \uD83D\uDE17 \uD83D\uDE19 \uD83D\uDE1A "
                                    + "\uD83D\uDE0B \uD83D\uDE1B \uD83D\uDE1D \uD83D\uDE1C \uD83E\uDD2A "
                                    + "\uD83E\uDD28 \uD83E\uDDD0 \uD83E\uDD13 \uD83D\uDE0E \uD83E\uDD78 "
                                    + "\uD83E\uDD29 \uD83E\uDD73 \uD83D\uDE0F \uD83D\uDE12"
                        )
                )
            }
            NotificationType.CUSTOM -> {
                notificationBuilder
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }

        return notificationBuilder.build()
    }
}