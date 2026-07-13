package com.rql.weatherreport.common

import android.app.Activity
import android.util.Log
import com.rql.weatherreport.common.db.MyDbUtils

object ActivityMangeUtils{

    private var activityList = ArrayList<Activity>()

    fun addActivity(activity: Activity){
        this.activityList.add(activity)
    }

    fun removeActivity(activity: Activity){
        if(this.activityList.contains(activity)){
            this.activityList.remove(activity)
        }
    }

    fun print(): String{
        return this.activityList.toString()
    }

    fun finishAll(){
        this.activityList.forEach {
            it.finish()
        }
        activityList.clear()
        MyDbUtils.closeAll()
    }
}