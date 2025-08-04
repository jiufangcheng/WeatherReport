package com.example.sunnyweather.logic


import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.Geocode
import com.example.sunnyweather.logic.model.Weather

import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {


//    fun refreshWeather(lngLat:String)= liveData(Dispatchers.IO) {
//        val result=try {
//            coroutineScope {
//                val deferredRealtime=async {
//                    SunnyWeatherNetwork.getRealtimeResponse(SunnyWeatherApplication.TOKEN,lngLat)
//                }
//                val deferredDaily=async {
//                    SunnyWeatherNetwork.getDailyReponse(SunnyWeatherApplication.TOKEN,lngLat)
//                }
//                val realtimeResponse = deferredRealtime.await()
//                val dailyResponse = deferredDaily.await()
//                if (realtimeResponse.status=="ok" && dailyResponse.status=="ok" ){
//                    val weather=Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
//                    Result.success(weather)
//                }else{
//                    Result.failure(RuntimeException( "realtime response status is ${realtimeResponse.status}" +
//                            "daily response status is ${dailyResponse.status})"))
//                }
//            }
//        }catch (e:Exception){
//            Result.failure<Weather>(e)
//        }
//        emit(result)
//    }
//
//
//    fun searchPlace(address:String)= liveData(Dispatchers.IO){
//        val result=try{
//            val placeResponse=SunnyWeatherNetwork.searchPlace(address)
//            if (placeResponse.status=="1"){
//                val geocodes=placeResponse.geocodes
//                Result.success(geocodes)
//            }else{
//                Result.failure(RuntimeException("place response is ${placeResponse.status}"))
//            }
//        }catch (e:Exception){
//            Result.failure<List<Geocode>>(e)
//        }
//        emit(result)
//    }
    fun searchPlace(address: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlace(address)
        if (placeResponse.status == "1") {
            val places = placeResponse.geocodes
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    fun refreshWeather(lngLat:String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                Log.d("hello", "refreshWeather: ${lngLat}")
                SunnyWeatherNetwork.getRealtimeResponse(SunnyWeatherApplication.TOKEN,lngLat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyReponse(SunnyWeatherApplication.TOKEN,lngLat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            Log.d("hello", "refreshWeather:realtimeResponse ${realtimeResponse.status}\n dailyResponse ${dailyResponse.status} ")
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime,
                    dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Log.e("hello", "fire: 全局捕获异常", e)
                Result.failure<T>(e)
            }
            emit(result)
        }
}