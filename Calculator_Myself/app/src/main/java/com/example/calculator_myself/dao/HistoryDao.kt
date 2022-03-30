package com.example.calculator_myself.dao

import androidx.room.*
import com.example.calculator_myself.model.History

/**
 * Calculator_Myself
 * Created by authorName
 * Date: 2022-03-29
 * Time: 오후 6:06
 * */

@Dao
interface HistoryDao {
    @Insert
    fun insert(item: History)

    @Update
    fun update(item: History)

    @Delete
    fun delete(item: History)

    @Query("SELECT expression FROM History WHERE uid = :uid")
    fun getExpression(uid: Int) : String

    @Query("SELECT result FROM History WHERE uid = :uid")
    fun getResult(uid: Int) : String


}