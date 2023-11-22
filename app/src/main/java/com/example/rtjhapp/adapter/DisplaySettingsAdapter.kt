package com.example.rtjhapp.adapter

import com.example.rtjhapp.databinding.DisplaySettingsDialogBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager

class DisplaySettingsAdapter(private val binding:DisplaySettingsDialogBinding) {
    private val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
    fun initSettings(){

        val localDutyDisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.duty,true)
        binding.dutyDisplaySwitch.isChecked = localDutyDisplayState

        val localNeGeStressToggleDisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.neStress,true)
        binding.negeToggleDisplaySwitch.isChecked = localNeGeStressToggleDisplayState

        val localDifferentGaugeDisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.differentGauge,true)
        binding.yachaDisplaySwitch.isChecked = localDifferentGaugeDisplayState

        val localSpare1DisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.spare1,false)
        binding.spare1DisplaySwitch.isChecked = localSpare1DisplayState

        val localSpare2DisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.spare2,false)
        binding.spare2DisplaySwitch.isChecked = localSpare2DisplayState

        val localSpare3DisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.spare3,false)
        binding.spare3DisplaySwitch.isChecked = localSpare3DisplayState

        val localSpare4DisplayState = sharedPreferencesManager.readBoolean(Constants.DisplayState.spare4,false)
        binding.spare4DisplaySwitch.isChecked = localSpare4DisplayState
    }

    fun setSaveButtonClickListener(listener:() -> Unit){
        binding.displaySettingSaveBtn.setOnClickListener { listener.invoke()
        }
    }

    fun saveSettings(){
        val dutyDisplayState = binding.dutyDisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.duty,dutyDisplayState)

        val neGeStressDisplayState = binding.negeToggleDisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.neStress,neGeStressDisplayState)

        val differentGaugeDisplayState = binding.yachaDisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.differentGauge,differentGaugeDisplayState)

        val spare1DisplayState = binding.spare1DisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.spare1,spare1DisplayState)

        val spare2DisplayState = binding.spare2DisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.spare2,spare2DisplayState)

        val spare3DisplayState = binding.spare3DisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.spare3,spare3DisplayState)

        val spare4DisplayState = binding.spare4DisplaySwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.DisplayState.spare4,spare4DisplayState)
    }
}