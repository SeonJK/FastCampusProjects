package com.example.calculator_myself.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Calculator_Myself
 * Created by authorName
 * Date: 2022-03-29
 * Time: 오후 6:01
 * */

@Entity
data class History(
    @ColumnInfo(name="expression") val expression: String?,
    @ColumnInfo(name="result") val result: String?
) {
    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}