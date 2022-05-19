package com.example.bookreview_answer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-19
 * Time: 오후 12:21
 * */
@Entity
data class Review (
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "review") val review: String?
)