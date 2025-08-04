package com.example.sunnyweather.logic.network

import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.DailyResponse
import com.example.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService  {

//    @GET("v2.6/${SunnyWeatherApplication.TOKEN}/weather.json?")
//    fun searchWeather(@Query("adcode") adcode:String):Call<WeatherResponse>

    @GET("v2.6/{token}/{lng_lat}/realtime")
    fun getRealtimeWeather(
        @Path("token") token:String,
        @Path("lng_lat") lngLat: String
    ): Call<RealtimeResponse>

    @GET("v2.6/{token}/{lng_lat}/daily?dailysteps=3")
    fun getDailyWeather(
        @Path("token") token:String,
        @Path("lng_lat") lngLat: String
    ):Call<DailyResponse>
}