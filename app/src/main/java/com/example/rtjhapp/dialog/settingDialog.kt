package com.example.rtjhapp.dialog

import android.annotation.SuppressLint
import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.customListAdapter
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.SettingOptionsAdapter

class SettingDialog(private val context : Context) {
    @SuppressLint("InflateParams", "ServiceCast")
    fun show() {
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