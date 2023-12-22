package com.example.rtjhapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.rtjhapp.model.PhoneItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context : Context) {

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences(
        "rtjhPreferences",
        Context.MODE_PRIVATE
    )

    val gson = Gson()

    /**
     * 写入String数据
     */
    fun writeString(key : String, value : String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    /**
     * 读取String类型数据，提供默认值
     */
    fun readString(key : String, defaultValue : String) : String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    /**
     * 写入布尔类型数据
     */
    fun writeBoolean(key : String, value : Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    /**
     * 读取布尔类型数据，提供默认值
     */
    fun readBoolean(key : String, defaultValue : Boolean) : Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    /**
     * 写入专呼数据
     */
    @SuppressLint("CommitPrefEdits")
    fun saveSpecialCallList(specialCallList: List<PhoneItem>) {
        val serializedList = gson.toJson(specialCallList)
        sharedPreferences.edit().putString("SpecialCallList",serializedList)
    }

    /**
     * 读取专呼数据
     */
    fun readSpecialCallList(): List<PhoneItem> {
        val serializedList = sharedPreferences.getString("SpecialCallList",null)
        return if (serializedList != null) {
            val typeToken = object : TypeToken<List<PhoneItem>> (){}.type
            gson.fromJson(serializedList,typeToken)
        } else {
            emptyList()
        }
    }
}