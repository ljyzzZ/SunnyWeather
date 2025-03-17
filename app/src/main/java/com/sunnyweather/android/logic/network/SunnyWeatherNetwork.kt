package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 统一的网络数据源访问入口，对所有网络请求的API进行封装
 */
object SunnyWeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>() // 创建PlaceService接口的动态代理对象

    // suspend：这是一个挂起函数，它可以在协程中被调用，并且可以在执行过程中暂停和恢复
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    // 自定义 await 扩展函数，将 Retrofit 的异步回调转换为协程的挂起函数，使得代码可以以同步的方式编写
    private suspend fun <T> Call<T>.await(): T {
        // 将协程异步回调转换为挂起函数
        return suspendCoroutine { continuation ->
            // Retrofit 中用于发起异步网络请求的方法，它接受一个 Callback 对象作为参数
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body) // 恢复协程并返回数据
                    } else {
                        continuation.resumeWithException(RuntimeException("Response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })

        }
    }
}