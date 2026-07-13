package com.rql.weatherreport.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.rql.weatherreport.network.dto.Place
import kotlin.jvm.java


// 地址持久化的工具类
object PlaceUtils {

    private val gson = Gson()

    private fun getSharedPrefences(): SharedPreferences{
        // 全局上下文
        return MyApplication.context.getSharedPreferences("place_info", Context.MODE_PRIVATE)
    }

    // 添加用户的最后的信息
    fun savePlace(place: Place){
        val json = gson.toJson(place)
        val edit = getSharedPrefences().edit()
        edit.putString("lastplace",json)
        edit.commit()
    }

    // 拿出用户保存的最后一个地址
    fun getLastPlace(): Place{
        val string = getSharedPrefences().getString("lastplace","")
        return gson.fromJson(string, Place::class.java)

    }

    // 用户是否保存了最后的地址
    fun isSaved():Boolean{
        return getSharedPrefences().contains("lastplace")
    }

}