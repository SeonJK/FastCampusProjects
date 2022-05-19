package com.example.bookreview_answer

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bookreview_answer.dao.HistoryDao
import com.example.bookreview_answer.dao.ReviewDao
import com.example.bookreview_answer.model.History
import com.example.bookreview_answer.model.Review

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-17
 * Time: 오후 5:08
 * */
@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}

// db 버전을 마이그레이션 하기 위해 필요한 메소드
fun getAppDatabase(context: Context): AppDatabase {

    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE `REVIEW` (`id` INTEGER, `review` TEXT, PRIMARY KEY(`id`))")
        }

    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "bookSearchDB"
    )
        .addMigrations(migration_1_2)
        .build()
}