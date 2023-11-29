package com.example.rtjhapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.rtjhapp.R
import com.example.rtjhapp.R.*
import io.github.muddz.styleabletoast.StyleableToast

class MyToast() {
    fun success(context : Context,text : String){
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.successToastColor))
            .show()
    }

    fun error(context : Context,text:String){
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.errorToastColor))
            .show()
    }

    fun info(context : Context,text : String) {
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.infoColor))
            .show()
    }
}