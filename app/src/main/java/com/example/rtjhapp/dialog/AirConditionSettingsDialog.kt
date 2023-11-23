package com.example.rtjhapp.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.AirConditionSettingsAdapter
import com.example.rtjhapp.databinding.AirConditionSettingsDialogBinding
import com.example.rtjhapp.utils.MyToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AirConditionSettingsDialog(context : Context) {
    private val binding = AirConditionSettingsDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .setBackground(ColorDrawable(ContextCompat.getColor(context,R.color.white)))
        .create()

    private val adapter = AirConditionSettingsAdapter(binding)
    init {
        dialog.setView(binding.root)
        adapter.initSettings()
        adapter.setSaveButtonClickListener { saveSettings() }
    }
    fun show() {
        dialog.show()
    }

    private fun saveSettings(){
        adapter.saveSettingVal()
        MyToast().success(binding.root.context)
        dialog.dismiss()
    }
}