package com.seonjk.todo_mvvm.data.local_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seonjk.todo_mvvm.data.entity.ToDoEntity
import com.seonjk.todo_mvvm.data.local_db.dao.ToDoDao

@Database(
    entities = [ToDoEntity::class],
    version=1,
    exportSchema = false)
abstract class ToDoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "ToDoDatabase.db"
    }

    abstract fun toDoDao(): ToDoDao
}