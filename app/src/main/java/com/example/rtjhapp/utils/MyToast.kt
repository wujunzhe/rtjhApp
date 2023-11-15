package com.example.rtjhapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.rtjhapp.R
import com.example.rtjhapp.R.*
import io.github.muddz.styleabletoast.StyleableToast

class MyToast(context : Context) {
    fun success(context : Context){
        StyleableToast.Builder(context)
            .text("保存成功")
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.successToastColor))
            .show()
    }
}