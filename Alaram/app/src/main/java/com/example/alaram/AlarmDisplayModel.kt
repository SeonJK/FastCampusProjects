package com.example.alaram

/**
 * Alaram
 * Created by SeonJK
 * Date: 2022-04-28
 * Time: 오전 1:24
 * */
data class AlarmDisplayModel(
    val hour: Int,
    val
    minute: Int,
    var onOff: Boolean
) {
    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour-12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }

    val onOffText: String
        get() {
            return if (onOff) "알람 끄기" else "알람 켜기"
        }

    fun makeDataForDB(): String {
        return "$hour:$minute"
    }
}