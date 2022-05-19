package com.example.bookreview_answer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bookreview_answer.model.History

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-17
 * Time: 오후 5:08
 * */
@Dao
interface HistoryDao {
    @Insert
    fun insertHistory(item: History)

    @Query("SELECT * FROM History")
    fun getAllHistories(): List<History>

    @Query("DELETE FROM History WHERE keyword == :keyword")
    fun deleteHistory(keyword: String)
}