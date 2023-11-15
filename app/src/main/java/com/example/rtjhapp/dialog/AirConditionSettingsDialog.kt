package com.example.rtjhapp.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.rtjhapp.databinding.AirConditionSettingsDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AirConditionSettingsDialog(context : Context) {
    private val binding = AirConditionSettingsDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .setTitle("空调设置")
        .create()

    init {
        dialog.setView(binding.root)
    }
    fun show() {
        dialog.show()
    }
}