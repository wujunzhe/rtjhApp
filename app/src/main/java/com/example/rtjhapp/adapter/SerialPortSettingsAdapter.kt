package com.example.rtjhapp.adapter

import com.example.rtjhapp.databinding.SerialPortSettingsDialogBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager

class SerialPortSettingsAdapter(private val binding: SerialPortSettingsDialogBinding) {
    fun initSettings(){

        // SharedPreferences来读取和存储数据
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        // 读取本地串口值赋值给输入框
        val localCallSerialPort = sharedPreferencesManager.readString(Constants.SerialPort.call,Constants.SerialPort.Default.call)
        binding.callSerialPort.setText(localCallSerialPort)

        val localAirConditionSerialPort = sharedPreferencesManager.readString(Constants.SerialPort.airCondition,Constants.SerialPort.Default.airCondition)
        binding.airConditionSerialPort.setText(localAirConditionSerialPort)

        val localControlSerialPort = sharedPreferencesManager.readString(Constants.SerialPort.control,Constants.SerialPort.Default.control)
        binding.controlSerialPort.setText(localControlSerialPort)

        val localMusicSerialPort = sharedPreferencesManager.readString(Constants.SerialPort.music,Constants.SerialPort.Default.music)
        binding.pronunciationSerialPort.setText(localMusicSerialPort)

    }

    fun setSaveButtonClickListener(listener: () -> Unit){
        binding.serialPortSettingSaveBtn.setOnClickListener { listener.invoke() }
    }

    fun saveSerialPortVal() {
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)

        val inputCallSerialPort = binding.callSerialPort.text.toString()
        sharedPreferencesManager.writeString(Constants.SerialPort.call, inputCallSerialPort)

        val inputAirConditionSerialPort = binding.airConditionSerialPort.text.toString()
        sharedPreferencesManager.writeString(Constants.SerialPort.airCondition, inputAirConditionSerialPort)

        val inputControlSerialPort = binding.controlSerialPort.text.toString()
        sharedPreferencesManager.writeString(Constants.SerialPort.control, inputControlSerialPort)

        val inputMusicSerialPort = binding.pronunciationSerialPort.text.toString()
        sharedPreferencesManager.writeString(Constants.SerialPort.music, inputMusicSerialPort)

    }
}