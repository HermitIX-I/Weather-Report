package com.rql.weatherreport.network.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

// 未来天气 包括 生活指数
data class DailyResponse (
    val status :String,
    val result: DailyResult?,
    val message: String? = null
)

data class DailyResult(val daily: Daily)
data class Daily(val temperature: List<Temperature>, val skycon: List<Skycon>, @SerializedName("life_index") val lifeIndex: LifeIndex)

data class Temperature(val max: Float, val min: Float)

data class Skycon(val value: String, val date: Date)

// 生活指数
data class LifeIndex(val coldRisk: List<LifeDescription>, val carWashing: List<LifeDescription>, val ultraviolet: List<LifeDescription>, val dressing: List<LifeDescription>)

// 生活描述对象
data class LifeDescription(val desc: String)