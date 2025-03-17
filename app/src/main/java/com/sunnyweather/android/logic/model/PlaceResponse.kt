package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 定义城市数据模型
 */
data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(
    val name: String, val location: com.sunnyweather.android.logic.model.Location,
    @SerializedName("formatted_address") val address: String // 注解让json字段和kotlin字段之间建立映射
)

data class Location(val lng: String, val lat: String)