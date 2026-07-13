package com.rql.weatherreport.network.repository

import android.util.Log
import com.rql.weatherreport.common.BaseActivity
import com.rql.weatherreport.common.RetrofitManager
import com.rql.weatherreport.network.dto.GaodeResponse
import com.rql.weatherreport.network.dto.Regeocode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GaodeRepository {

    private val gaodeApi = RetrofitManager.gaodeApi

    // 通过经纬度 反查 地址信息
    fun getAddressByLocation(lng: String, lat: String, callback: (GaodeResponse) -> Unit) {
        val call = gaodeApi.getAddressByLocation("${lng},${lat}")
        call.enqueue(object : Callback<GaodeResponse> {
            override fun onResponse(
                call: Call<GaodeResponse?>,
                response: Response<GaodeResponse?>
            ) {
                callback(response.body()?: GaodeResponse("NG",Regeocode("")))
            }

            override fun onFailure(
                call: Call<GaodeResponse?>,
                t: Throwable
            ) {
                Log.e(BaseActivity.ALL_TAG, "onFailure: ${t.message}", t)
                callback(GaodeResponse("NG", Regeocode("")))
            }

        })
    }
}