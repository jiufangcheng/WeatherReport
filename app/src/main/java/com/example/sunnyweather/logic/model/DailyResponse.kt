package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DailyResponse(val status: String, val result: Result){
    data class Result(val daily:Daily)
    data class Daily(val temperature: List<Temperature>, val skycon: List<Skycon>,
                     @SerializedName("life_index") val lifeIndex: LifeIndex)
    data class Temperature(val date:Date,val max: Float, val min: Float,val avg:Float)
    data class Skycon( val date: Date,val value: String)
    data class LifeIndex(val coldRisk: List<LifeDescription>, val carWashing:
    List<LifeDescription>, val ultraviolet: List<LifeDescription>,
                         val dressing: List<LifeDescription>)

    data class LifeDescription(val desc: String)
}