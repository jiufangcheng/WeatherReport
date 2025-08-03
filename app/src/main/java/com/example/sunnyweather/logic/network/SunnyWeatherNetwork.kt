package com.example.sunnyweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {
    private val weatherService=WeatherServiceCreator.create<WeatherService>()
    private val placeService=PlaceServiceCreator.create<PlaceService>()
    suspend fun searchPlace(address:String)= placeService.searchPlace(address).await()
    suspend fun searchWeather(adcode:String)= weatherService.searchWeather(adcode).await()
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine{continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if (body!=null)
                        continuation.resume(body)
                    else
                        continuation.resumeWithException(RuntimeException("response is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
//    当外部调用SunnyWeatherNetwork的searchPlaces()函数时，Retrofit就会立即
//    发起网络请求，同时当前的协程也会被阻塞住。直到服务器响应我们的请求之后，await()函
//    数会将解析出来的数据模型对象取出并返回，同时恢复当前协程的执行，searchPlaces()函
//    数在得到await()函数的返回值后会将该数据再返回到上一层
}