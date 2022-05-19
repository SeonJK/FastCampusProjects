package com.example.bookreview.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-03
 * Time: 오후 4:34
 * */

// api에서 카테고리 제목과 item 목록을 받는다.
data class BookItem(
    @SerializedName("title")
    val categoryTitle: String,

    @SerializedName("item")
    val bookList: List<Book>
)

// item 배열 속에서 가져올 것들
@Parcelize
data class Book(
    @SerializedName("itemId")
    val id: Long,

    @SerializedName("title")


    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("coverSmallUrl")
    val imgUrl: String
): Parcelable