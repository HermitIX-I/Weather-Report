package com.rql.weatherreport.network.dto

import com.google.gson.annotations.SerializedName

data class GaodeResponse(
    val info:String,
    val regeocode: Regeocode
)

data class Regeocode(
    @SerializedName("formatted_address")
    val address:String
)