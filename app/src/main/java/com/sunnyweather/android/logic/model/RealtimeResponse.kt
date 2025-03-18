package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 实时天气接口返回数据模型
 */
data class RealtimeResponse(val status: String, val result: Result) {
    data class Result(val realtime: Realtime)

    data class Realtime(
        val skycon: String, val temperature: Float,
        @SerializedName("air_quality") val airQuality: AirQuality // 注解映射Json字段名
    )

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)
}
