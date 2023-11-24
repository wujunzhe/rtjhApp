package com.example.rtjhapp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.BottomBinding
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.MySerialHelper
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.example.rtjhapp.utils.modbus.ModbusRtuMaster

class BottomAdapter(private val binding:BottomBinding) {
        private lateinit var lighting1Btn: ImageView
        private lateinit var lighting2Btn: ImageView
        private lateinit var viewingLightsBtn: ImageView
        private lateinit var noShadowLightsBtn: ImageView
        private lateinit var surgicalLightsBtn: ImageView
        private lateinit var exhaustFanBtn: ImageView
        private lateinit var disinfectBtn: ImageView
        private lateinit var firefightingBtn: ImageView
        private lateinit var powerBtn: ImageView
        private lateinit var silenceBtn: ImageView
        private lateinit var spare1Btn: ImageView
        private lateinit var spare2Btn : ImageView
        private lateinit var spare3Btn: ImageView
        private lateinit var spare4Btn: ImageView
        private lateinit var spare1Layout: LinearLayout
        private lateinit var spare2Layout: LinearLayout
        private lateinit var spare3Layout: LinearLayout
        private lateinit var spare4Layout: LinearLayout
        private lateinit var differentGaugeLayout: LinearLayout
        private lateinit var sharedPreferencesManager : SharedPreferencesManager
        private lateinit var controlSerialHelper: MySerialHelper
        private lateinit var modbusRtuMaster : ModbusRtuMaster
        private var lighting1Flag = false
        private var lighting2Flag = false
        private var viewingLightsFlag = false
        private var noShadowLightsFlag = false
        fun initViews(){
            differentGaugeLayout = binding.differentGauge
            spare1Layout = binding.spare1Layout
            spare2Layout = binding.spare2Layout
            spare3Layout = binding.spare3Layout
            spare4Layout = binding.spare4Layout
            lighting1Btn = binding.lighting1Btn
            lighting2Btn = binding.lighting2Btn
            viewingLightsBtn = binding.viewingLightsBtn
            noShadowLightsBtn = binding.noShadowLightsBtn
            surgicalLightsBtn = binding.surgicalLightsBtn
            exhaustFanBtn = binding.exhaustFanBtn
            disinfectBtn = binding.disinfectBtn
            firefightingBtn = binding.firefightingBtn
            powerBtn = binding.powerBtn
            silenceBtn = binding.silenceBtn
            spare1Btn = binding.spare1Btn
            spare2Btn = binding.spare2Btn
            spare3Btn = binding.spare3Btn
            spare4Btn = binding.spare4Btn

            sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
            val serialAddress = sharedPreferencesManager.readString(Constants.SerialPort.control,Constants.SerialPort.Default.control)
            val baudRate = Constants.SerialPortDefaultConfig.baudRate
            controlSerialHelper = serialAddress?.let { MySerialHelper(it,baudRate) } !!
            modbusRtuMaster = ModbusRtuMaster(controlSerialHelper)
            try {
                controlSerialHelper.open()
            } catch (e: Exception) {
                MyToast().error(binding.root.context,"控制模块串口未打开")
            }
            setBtnClickListeners()
        }

    fun isShow(){
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val layoutMap = mapOf(
            differentGaugeLayout to Constants.DisplayState.differentGauge,
            spare1Layout to Constants.DisplayState.spare1,
            spare2Layout to Constants.DisplayState.spare2,
            spare3Layout to Constants.DisplayState.spare3,
            spare4Layout to Constants.DisplayState.spare4
        )
        layoutMap.forEach{(layout,displayState) ->
            val display = sharedPreferencesManager.readBoolean(displayState,true)
            layout.visibility = if (!display) View.GONE else View.VISIBLE
        }
    }

    private fun setBtnClickListeners(){

        // 照明1
        lighting1Btn.setOnClickListener {
            lighting1Flag = if (!lighting1Flag) {
                lighting1Btn.setImageResource(R.drawable.lights1_selected)
                true
            }else {
                lighting1Btn.setImageResource(R.drawable.lights1_unselected)
                false
            }

        }

        // 照明2
        lighting2Btn.setOnClickListener {
            lighting2Flag = if (!lighting2Flag){
                lighting2Btn.setImageResource(R.drawable.lights2_selected)
                true
            }else {
                lighting2Btn.setImageResource(R.drawable.lights2_selected)
                false
            }
        }
    }

    fun getStatus(){
        val statusHex = modbusRtuMaster.readHoldingRegister(255,1)
        if (controlSerialHelper.isOpen){
            val getStatusHex = "FF03000A000871D0"
            controlSerialHelper.sendHex(getStatusHex)

        }
    }
}