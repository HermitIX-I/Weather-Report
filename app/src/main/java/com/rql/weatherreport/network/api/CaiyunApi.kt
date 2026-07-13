package com.rql.weatherreport.network.api


import com.rql.weatherreport.common.MyApplication
import com.rql.weatherreport.network.dto.PlaceResponse
import com.rql.weatherreport.network.dto.DailyResponse
import com.rql.weatherreport.network.dto.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CaiyunApi {

    @GET("v2/place?token=${MyApplication.TOKEN_CAIYUN}")
    fun searchPlaces(
        @Query("query") query: String,
        @Query("lang") lang: String
    ): Call<PlaceResponse>

    @GET("v2.5/${MyApplication.TOKEN_CAIYUN}/{lng},{lat}/realtime.json")
    fun getRealtime(@Path("lng") lng: String,
                    @Path("lat") lat: String,
                    @Query("lang") lang: String): Call<RealtimeResponse>

    @GET("v2.5/${MyApplication.TOKEN_CAIYUN}/{lng},{lat}/daily.json")
    fun getDaily(
        @Path("lng") lng: String,
        @Path("lat") lat: String,
        @Query("lang") lang: String
    ): Call<DailyResponse>
}