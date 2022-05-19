package com.example.bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-13
 * Time: 오전 10:30
 * */
@Entity
data class History (
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="keyword") val keyword: String?
)