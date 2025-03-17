package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 仓库层的统一封装入口
 */
object Repository {

    fun searchPlaces(query: String) =
        liveData(Dispatchers.IO) { // LiveData构建对象，提供线程参数类型：Dispatchers.IO挂起函数的上下文，这样就可以在当前函数代码块中调用任意挂起函数了
            val result = try {
                val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
                if (placeResponse.status == "ok") {
                    val places = placeResponse.places
                    Result.success(places)
                } else {
                    Result.failure(RuntimeException("Response status is ${placeResponse.status}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result) // 通知数据变化
        }
}