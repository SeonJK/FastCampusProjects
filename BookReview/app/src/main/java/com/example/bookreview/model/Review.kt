package com.example.bookreview.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-19
 * Time: 오후 4:03
 * */
@Entity
data class Review (
    @PrimaryKey val id: Int?,
    @ColumnInfo val text: String?
)