package com.sunnyweather.android.logic.model

/**
 * 天气类，封装实时天气和未来几天天气
 */
data class Weather(val realtime: RealtimeResponse.Realtime, val daily: DailyResponse.Daily)
