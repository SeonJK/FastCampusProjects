package com.example.notificationreceiver

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * NotificationReceiver
 * Created by authorName
 * Date: 2022-04-20
 * Time: 오후 10:15
 * */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    // 앱이 복원, 재설치, 데이터 삭제 됐을 경우
    // 서버에 Token이 갱신됨을 알려주는 처리를 하는 메소드
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
}