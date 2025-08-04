package com.example.sunnyweather.logic.model

import com.google.gson.annotations.SerializedName

data class RealtimeResponse(val status: String, val result: Result) {

    data class Result(val realtime: Realtime)

    data class Realtime( val temperature: Float,val skycon: String,
                        @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val aqi: AQI)

    data class AQI(val chn: Float)

}


/**
 * {
 *   "status": "ok",
 *   "api_version": "v2.6",
 *   "api_status": "active",
 *   "lang": "zh_CN",
 *   "unit": "metric",
 *   "tzshift": 28800,
 *   "timezone": "Asia/Shanghai",
 *   "server_time": 1754212792,
 *   "location": [39.2072, 101.6656],
 *   "result": {
 *     "realtime": {
 *       "status": "ok",
 *       "temperature": 24.57,
 *       "humidity": 0.71,
 *       "cloudrate": 1,
 *       "skycon": "CLOUDY",
 *       "visibility": 19.08,
 *       "dswrf": 100.5,
 *       "wind": {
 *         "speed": 24.58,
 *         "direction": 322.86
 *       },
 *       "pressure": 83768.02,
 *       "apparent_temperature": 22.8,
 *       "precipitation": {
 *         "local": {
 *           "status": "ok",
 *           "datasource": "radar",
 *           "intensity": 0
 *         },
 *         "nearest": {
 *           "status": "ok",
 *           "distance": 26.68,
 *           "intensity": 0.1875
 *         }
 *       },
 *       "air_quality": {
 *         "pm25": 6,
 *         "pm10": 7,
 *         "o3": 84,
 *         "so2": 6,
 *         "no2": 9,
 *         "co": 0.7,
 *         "aqi": {
 *           "chn": 27,
 *           "usa": 67
 *         },
 *         "description": {
 *           "chn": "优",
 *           "usa": "良"
 *         }
 *       },
 *       "life_index": {
 *         "ultraviolet": {
 *           "index": 2,
 *           "desc": "很弱"
 *         },
 *         "comfort": {
 *           "index": 4,
 *           "desc": "温暖"
 *         }
 *       }
 *     },
 *     "primary": 0
 *   }
 * }
 * **/