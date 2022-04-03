package com.example.calculatoranswer.dao

import androidx.room.*
import com.example.calculatoranswer.model.History

/**
 * CalculatorAnswer
 * Created by authorName
 * Date: 2022-03-31
 * Time: 오후 5:26
 * */

@Dao
interface HistoryDao {
    @Insert
    fun insert(item: History)

    @Query("SELECT * FROM History")
    fun getAll(): List<History>

    @Query("DELETE FROM History")
    fun deleteAll()

}