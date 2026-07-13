package com.rql.weatherreport.common

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

// 成为我的项目中所有activity 的父类
open class BaseActivity : AppCompatActivity() {

    companion object{
        const val ALL_TAG:String = "weatherreport"
        fun showMessageLong(context: Context,msg:String){
            Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
        }
        fun showMessageShort(context: Context,msg:String){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }

        fun showAlterDialog(context: Context,title:String,message:String){
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ok",null)
                .show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMangeUtils.addActivity(this)

    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityMangeUtils.removeActivity(this)
    }

    // 以后他的子类activity 一被激活 就先答应日志
    override fun onResume() {
        super.onResume()
        Log.i(ALL_TAG, "目前在 ${this.javaClass.name}")
    }

}