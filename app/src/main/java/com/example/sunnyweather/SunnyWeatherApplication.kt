package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
//经过这样的配置之后，我们就可以在项目的任何位置通过调用 SunnyWeatherApplication.context来获取Context对象了，非常便利
class SunnyWeatherApplication:Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        const val TOKEN="3sQAmAkIlCI27PHH"
        const val KEY="8ea20ca9d04c8f5bdce4258b5b4dd76e"
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}