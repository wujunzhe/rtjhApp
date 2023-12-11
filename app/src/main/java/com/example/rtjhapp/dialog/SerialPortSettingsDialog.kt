package com.example.rtjhapp.dialog

import android.content.Context
import android.view.LayoutInflater
import com.example.rtjhapp.adapter.SerialPortSettingsAdapter
import com.example.rtjhapp.databinding.SerialPortSettingsDialogBinding
import com.example.rtjhapp.utils.MyToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SerialPortSettingsDialog(context : Context) {
    private val binding = SerialPortSettingsDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .create()

    private val adapter = SerialPortSettingsAdapter(binding)

    init {
        dialog.setView(binding.root)
        adapter.initSettings()
        adapter.setSaveButtonClickListener { saveSerialPortVal() }
    }

    fun show() {
        dialog.show()
    }

    private fun saveSerialPortVal() {
        adapter.saveSerialPortVal()
        MyToast().success(binding.root.context, "保存成功")
        dialog.dismiss()
    }
}