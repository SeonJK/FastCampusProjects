package com.example.bookreview_answer.api

import com.example.bookreview_answer.model.BestSellerDto
import com.example.bookreview_answer.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * BookReview_Answer
 * Created by SeonJK
 * Date: 2022-05-11
 * Time: 오전 10:08
 * */
interface BookService {

    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>
}