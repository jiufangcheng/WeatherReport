package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName



data class PlaceResponse(val status: String,val geocodes:List<Geocode>)
data class Geocode(@SerializedName("formatted_address") val address:String,val adcode:String,val province:String,val location:String)

/**
 * {
 *   "status": "1",
 *   "info": "OK",
 *   "infocode": "10000",
 *   "count": "1",
 *   "geocodes": [
 *     {
 *       "formatted_address": "江西省南昌市",
 *       "country": "中国",
 *       "province": "江西省",
 *       "citycode": "0791",
 *       "city": "南昌市",
 *       "district": [],
 *       "township": [],
 *       "neighborhood": {
 *         "name": [],
 *         "type": []
 *       },
 *       "building": {
 *         "name": [],
 *         "type": []
 *       },
 *       "adcode": "360100",
 *       "street": [],
 *       "number": [],
 *       "location": "115.857972,28.682976",
 *       "level": "市"
 *     }
 *   ]
 * }
 * **/