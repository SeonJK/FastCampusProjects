package com.seonjk.mygithubrepo.utility

import android.content.Context
import androidx.room.Room
import com.seonjk.mygithubrepo.data.database.SimpleGithubDatabase

object DataBaseProvider {

    private const val DB_NAME = "github_repository_app.db"

    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        SimpleGithubDatabase::class.java, DB_NAME
    ).build()
}