package com.example.sunnyweather.logic


import android.util.Log
import androidx.lifecycle.liveData
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.dao.PlaceDao
import com.example.sunnyweather.logic.model.Geocode
import com.example.sunnyweather.logic.model.Weather

import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

object Repository {



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

    fun savePlace(geocode: Geocode)=PlaceDao.savePlace(geocode)
    fun getSavedPlace()=PlaceDao.getSavedPlace()
    fun isPlaceSaved()=PlaceDao.isPlaceSaved()
}