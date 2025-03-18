package com.sunnyweather.android.ui.weather

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding

    private val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 透明状态栏
        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                Log.d("MyDebug", "返回天气信息：$weather")
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            binding.root.isRefreshing = false
        })
        binding.root.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        binding.root.setOnRefreshListener {
            refreshWeather()
        }
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        binding.root.isRefreshing = true
    }

    /**
     * 解析展示服务器返回的天气信息
     */
    private fun showWeatherInfo(weather: Weather) {
        // 填充now.xml布局数据
        binding.now.placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        val currentTempText = "${realtime.temperature.toInt()} °C"
        binding.now.currentTemp.text = currentTempText
        val realtimeSky = getSky(realtime.skycon)
        binding.now.currentSky.text = realtimeSky.info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        binding.now.currentAQI.text = currentPM25Text
        binding.now.nowLayout.setBackgroundResource(realtimeSky.bg)

        // 填充forecast.xml布局数据
        binding.forecast.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        // 循环动态加载forecast_item.xml布局并设置相应的数据，再添加到父布局中
        for (i in 0 until days) {
            val skyCon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this)
                .inflate(R.layout.forecast_item, binding.forecast.forecastLayout, false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(
                skyCon.date
            )
            val sky = getSky(skyCon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} °C"
            temperatureInfo.text = tempText
            binding.forecast.forecastLayout.addView(view)
        }
        // 填充life_index.xml布局数据，生活指数只取当天数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndex.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndex.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndex.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndex.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE
    }
}