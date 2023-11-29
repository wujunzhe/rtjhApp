package com.example.rtjhapp.adapter

import com.example.rtjhapp.databinding.AirConditionSettingsDialogBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager

class AirConditionSettingsAdapter(private val binding: AirConditionSettingsDialogBinding) {
    fun initSettings(){
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)

        val localGetStatusOrder = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Order.getStatus,
            Constants.AirConditionSettings.Default.Order.getStatus
        )
        binding.readAirOrder.setText(localGetStatusOrder)

        // 开关机部分 start
        val localPowerOnOrder = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Order.powerOn,
            Constants.AirConditionSettings.Default.Order.powerOn
            )
        binding.powerOnOrder.setText(localPowerOnOrder)

        val localPowerOffOrder = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Order.powerOff,
            Constants.AirConditionSettings.Default.Order.powerOff
            )
        binding.powerOffOrder.setText(localPowerOffOrder)
        // 开关机部分 end

        // 回风部分 start
        val localReturnHumRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.returnHum,
            Constants.AirConditionSettings.Default.Register.returnHum
            )
        binding.returnHumRegNum.setText(localReturnHumRegNum)

        val localReturnHumNeedDivision = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.returnHum,
            true
            )
        binding.humEchoSwitch.isChecked = localReturnHumNeedDivision

        val localReturnTemRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.returnTem,
            Constants.AirConditionSettings.Default.Register.returnTem
            )
        binding.returnTemRegNum.setText(localReturnTemRegNum)

        val localReturnTemNeedDivision = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.returnTem,
            true
            )
        binding.temEchoSwitch.isChecked = localReturnTemNeedDivision
        // 回风部分 end

        // 温湿度设置部分 start
        val localSettingHumRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.settingHum,
            Constants.AirConditionSettings.Default.Register.settingHum,
            )
        binding.settingHumRegNum.setText(localSettingHumRegNum)

        val localSettingHumMax = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Max.hum,
            Constants.AirConditionSettings.Default.Max.hum
        )
        binding.humMax.setText(localSettingHumMax)

        val localSettingHumMin = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Min.hum,
            Constants.AirConditionSettings.Default.Min.hum
        )
        binding.humMin.setText(localSettingHumMin)

        val localSettingHumNeedMulti = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.settingHum,
            true
            )
        binding.humSettingEchoSwitch.isChecked = localSettingHumNeedMulti

        val localSettingTemRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.settingTem,
            Constants.AirConditionSettings.Default.Register.settingTem
            )
        binding.settingTemRegNum.setText(localSettingTemRegNum)

        val localSettingTemMax = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Max.tem,
            Constants.AirConditionSettings.Default.Max.tem
        )
        binding.temMax.setText(localSettingTemMax)

        val localSettingTemMin = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Min.tem,
            Constants.AirConditionSettings.Default.Min.tem
        )
        binding.temMin.setText(localSettingTemMin)

        val localSettingTemNeedMulti = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.settingTem,
            true
            )
        binding.temSettingEchoSwitch.isChecked = localSettingTemNeedMulti
        // 温湿度设置部分 end

        // 其他设置部分 start
        val localRunningStateRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.runningState,
            Constants.AirConditionSettings.Default.Register.runningState
           )
        binding.runningStatusRegNum.setText(localRunningStateRegNum)

        val localUnitFailureRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.unitFailure,
            Constants.AirConditionSettings.Default.Register.unitFailure
            )
        binding.unitFailureRegNum.setText(localUnitFailureRegNum)

        val localDutyModeRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.dutyMode,
            Constants.AirConditionSettings.Default.Register.dutyMode
           )
        binding.dutyModeRegNum.setText(localDutyModeRegNum)

        val localNeGeStressToggleRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.neStressToggle,
            Constants.AirConditionSettings.Default.Register.neStressToggle
            )
        binding.stressToggleRegNum.setText(localNeGeStressToggleRegNum)

        val localSystemErrorRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.systemError,
            Constants.AirConditionSettings.Default.Register.systemError
        )
        binding.systemErrorReg.setText(localSystemErrorRegNum)

        val localRunningTimeRegNum = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.runningTime,
            Constants.AirConditionSettings.Default.Register.runningTime
           )
        binding.runningTimeRegNum.setText(localRunningTimeRegNum)
        // 其他设置部分 end

    }
    fun setSaveButtonClickListener(listener: () -> Unit){
        binding.airConditionSettingsSaveBtn.setOnClickListener { listener.invoke() }
    }

    fun saveSettingVal(){
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)

        val getStatusOrder = binding.readAirOrder.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Order.getStatus,getStatusOrder)

        // 开关机部分 start
        val powerOnOrder = binding.powerOnOrder.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Order.powerOn,powerOnOrder)

        val powerOffOrder = binding.powerOffOrder.text.toString()
       sharedPreferencesManager.writeString(Constants.AirConditionSettings.Order.powerOff,powerOffOrder)
        // 开关机部分 end

        // 回风部分 start
        val returnHumRegNum = binding.returnHumRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.returnHum,returnHumRegNum)

        val returnHumNeedDivision = binding.humEchoSwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.AirConditionSettings.NeedCalculator.returnHum,returnHumNeedDivision)

        val returnTemRegNum = binding.returnTemRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.returnTem,returnTemRegNum)

        val returnTemNeedDivision = binding.temEchoSwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.AirConditionSettings.NeedCalculator.returnTem,returnTemNeedDivision)
        // 回风部分 end

        // 温湿度设置部分 start
        val settingHumRegNum = binding.settingHumRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.settingHum,settingHumRegNum)

        val settingHumMax = binding.humMax.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Max.hum,settingHumMax)

        val settingHumMin = binding.humMin.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Min.hum,settingHumMin)

        val settingHumNeedMulti = binding.humSettingEchoSwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.AirConditionSettings.NeedCalculator.settingHum,settingHumNeedMulti)

        val settingTemRegNum = binding.settingTemRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.settingTem,settingTemRegNum)

        val settingTemMax = binding.temMax.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Max.tem,settingTemMax)

        val settingTemMin = binding.temMin.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Min.tem,settingTemMin)

        val settingTemNeedMulti = binding.temSettingEchoSwitch.isChecked
        sharedPreferencesManager.writeBoolean(Constants.AirConditionSettings.NeedCalculator.settingTem,settingTemNeedMulti)
        // 温湿度设置部分 end

        // 其他设置部分 start
        val runningStateRegNum = binding.runningStatusRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.runningState,runningStateRegNum)

        val unitFailureRegNum = binding.unitFailureRegNum.text.toString()
        sharedPreferencesManager.readString(Constants.AirConditionSettings.Register.unitFailure,unitFailureRegNum)

        val dutyModeRegNum = binding.dutyModeRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.dutyMode,dutyModeRegNum)

        val neGeStressToggleRegNum = binding.stressToggleRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.neStressToggle,neGeStressToggleRegNum)

        val systemErrorRegNum = binding.systemErrorReg.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.systemError,systemErrorRegNum)

        val runningTimeRegNum = binding.runningTimeRegNum.text.toString()
        sharedPreferencesManager.writeString(Constants.AirConditionSettings.Register.runningTime,runningTimeRegNum)
        // 其他设置部分 end
    }

}