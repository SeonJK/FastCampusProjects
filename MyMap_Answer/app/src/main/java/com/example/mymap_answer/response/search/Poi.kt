package com.example.mymap_answer.response.search

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-06-10
 * Time: 오후 12:35
 * */
data class Poi (
    // POI의 id
    val id: String? = null,

    // POI의 name
    val name: String? = null,

    // POI에 대한 전화번호
    val telNo: String? = null,

    // 시설물 입구 위도 좌표
    val frontLat: Float = 0.0f,

    // 시설물 입구 경도 좌표
    val frontLon: Float = 0.0f,

    // 중심점 위도 좌표
    val noorLat: Float = 0.0f,

    // 중심점 경도 좌표
    val noorLon: Float = 0.0f,

    // 표출 주소 대분류명
    val upperAddrName: String? = null,

    // 표출 주소 중분류명
    val middleAddrName: String? = null,

    // 표출 주소 소분류명
    val lowerAddrName: String? = null,

    // 표출 주소 세분류명
    val detailAddrName: String? = null,

    // 도로명
    val roadName: String? = null,

    // 건물번호 1
    val firstBuildNo: String? = null,

    // 건물번호 2
    val secondBuildNo: String? = null,

)