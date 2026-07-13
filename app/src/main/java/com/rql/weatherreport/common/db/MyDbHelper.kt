package com.rql.weatherreport.common.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rql.weatherreport.common.BaseActivity

class MyDbHelper(val context: Context, dbname:String, version:Int)
    : SQLiteOpenHelper(context,dbname,null,version) {

    private val createAccountBook = "create table tb_account_book (" +
            " id integer primary key autoincrement," +
            "categray_id int," +
            "categray_name text," +
            "money real," +
            "comment text," +
            "create_time text)"

    private val createCategory = "create table tb_category (" +
            " id integer primary key autoincrement," +
            "categray_name text)"

        // 第一次使用数据库的时候 会先调用这个方法创建一个数据库
    override fun onCreate(db: SQLiteDatabase?) {
            BaseActivity.showMessageLong(context,"数据库创建了")
            db?.execSQL(createAccountBook)
            db?.execSQL(createCategory)
            db?.execSQL("insert into tb_category(categray_name) values('学习')")
            db?.execSQL("insert into tb_category(categray_name) values('工作')")
            db?.execSQL("insert into tb_category(categray_name) values('生活')")
            db?.execSQL("insert into tb_category(categray_name) values('娱乐')")
            db?.execSQL("insert into tb_category(categray_name) values('社交')")
            db?.execSQL("insert into tb_category(categray_name) values('谈爱')")
    }


    // 升级的时候调用这个方法 升级数据库
    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }
}