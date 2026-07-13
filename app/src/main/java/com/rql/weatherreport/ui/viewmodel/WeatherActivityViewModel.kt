package com.rql.weatherreport.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

import com.rql.weatherreport.network.dto.Location
import com.rql.weatherreport.network.dto.DailyResult
import com.rql.weatherreport.network.dto.RealtimeResult
import com.rql.weatherreport.network.repository.CaiyunRepository


class WeatherActivityViewModel: ViewModel() {

    private val caiyunRepository = CaiyunRepository()

    private var _location = MutableLiveData<Location>()
    // 实时天气
    val realtimeResult: LiveData<RealtimeResult> = _location.switchMap { location ->
        var result = MutableLiveData<RealtimeResult>()
        // 根据坐标的改变要去查
        caiyunRepository.getRealtime(location.lng,location.lat){realtimeResponse ->
            Log.i("WeatherViewModel", "实时天气请求回调：status=${realtimeResponse.status}, message=${realtimeResponse.message}")
            if(realtimeResponse.status=="ok"){
                result.postValue(realtimeResponse.result)
            }else{
                Log.e("WeatherViewModel", "实时天气请求失败：${realtimeResponse.message}")
            }
        }
        result
    }
    // 未来天气
    val dailyResult: LiveData<DailyResult> = _location.switchMap { location ->
        var result = MutableLiveData<DailyResult>()
        // 根据坐标的改变要去查
        caiyunRepository.getDaily(location.lng,location.lat){dailyResponse ->
            if(dailyResponse.status=="ok"){
                result.postValue(dailyResponse.result)
            }
        }
        result
    }

    fun changeLocation(lng:String,lat:String){
        val tl = Location(lng=lng,lat=lat)
        _location.postValue(tl)
    }
    fun fetchWeatherData(lng: String, lat: String) {
        // 直接调用 changeLocation，触发 _location 变化，进而触发 switchMap 请求数据
        changeLocation(lng, lat)
        // 可选：若需要日志调试，可添加 Log（需先导入 android.util.Log）
        Log.i("WeatherViewModel", "触发数据请求：lng=$lng, lat=$lat")
    }

}