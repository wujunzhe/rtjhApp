package com.example.rtjhapp.adapter

import com.example.rtjhapp.databinding.SmartModeSettingsDialogBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager

class SmartSettingsAdapter(private val binding : SmartModeSettingsDialogBinding) {
    private val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)

    fun initSettings() {
        val localDisinfectTime = sharedPreferencesManager.readString(
            Constants.SmartSettings.disinfectTime,
            Constants.SmartSettings.Default.disinfectTime
        )
        binding.disinfectTime.setText(localDisinfectTime)

        val localPowerOnDisinfect = sharedPreferencesManager.readBoolean(
            Constants.SmartSettings.powerOnDisinfect,
            Constants.SmartSettings.Default.powerOnDisinfect
        )
        binding.powerOnDisinfectSwitch.isChecked = localPowerOnDisinfect

        val localLightDelayTime = sharedPreferencesManager.readString(
            Constants.SmartSettings.lightDelayTime,
            Constants.SmartSettings.Default.lightDelayTime
        )
        binding.lightsDelayTime.setText(localLightDelayTime)
    }

    fun setSaveButtonClickListener(listener : () -> Unit) {
        binding.displaySettingSaveBtn.setOnClickListener {
            listener.invoke()
        }
    }

    fun saveSettings() {
        val disinfectTime = binding.disinfectTime.text.toString()
        sharedPreferencesManager.writeString(Constants.SmartSettings.disinfectTime, disinfectTime)

        val powerOnDisinfect = binding.powerOnDisinfectSwitch.isChecked
        sharedPreferencesManager.writeBoolean(
            Constants.SmartSettings.powerOnDisinfect,
            powerOnDisinfect
        )

        val lightDelayTime = binding.lightsDelayTime.text.toString()
        sharedPreferencesManager.writeString(Constants.SmartSettings.lightDelayTime, lightDelayTime)
    }
}