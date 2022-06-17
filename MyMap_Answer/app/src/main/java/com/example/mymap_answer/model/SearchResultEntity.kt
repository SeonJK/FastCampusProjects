package com.example.mymap_answer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-06-09
 * Time: 오후 5:03
 * */
@Parcelize
data class SearchResultEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLng: LocationLatLngEntity
): Parcelable