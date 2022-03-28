package com.example.secretdiary

import android.app.Application

/**
 * SecretDiary
 * Created by authorName
 * Date: 2022-03-27
 * Time: 오후 11:15
 * */
class App : Application() {

    companion object {
        lateinit var prefs : MySharedPreferences
    }

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}