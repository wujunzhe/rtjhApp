package com.example.rtjhapp.adapter

import android.widget.ImageView
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.MedicalStressStatusBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.GlobalData

class GasAdapter(private val binding : MedicalStressStatusBinding) {
    private val gasImageViews : Map<String, ImageView> = mapOf(
        Constants.AirConditionSettings.Default.Register.oxygenStatus to binding.oxygenStatus,
        Constants.AirConditionSettings.Default.Register.nitrogenStatus to binding.nitrogenStatus,
        Constants.AirConditionSettings.Default.Register.nitrousOxideStatus to binding.nitrousOxideStatus,
        Constants.AirConditionSettings.Default.Register.hydrogenStatus to binding.hydrogenStatus,
        Constants.AirConditionSettings.Default.Register.negativePressureStatus to binding.negativePressureStatus,
        Constants.AirConditionSettings.Default.Register.pressurizedAirStatus to binding.pressurizedAirStatus,
        Constants.AirConditionSettings.Default.Register.carbonDioxideStatus to binding.carbonDioxideStatus
    )

    init {
        val hex = GlobalData.airConditionStatusHex
        setStatus(hex)
    }

    fun setStatus(hex : String?) {
        if (hex != null) {
            val data = hex.substring(6, hex.length - 1)
            val dataList = data.chunked(4)

            for ((index, bytes) in dataList.withIndex()) {
                val imageView = gasImageViews[(index + 1).toString()]

                imageView?.let {
                    when (bytes.lastOrNull().toString()) {
                        "0" -> it.setImageResource(R.drawable.green)
                        "1" -> it.setImageResource(R.drawable.red)
                        "2" -> it.setImageResource(R.drawable.yellow)
                    }
                }
            }
        }
    }
}
