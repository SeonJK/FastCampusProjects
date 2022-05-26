package com.example.mymap.model

/**
 * MyMap
 * Created by SeonJK
 * Date: 2022-05-23
 * Time: 오후 5:09
 * */
data class SearchResult (
    val fullAddress: String,
    val buildingName: String,
    val locationLatLng: LocationLatLng
)