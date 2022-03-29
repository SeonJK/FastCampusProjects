package com.example.calculator_myself

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculator_myself.dao.HistoryDao
import com.example.calculator_myself.model.History

/**
 * Calculator_Myself
 * Created by authorName
 * Date: 2022-03-29
 * Time: 오후 11:45
 * */

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}