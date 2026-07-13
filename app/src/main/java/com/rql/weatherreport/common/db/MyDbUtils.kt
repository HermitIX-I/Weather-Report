package com.rql.weatherreport.common.db

import android.database.sqlite.SQLiteDatabase
import com.rql.weatherreport.common.MyApplication


object MyDbUtils {

    private val myDbHelper = MyDbHelper(MyApplication.context,"test.db",4)
    fun getWriteDb(): SQLiteDatabase{
        return myDbHelper.writableDatabase
    }

    fun getReadDb(): SQLiteDatabase{
        return myDbHelper.readableDatabase
    }

    fun closeAll(){
        if(myDbHelper.readableDatabase.isOpen){
            myDbHelper.readableDatabase.close()
        }
        if(myDbHelper.writableDatabase.isOpen){
            myDbHelper.writableDatabase.close()
        }
    }
}