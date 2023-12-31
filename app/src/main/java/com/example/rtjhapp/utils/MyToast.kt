package com.example.rtjhapp.utils

import android.content.Context
import android.graphics.Color
import com.example.rtjhapp.R.color
import io.github.muddz.styleabletoast.StyleableToast

class MyToast {
    fun success(context : Context, text : String) {
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.successToastColor))
            .show()
    }

    fun error(context : Context, text : String) {
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.errorToastColor))
            .show()
    }

    fun info(context : Context, text : String) {
        StyleableToast.Builder(context)
            .text(text)
            .textColor(Color.WHITE)
            .backgroundColor(context.getColor(color.infoColor))
            .show()
    }
}