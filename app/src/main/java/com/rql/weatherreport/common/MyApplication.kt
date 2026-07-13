package com.rql.weatherreport.common

import android.app.Application
import android.content.Context
import com.rql.weatherreport.BuildConfig

class MyApplication : Application() {
    companion object {
        const val TOKEN_CAIYUN = BuildConfig.CAIYUN_TOKEN
        const val TOKEN_GAODE = BuildConfig.GAODE_TOKEN
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}