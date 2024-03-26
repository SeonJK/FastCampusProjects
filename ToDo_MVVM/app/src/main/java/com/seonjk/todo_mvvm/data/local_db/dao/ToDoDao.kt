package com.seonjk.todo_mvvm.data.local_db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.seonjk.todo_mvvm.data.entity.ToDoEntity

interface ToDoDao {

    @Query("SELECT * FROM TODOENTITY")
    suspend fun getAll(): List<ToDoEntity>

    @Query("SELECT * FROM TODOENTITY WHERE id=:id")
    suspend fun getItem(id: Long): ToDoEntity?

    @Insert
    suspend fun insert(toDoEntity: ToDoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoEntityList: List<ToDoEntity>)

    @Query("DELETE FROM TODOENTITY WHERE id=:id")
    suspend fun deleteItem(id: Long)

    @Query("DELETE FROM TODOENTITY")
    suspend fun deleteAll()

    @Update
    suspend fun update(toDoEntity: ToDoEntity)
}