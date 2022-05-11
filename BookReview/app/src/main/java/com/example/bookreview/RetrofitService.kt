package com.example.bookreview

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-03
 * Time: 오후 4:48
 * */
interface RetrofitService {

    @GET("api/bestSeller.api")
    fun getBestSellerList(
        @Query("key")apiKey: String,
        @Query("categoryId")categoryId: Int,
        @Query("output")output: String
    ) : Call<BookItem>

    // TODO: 책 검색에 대한 호출 메소드 생성
}