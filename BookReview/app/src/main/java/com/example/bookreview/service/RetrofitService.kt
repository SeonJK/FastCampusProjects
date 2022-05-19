package com.example.bookreview.service

import com.example.bookreview.model.BookItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * BookReview
 * Created by SeonJK
 * Date: 2022-05-03
 * Time: 오후 4:48
 * */
interface RetrofitService {

    @GET("api/bestSeller.api?categoryId=100&output=json")
    fun getBestSellerList(
        @Query("key")apiKey: String
    ) : Call<BookItem>

    // TODO: 책 검색에 대한 호출 메소드 생성
    @GET("api/search.api?output=json")
    fun searchBookList(
        @Query("key")apiKey: String,
        @Query("query") keyword: String
    ) : Call<BookItem>
}