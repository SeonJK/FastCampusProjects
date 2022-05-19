package com.example.bookreview_answer.model

import com.google.gson.annotations.SerializedName

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-11
 * Time: 오전 10:36
 * */
data class SearchBookDto (
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)