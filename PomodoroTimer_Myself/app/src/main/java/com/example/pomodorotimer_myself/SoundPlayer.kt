package com.example.pomodorotimer_myself

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * PomodoroTimer_Myself
 * Created by authorName
 * Date: 2022-04-08
 * Time: 오후 9:01
 * */
class SoundPlayer {

    companion object {
        const val DONE = R.raw.timer_bell
        const val TICKING = R.raw.timer_ticking

        lateinit var soundPool: SoundPool
        lateinit var soundPoolMap: HashMap<Int, Int>

        fun initSounds(context: Context) {
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            soundPool = SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build()

            soundPoolMap = HashMap(2)
            soundPoolMap[DONE] = soundPool.load(context, DONE, 1)
            soundPoolMap[TICKING] = soundPool.load(context, TICKING, 1)

        }

        fun play(rawId: Int) {
            if( soundPoolMap.containsKey(rawId) ) {
                soundPoolMap[rawId]?.let {
                    soundPool.play(it, 1F, 1F, 0, -1, 1f) }
            }
        }

        fun alarmPlay(rawId: Int) {
            if( soundPoolMap.containsKey(rawId) ) {
                soundPoolMap[rawId]?.let {
                    soundPool.play(it, 1f, 1f, 0, 1, 1f) }
            }
        }
    }
}