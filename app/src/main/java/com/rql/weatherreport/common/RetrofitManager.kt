package com.rql.weatherreport.common

import com.google.gson.GsonBuilder
import com.rql.weatherreport.network.api.CaiyunApi
import com.rql.weatherreport.network.api.GaodeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitManager {

    // 基础URL
    private const val BASE_URL_CAIYUN = "https://api.caiyunapp.com/"
    private const val BASE_URL_GAODE = "https://restapi.amap.com/"

    // 自定义Gson（处理日期格式、字段映射等）
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create()

    // Retrofit实例
    private val retrofitCaiyun = Retrofit.Builder()
        .baseUrl(BASE_URL_CAIYUN)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    private val retrofitGaode = Retrofit.Builder()
        .baseUrl(BASE_URL_GAODE)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val caiyunApi: CaiyunApi by lazy {
        retrofitCaiyun.create(CaiyunApi::class.java)
    }

    val gaodeApi: GaodeApi by lazy {
        retrofitGaode.create(GaodeApi::class.java)
    }
}