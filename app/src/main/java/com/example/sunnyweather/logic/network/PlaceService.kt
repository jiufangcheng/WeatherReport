package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.PlaceResponse
import com.example.sunnyweather.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PlaceService {
//    @GET("v3/geocode/geo?address={address}&key=${SunnyWeatherApplication.KEY}")
//    fun searchPlace(@Path("address") address:String): Call<PlaceResponse>

    @GET("v3/geocode/geo")
    fun searchPlace(
        @Query("address") address:String,
        @Query("key") key:String=SunnyWeatherApplication.KEY
    ):Call<PlaceResponse>
}