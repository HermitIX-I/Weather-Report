package com.rql.weatherreport.network.api

import com.rql.weatherreport.common.MyApplication
import com.rql.weatherreport.network.dto.GaodeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GaodeApi {

    @GET("v3/geocode/regeo")
    fun getAddressByLocation(@Query("location") locationstr:String, @Query("key") key:String = "${MyApplication.TOKEN_GAODE}"): Call<GaodeResponse>
}