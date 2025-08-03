package com.example.sunnyweather.logic


import androidx.lifecycle.liveData

import com.example.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

object Repository {
    fun searchWeather(adcode:String)=liveData(Dispatchers.IO){
        val result=try {
            val weatherResponse=SunnyWeatherNetwork.searchWeather(adcode)
            if (weatherResponse.status=="ok"){
                val location=weatherResponse.location
                Result.success(location)
            } else {
                Result.failure(RuntimeException("weather response is ${weatherResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<Exception>(e)
        }
        emit(result)//类似于调用LiveData的 setValue()方法来通知数据变化，只不过这里我们无法直接取得返回的LiveData对象，所以 lifecycle-livedata-ktx库提供了这样一个替代方法
    }
    fun searchPlace(address:String)= liveData(Dispatchers.IO){
        val result=try{
            val placeResponse=SunnyWeatherNetwork.searchPlace(address)
            if (placeResponse.status=="1"){
                val geocodes=placeResponse.geocodes
                Result.success(geocodes)
            }else{
                Result.failure(RuntimeException("place response is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<Exception>(e)
        }
        emit(result)
    }
}