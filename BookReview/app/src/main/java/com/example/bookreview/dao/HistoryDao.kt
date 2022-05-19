package com.example.bookreview.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bookreview.model.History

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-13
 * Time: 오전 10:38
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