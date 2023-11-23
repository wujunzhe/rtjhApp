package com.example.rtjhapp.model

import androidx.databinding.BaseObservable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DebugItemViewModel(private val debugInfo: String) : BaseObservable() {

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    fun getDebugInfo(): String {
        val currentTime = Calendar.getInstance().time
        val formattedTime = timeFormat.format(currentTime)
        return "$formattedTime - $debugInfo"
    }
}