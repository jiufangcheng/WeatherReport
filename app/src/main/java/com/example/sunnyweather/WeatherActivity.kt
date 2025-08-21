package com.example.sunnyweather

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sunnyweather.SunnyWeatherApplication.Companion.context
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.example.sunnyweather.databinding.ForecastBinding
import com.example.sunnyweather.databinding.LifeIndexBinding
import com.example.sunnyweather.databinding.NowBinding
import com.example.sunnyweather.logic.Repository
import com.example.sunnyweather.logic.enity.Place
import com.example.sunnyweather.logic.model.Weather
import com.example.sunnyweather.logic.model.getSky
import com.example.sunnyweather.ui.weather.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWeatherBinding

    val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.now.toolbar) // 在布局中添加Toolbar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //状态栏沉浸
        WindowCompat.setDecorFitsSystemWindows(window, false)

        //设置专栏栏和导航栏的底色，透明
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = Color.TRANSPARENT
        }




        if (viewModel.lngLat.isEmpty()){
            viewModel.lngLat=intent.getStringExtra("location")?:""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName=intent.getStringExtra("place_name")?:""
        }

        viewModel.weatherLiveData.observe(this, Observer {result->
            val weather=result.getOrNull()
            if (weather!=null){
                showWeatherInfo(weather)
            }else{
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.swipeRefresh.isRefreshing=false
        })
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        binding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        binding.now.navBtn.setOnClickListener{
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
            }

            override fun onDrawerClosed(drawerView: View) {
                val manager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)//，当滑动菜单被隐藏 的时候，同时也要隐藏输入法
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })

    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.lngLat)
        binding.swipeRefresh.isRefreshing=true
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.now.placeName.text=viewModel.placeName
        val realtime=weather.realtime
        val daily=weather.daily
        //now

        binding.now.currentTemp.text="${realtime.temperature.toInt()}"
        binding.now.currentSky.text= getSky(realtime.skycon).info
        binding.now.currentAQI.text="空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)

        //forecast
        binding.forecast.forecastLayout.removeAllViews()
        val days=daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)

        }
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id) {
            R.id.collection -> {
                val place= Place(name = viewModel.placeName, location = viewModel.lngLat)
                // 收藏当前地点
                lifecycleScope.launch(Dispatchers.IO) {
                    val temp=Repository.queryPlace(viewModel.placeName)
                    if (temp==null){
                        Repository.addPlace(place)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(SunnyWeatherApplication.context, "已收藏", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "已经存在", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

                    return true

            }
            R.id.collectionManager -> {
                startActivity(Intent(this, CollectionManagerActivity::class.java))
                return true
            }
            R.id.setting -> {
                // 跳转设置页
                //startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}