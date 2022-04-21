package com.example.notificationreceiver

/**
 * NotificationReceiver
 * Created by authorName
 * Date: 2022-04-21
 * Time: 오후 12:18
 * */
enum class NotificationType(val title: String, val id: Int) {
    NORMAL("일반 알림", 0),
    EXPANDABLE("확장 알림", 1),
    CUSTOM("커스텀 알림", 2)
}