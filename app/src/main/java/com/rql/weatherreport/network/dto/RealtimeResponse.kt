package com.rql.weatherreport.network.dto

import com.google.gson.annotations.SerializedName

// 从服务端返回的实时天气
data class RealtimeResponse(
    val status:String,
    val result: RealtimeResult?,

    val message: String? = null

)
data class RealtimeResult(val realtime: Realtime)
data class Realtime(val temperature: Float,val skycon:String,
                    @SerializedName("air_quality") val airQuality:AirQuality)
data class AirQuality(val aqi:Aqi)
data class Aqi(val chn:String)