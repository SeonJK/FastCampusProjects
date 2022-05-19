package com.example.bookreview_answer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-17
 * Time: 오후 5:09
 * */
@Entity
data class History (
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="keyword") val keyword: String?
)