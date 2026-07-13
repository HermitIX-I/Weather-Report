package com.rql.weatherreport.ui.activity

import android.os.Bundle
import android.util.Log
import com.rql.weatherreport.common.BaseActivity
import com.rql.weatherreport.common.PlaceUtils
import com.rql.weatherreport.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // 判断用户是否保存过最后的地址
        if(PlaceUtils.isSaved()){
            val lastPlace = PlaceUtils.getLastPlace()
            val lng = lastPlace.location.lng
            val lat = lastPlace.location.lat
            val placeName = lastPlace.name
            WeatherActivity.runActivity(this,lng,lat,placeName)
            this.finish()
        }

    }
}