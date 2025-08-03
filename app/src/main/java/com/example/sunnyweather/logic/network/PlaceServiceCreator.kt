package com.example.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlaceServiceCreator {
    private const val PLACE_BASE_URL="https://restapi.amap.com/"
    private val retrofit= Retrofit.Builder()
        .baseUrl(PLACE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>):T= retrofit.create(serviceClass)
    inline fun <reified T> create():T= create(T::class.java)
}