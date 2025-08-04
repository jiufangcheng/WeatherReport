package com.example.sunnyweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.model.Geocode

class WeatherViewModel:ViewModel() {
    private val LngLatLiveData=MutableLiveData<String>()
    var lngLat=""
    var placeName=""

    val weatherLiveData=LngLatLiveData.switchMap {
        lngLat->Repository.refreshWeather(lngLat)
    }
    fun refreshWeather(lngLat:String){
        LngLatLiveData.value=lngLat
    }
}