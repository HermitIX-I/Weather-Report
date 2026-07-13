package com.rql.weatherreport.network.repository

import android.util.Log
import com.rql.weatherreport.R
import com.rql.weatherreport.common.BaseActivity
import com.rql.weatherreport.common.MyApplication
import com.rql.weatherreport.common.RetrofitManager
import com.rql.weatherreport.network.dto.PlaceResponse
import com.rql.weatherreport.network.dto.DailyResponse
import com.rql.weatherreport.network.dto.RealtimeResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CaiyunRepository {

    private val caiyunApi = RetrofitManager.caiyunApi

    // 根据关键字查询地址的集合
    fun searchPlaces(query: String,callback:(PlaceResponse)-> Unit){
        val call = caiyunApi.searchPlaces(query, MyApplication.context.resources.getString(R.string.lang))
        call.enqueue(object: Callback<PlaceResponse> {
            override fun onResponse(
                call: Call<PlaceResponse?>,
                response: Response<PlaceResponse?>
            ) {
                callback(response.body()?: PlaceResponse("ng", emptyList()))
            }

            override fun onFailure(
                call: Call<PlaceResponse?>,
                t: Throwable
            ) {
                Log.e(BaseActivity.Companion.ALL_TAG, "onFailure: ${t.message}", t)
                callback(PlaceResponse("ng", emptyList()))
            }

        })

    }

    // // 根据精度和维度 查询实时天气
// 1. 获取实时天气（修复 Callback 泛型 + 确保 lang 参数正确）
    fun getRealtime(
        lng: String,
        lat: String,
        lang: String = "zh_CN", // 默认中文，可外部传入覆盖
        callback: (RealtimeResponse) -> Unit
    ) {
        val call = caiyunApi.getRealtime(lng, lat, lang)
        Log.i("CaiyunRepository", "实时天气请求 URL：${call.request().url}")
        // 修复：Callback 泛型与接口一致，去掉 ?
        call.enqueue(object : Callback<RealtimeResponse> {
            override fun onResponse(
                call: Call<RealtimeResponse>,
                response: Response<RealtimeResponse>
            ) {
                // 传入非空对象（避免后续空指针，补充 message 字段）
                val responseData = response.body() ?: RealtimeResponse(
                    status = "ng",
                    result = null,
                    message = "接口返回空数据" // 补充错误信息，方便日志排查
                )
                callback(responseData)
            }

            override fun onFailure(
                call: Call<RealtimeResponse>,
                t: Throwable
            ) {
                Log.e(BaseActivity.Companion.ALL_TAG, "实时天气请求失败：${t.message}", t)
                // 失败时传入明确的错误状态和信息
                callback(RealtimeResponse(
                    status = "ng",
                    result = null,
                    message = t.message ?: "网络异常（如无网、超时）"
                ))
            }
        })
    }

    // 2. 获取未来天气（统一 lang 参数 + 修复 Callback 泛型）
    fun getDaily(
        lng: String,
        lat: String,
        lang: String = "zh_CN", // 统一用默认参数，与 getRealtime 保持一致
        callback: (DailyResponse) -> Unit
    ) {
        // 修复：使用方法参数 lang，而非单独引用资源（避免参数不一致）
        val call = caiyunApi.getDaily(lng, lat,MyApplication.context.resources.getString(R.string.lang))
        // 修复：Callback 泛型与接口一致，去掉 ?
        call.enqueue(object : Callback<DailyResponse> {
            override fun onResponse(
                call: Call<DailyResponse>,
                response: Response<DailyResponse>
            ) {
                val responseData = response.body() ?: DailyResponse(
                    status = "ng",
                    result = null,
                    message = "接口返回空数据" // 补充错误信息
                )
                callback(responseData)
            }

            override fun onFailure(
                call: Call<DailyResponse>,
                t: Throwable
            ) {
                Log.e(BaseActivity.Companion.ALL_TAG, "未来天气请求失败：${t.message}", t)
                callback(DailyResponse(
                    status = "ng",
                    result = null,
                    message = t.message ?: "网络异常（如无网、超时）"
                ))
            }
        })
    }
}