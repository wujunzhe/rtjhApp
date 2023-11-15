package com.example.rtjhapp.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context : Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "rtjhPreferences",
        Context.MODE_PRIVATE
    )

    /**
     * 写入String数据
     */
    fun writeString(key: String, value: String){
        val editor = sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    /**
     * 读取String类型数据，提供默认值
     */
    fun readString(key: String, defaultValue: String) : String? {
        return sharedPreferences.getString(key,defaultValue)
    }

    /**
     * 写入布尔类型数据
     */
    fun writeBoolean(key: String, value: Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    /**
     * 读取布尔类型数据，提供默认值
     */
    fun readBoolean(key: String, defaultValue:Boolean): Boolean {
        return sharedPreferences.getBoolean(key,defaultValue)
    }
}