package com.example.rtjhapp.utils

import android.view.View

class DebounceClickListener(
    private val clickListener : View.OnClickListener,
    private val debounceInterval : Long
) : View.OnClickListener {
    private var lastClickTime : Long = 0

    override fun onClick(v : View?) {
        val currentTIme = System.currentTimeMillis()
        if (currentTIme - lastClickTime >= debounceInterval) {
            lastClickTime = currentTIme
            clickListener.onClick(v)
        }
    }
}