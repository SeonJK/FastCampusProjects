package com.example.bookreview.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookreview.model.Review

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-19
 * Time: 오후 4:01
 * */
@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveReview(review: Review)

    @Query("SELECT * FROM Review WHERE id == :id")
    fun getReview(id: Int): Review
}