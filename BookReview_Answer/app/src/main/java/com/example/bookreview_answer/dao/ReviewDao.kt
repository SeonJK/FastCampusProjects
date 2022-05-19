package com.example.bookreview_answer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreview_answer.model.Review

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-19
 * Time: 오후 12:18
 * */
@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)

    @Query("SELECT * FROM Review WHERE id == :id")
    fun getReview(id: Int): Review
}