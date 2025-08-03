package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService  {

//    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/weather.json?")
//    fun searchWeather(@Query("adcode") adcode:String):Call<WeatherResponse>

    @GET("v2.6/weather.json")
    fun searchWeather(
        @Query("adcode") adcode: String,
        @Query("token") token: String = SunnyWeatherApplication.TOKEN
    ): Call<WeatherResponse>
}