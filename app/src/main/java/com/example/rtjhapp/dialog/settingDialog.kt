package com.example.rtjhapp.dialog

import android.annotation.SuppressLint
import com.example.rtjhapp.adapter.SettingOptionsAdapter
import android.content.Context
import android.view.LayoutInflater
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.list.customListAdapter

import com.example.rtjhapp.R

class SettingDialog(private val context : Context) {
    @SuppressLint("InflateParams")
    fun show(){
        val options = context.resources.getStringArray(R.array.setting_options)
        val iconFontCodes = context.resources.getStringArray(R.array.icon_font_codes)
        
       MaterialDialog(context)
            .customListAdapter(
            SettingOptionsAdapter(
                options,
                iconFontCodes
            )
        )
           .cornerRadius(16f)
            .show()
    }
}