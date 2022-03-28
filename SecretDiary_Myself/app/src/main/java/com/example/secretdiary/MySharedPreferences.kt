package com.example.secretdiary

import android.content.Context
import android.content.SharedPreferences

/**
 * SecretDiary
 * Created by authorName
 * Date: 2022-03-27
 * Time: 오후 11:17
 * */
class MySharedPreferences(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyPassword = "diaryPassword"
    private val prefsKeyContent = "diaryText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    var diaryPassword: String?
        get() = prefs.getString(prefsKeyPassword, "000")
        set(value) = prefs.edit().putString(prefsKeyPassword, value).apply()

    var diaryText: String?
        get() = prefs.getString(prefsKeyContent, "")
        set(value) = prefs.edit().putString(prefsKeyContent, value).apply()
}