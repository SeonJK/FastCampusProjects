package com.example.calculatoranswer

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculatoranswer.dao.HistoryDao
import com.example.calculatoranswer.model.History

/**
 * CalculatorAnswer
 * Created by authorName
 * Date: 2022-03-31
 * Time: 오후 5:31
 * */

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}