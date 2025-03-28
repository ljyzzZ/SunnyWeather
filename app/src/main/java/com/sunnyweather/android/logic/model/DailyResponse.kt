package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * 未来几天天气接口返回数据模型
 */
data class DailyResponse(val status: String, val result: Result) {
    data class Result(val daily: Daily)
    data class Daily(
        val temperature: List<Temperature>, val skycon: List<SkyCon>,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    )

    data class Temperature(val max: Float, val min: Float)
    data class SkyCon(val value: String, val date: Date)
    data class LifeIndex(
        val coldRisk: List<LifeDescription>, val carWashing:
        List<LifeDescription>, val ultraviolet: List<LifeDescription>,
        val dressing: List<LifeDescription>
    )

    data class LifeDescription(val desc: String)
}