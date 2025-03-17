package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * MVVM架构下，给项目提供全局获取Activity的方式
 */

class SunnyWeatherApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN = "iE9AscymNW9s89NP"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}