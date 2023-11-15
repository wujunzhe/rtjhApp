package com.example.rtjhapp.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.SerialPortSettingsDialogBinding
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.muddz.styleabletoast.StyleableToast

class SerialPortSettingsDialog(context : Context){
    private val binding = SerialPortSettingsDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .setTitle("串口设置")
        .create()

    init {
        dialog.setView(binding.root)

        // SharedPreferences来读取和存储数据
        val sharedPreferencesManager = SharedPreferencesManager(context)
        // 读取本地串口值赋值给输入框
        val localCallSerialPort = sharedPreferencesManager.readString("CallSerialPort","/dev/ttyS9")
         binding.callSerialPort.setText(localCallSerialPort)

        val localAirConditionSerialPort = sharedPreferencesManager.readString("AirConditionSerialPort","/dev/ttyS2")
        binding.airConditionSerialPort.setText(localAirConditionSerialPort)

        val localControlSerialPort = sharedPreferencesManager.readString("ControlSerialPort","/dev/ttyS0")
        binding.controlSerialPort.setText(localControlSerialPort)

        val localMusicSerialPort = sharedPreferencesManager.readString("MusicSerialPort","/dev/ttyS5")
        binding.pronunciationSerialPort.setText(localMusicSerialPort)

        // 监听保存按钮事件
        val saveBtn = binding.saveBtn
        saveBtn.setOnClickListener {
            saveSerialPortVal(binding,sharedPreferencesManager)
        }
    }

    fun show() {
        dialog.show()
    }

    @SuppressLint("ResourceAsColor")
    private fun saveSerialPortVal(binding:SerialPortSettingsDialogBinding, sharedPreferencesManager : SharedPreferencesManager){
        val inputCallSerialPort = binding.callSerialPort.text.toString()
        sharedPreferencesManager.writeString("CallSerialPort", inputCallSerialPort)

        val inputAirConditionSerialPort = binding.airConditionSerialPort.text.toString()
        sharedPreferencesManager.writeString("AirConditionSerialPort",inputAirConditionSerialPort)

        val inputControlSerialPort = binding.controlSerialPort.text.toString()
        sharedPreferencesManager.writeString("ControlSerialPort",inputControlSerialPort)

        val inputMusicSerialPort = binding.pronunciationSerialPort.text.toString()
        sharedPreferencesManager.writeString("MusicSerialPort",inputMusicSerialPort)

        MyToast(binding.root.context).success(binding.root.context)

        dialog.dismiss()
    }
}