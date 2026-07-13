package com.zoukx.hznewworkdemo04.common

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val status:String,
    @SerializedName(value = "data", alternate = arrayOf("result", "places"))
    val data:T?
)