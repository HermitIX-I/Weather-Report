package com.rql.weatherreport.common

import android.content.Context
import android.location.Location
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class LocationHelper(val context: Context) {
    // 获取FusedLocationProviderClient实例
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * 获取设备最后已知的位置
     * 这是一个异步操作，返回一个Task对象
     */
    fun getLastKnownLocation(): Task<Location> {
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PermissionChecker.PERMISSION_GRANTED

        // 如果没有位置权限，返回一个失败的Task
        if (!hasFineLocationPermission && !hasCoarseLocationPermission) {
            return Tasks.forException(
                SecurityException("Location permission not granted")
            )
        }
        return fusedLocationClient.lastLocation
    }
}
