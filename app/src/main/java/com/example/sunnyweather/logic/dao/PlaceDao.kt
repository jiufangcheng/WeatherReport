package com.example.sunnyweather.logic.dao

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import com.example.sunnyweather.R
import com.example.sunnyweather.SunnyWeatherApplication
import com.example.sunnyweather.logic.model.Geocode
import com.google.gson.Gson

object PlaceDao {
    val shp = SunnyWeatherApplication.context.getSharedPreferences(
        "myData", Context.MODE_PRIVATE)
    fun savePlace(geocode: Geocode){
        val editor = shp.edit()
        editor.putString("geocode", Gson().toJson(geocode))
        editor.apply() // 异步提交
    }
    fun getSavedPlace():Geocode {
        val placejson = shp.getString("geocode","")
        return Gson().fromJson(placejson,Geocode::class.java)
    }

    fun isPlaceSaved()= shp.contains("geocode")


}