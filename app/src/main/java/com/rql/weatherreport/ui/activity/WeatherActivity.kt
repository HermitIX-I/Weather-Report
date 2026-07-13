package com.rql.weatherreport.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.rql.weatherreport.R
import com.rql.weatherreport.common.BaseActivity
import com.rql.weatherreport.common.getSky
import com.rql.weatherreport.databinding.ActivityWeatherBinding
import com.rql.weatherreport.ui.viewmodel.WeatherActivityViewModel
import java.text.SimpleDateFormat
import kotlin.text.toInt



class WeatherActivity : BaseActivity() {

    companion object{
        fun runActivity(context: Context,lng:String,lat:String,placeName:String){
            val intent = Intent(context,WeatherActivity::class.java)
            intent.putExtra("lng",lng)
            intent.putExtra("lat",lat)
            intent.putExtra("placeName",placeName)
            context.startActivity(intent)
        }
    }
    lateinit var viewBinding: ActivityWeatherBinding
    lateinit var lng:String
    lateinit var lat:String
    lateinit var placeName:String

    val viewModel: WeatherActivityViewModel by lazy {
        ViewModelProvider(this).get(WeatherActivityViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        // 获得上一个页面传来的意图
        lng = this.intent.getStringExtra("lng")?:""
        lat = this.intent.getStringExtra("lat")?:""
        placeName = this.intent.getStringExtra("placeName")?:""
        Log.i(ALL_TAG, "lng=$lng , lat=$lat, placeName=$placeName ")


        // 1 用上个页面传来的精度 和 维度 改变 viewmodel 中location
//        viewModel.changeLocation(lng,lat)
        if (lng.isNotEmpty() && lat.isNotEmpty()) {
            viewModel.fetchWeatherData(lng, lat) // 新增：直接调用请求方法（需在 ViewModel 中定义）
        }
        // 2 注册观察
        initObsever()
        // 3 注册事件
        initEvent()
    }
    private fun initEvent() {
        // 下拉刷新事件
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.changeLocation(lng,lat)
        }

        // 导航图标被点击事件  打开侧滑抽屉
        viewBinding.nowweather.navBtn.setOnClickListener {
            viewBinding.drawerlayout.openDrawer(GravityCompat.START,true)
        }
    }
    private fun initObsever() {
        // valstring = this.resources.getString(R.string.aqi)
        // 观察实时数据
        viewModel.realtimeResult.observe(this){realtimeResult ->
            viewBinding.nowweather.placeName.text = placeName
// 填充now.xml布局中的数据
            val currentTempText = "${realtimeResult?.realtime?.temperature?.toInt()} ℃"
            viewBinding.nowweather.currentTemp.text = currentTempText
            viewBinding.nowweather.currentSky.text =
                getSky(realtimeResult?.realtime?.skycon ?: "").info
            val currentPM25Text =
                "${resources.getString(R.string.aqi)} ${realtimeResult?.realtime?.airQuality?.aqi?.chn?.toInt() ?: 0}"
            viewBinding.nowweather.currentAQI.text = currentPM25Text
            viewBinding.nowweather.nowLayout.setBackgroundResource(
                getSky(
                    realtimeResult?.realtime?.skycon ?: ""
                ).bg
            )
        }

        viewModel.dailyResult.observe(this) { dailyResult ->
            viewBinding.forecastweather.forecastLayout.removeAllViews()
            val days = dailyResult?.daily?.skycon?.size ?: 0
            for (i in 0 until days) {
                val skycon = dailyResult?.daily?.skycon[i]
                val temperature = dailyResult?.daily?.temperature[i]
                val view = LayoutInflater.from(this).inflate(
                    R.layout.forecast_item,
                    viewBinding.forecastweather.forecastLayout, false
                )
                val dateInfo = view.findViewById(R.id.dateInfo) as TextView
                val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
                val skyInfo = view.findViewById(R.id.skyInfo) as TextView
                val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                dateInfo.text = simpleDateFormat.format(skycon?.date)
                val sky = getSky(skycon?.value ?: "")
                skyIcon.setImageResource(sky.icon)
                skyInfo.text = sky.info
                val tempText = "${temperature?.min?.toInt() ?: 0} ~ ${temperature?.max?.toInt()} ℃"
                temperatureInfo.text = tempText
                viewBinding.forecastweather.forecastLayout.addView(view)
            }
// 填充life_index.xml布局中的数据
            val lifeIndex = dailyResult?.daily?.lifeIndex
            viewBinding.lifeIndexweather.coldRiskText.text = lifeIndex?.coldRisk[0]?.desc
            viewBinding.lifeIndexweather.dressingText.text = lifeIndex?.dressing[0]?.desc
            viewBinding.lifeIndexweather.ultravioletText.text = lifeIndex?.ultraviolet[0]?.desc
            viewBinding.lifeIndexweather.carWashingText.text = lifeIndex?.carWashing[0]?.desc

            viewBinding.swipeRefreshLayout.isRefreshing = false

        }

    }

}