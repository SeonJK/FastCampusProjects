package com.example.mymap_answer.response.search

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-06-10
 * Time: 오후 12:34
 * */
data class SearchPoiInfo(
    val totalCount: String,
    val count: String,
    val page: String,
    val pois: Pois
)