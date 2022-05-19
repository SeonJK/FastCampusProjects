package com.example.bookreview_answer.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-11
 * Time: 오전 10:14
 * */
@Parcelize
data class Book(
    @SerializedName("itemId")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("coverSmallUrl")
    val coverSmallUrl: String
): Parcelable