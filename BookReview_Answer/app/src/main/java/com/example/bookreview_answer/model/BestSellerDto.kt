package com.example.bookreview_answer.model

import com.google.gson.annotations.SerializedName

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-11
 * Time: 오전 10:17
 *
 * Dto는 json의 전체적인 모델에서 데이터를 꺼내올 수 있도록 하는 개념
 * */
data class BestSellerDto (
    @SerializedName("title") val title: String,
    @SerializedName("item") val books: List<Book>
)