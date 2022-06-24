package com.example.mymap_answer.utility

import com.example.mymap_answer.response.address.AddressInfoResponse
import com.example.mymap_answer.response.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-06-21
 * Time: 오후 12:47
 * */
interface ApiService {

    @GET(Url.GET_TMAP_LOCATION)
    suspend fun getSearchLocation(
        @Header("appKey") appKey: String = Key.TMAP_API,
        @Query("version") version: Int = 1,
        @Query("searchKeyword") keyword: String,
        @Query("searchType") searchType: String? = null,
        @Query("searchtypCd") searchtypCd: String? = null,
        @Query("count") count: Int = 20,
        @Query("reqCoordType") reqCoordType: String? = null,
        @Query("resCoordType") resCoordType: String? = null,
        @Query("centerLon") centerLon: String? = null,
        @Query("centerLat") centerLat: String? = null,
        @Query("radius") radius: String? = null,
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("multiPoint") multiPoint: String? = null,
        @Query("callback") callback: String? = null,
    ): Response<SearchResponse>

    @GET(Url.GET_TMAP_REVERSE_GEO_CODE)
    suspend fun getReverseGeoCode(
        @Header("appKey") appKey: String = Key.TMAP_API,
        @Query("version") version: Int = 1,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("coordType") coordType: String? = null,
        @Query("addressType") addressType: String? = null,
        @Query("callback") callback: String? = null
    ): Response<AddressInfoResponse>
}