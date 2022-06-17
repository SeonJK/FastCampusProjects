package com.example.mymap_answer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MyMap_Answer
 * Created by SeonJK
 * Date: 2022-06-09
 * Time: 오후 5:04
 * */
@Parcelize
data class LocationLatLngEntity(
    val latitude: Float,
    val longitude: Float
): Parcelable