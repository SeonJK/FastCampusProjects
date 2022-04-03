package com.example.calculatoranswer.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * CalculatorAnswer
 * Created by authorName
 * Date: 2022-03-31
 * Time: 오후 5:20
 * */

@Entity
data class History(
    @PrimaryKey val uid: Int?,
    @ColumnInfo(name="expression") val expression: String?,
    @ColumnInfo(name="result") val result: String?
)