package com.example.rtjhapp.adapter

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.BottomBinding
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.ByteUtil
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.ControlSerialHelper
import com.example.rtjhapp.utils.DebounceClickListener
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.OnDataReceivedListener
import com.example.rtjhapp.utils.SharedPreferencesManager

class BottomAdapter(private val binding : BottomBinding) : OnDataReceivedListener {
    private var lighting1Btn : ImageView = binding.lighting1Btn
    private var lighting2Btn : ImageView = binding.lighting2Btn
    private var viewingLightsBtn : ImageView = binding.viewingLightsBtn
    private var noShadowLightsBtn : ImageView = binding.noShadowLightsBtn
    private var surgicalLightsBtn : ImageView = binding.surgicalLightsBtn
    private var exhaustFanBtn : ImageView = binding.exhaustFanBtn
    private var disinfectBtn : ImageView = binding.disinfectBtn
    private var firefightingBtn : ImageView = binding.firefightingBtn
    private var powerBtn : ImageView = binding.powerBtn
    private var silenceBtn : ImageView = binding.silenceBtn
    private var spare1Btn : ImageView = binding.spare1Btn
    private var spare2Btn : ImageView = binding.spare2Btn
    private var spare3Btn : ImageView = binding.spare3Btn
    private var spare4Btn : ImageView = binding.spare4Btn
    private var spare1Layout : LinearLayout = binding.spare1Layout
    private var spare2Layout : LinearLayout = binding.spare2Layout
    private var spare3Layout : LinearLayout = binding.spare3Layout
    private var spare4Layout : LinearLayout = binding.spare4Layout
    private var differentGaugeLayout : LinearLayout = binding.differentGauge
    private var sharedPreferencesManager : SharedPreferencesManager =
        SharedPreferencesManager(binding.root.context)
    private var controlSerialHelper : ControlSerialHelper
    private var lighting1Flag = false
    private var lighting2Flag = false
    private var viewingLightsFlag = false
    private var noShadowLightsFlag = false
    private var surgicalLightsFlag = false
    private var exhaustFanFlag = false
    private var disinfectFlag = false
    private var firefightingFlag = false
    private var powerFlag = false
    private var silenceFlag = false
    private var spare1Flag = false
    private val bottomHandler = Handler(Looper.getMainLooper())

    init {
        val serialAddress = sharedPreferencesManager.readString(
            Constants.SerialPort.control,
            Constants.SerialPort.Default.control
        )
        val baudRate = Constants.SerialPortDefaultConfig.baudRate
        controlSerialHelper = ControlSerialHelper(serialAddress, baudRate)
        controlSerialHelper.setOnDataReceivedListener(this)
        try {
            controlSerialHelper.open()
            controlSerialHelper.sendHex("FF060008000A9DD1")
        } catch (e : Exception) {
            MyToast().error(binding.root.context, "控制模块串口未打开")
        }
//        setDataListener()
        setTimeSettings()
        setBtnClickListeners()
    }

    fun getStatus() {
        if (controlSerialHelper.isOpen) {
            val hex = Constants.ControlOrder.Get.controlStatusHex
            controlSerialHelper.sendHex(hex)
        }
    }

    override fun onControlDataReceived(receivedData : String, hex : String) {
        super.onControlDataReceived(receivedData, hex)
        bottomHandler.postDelayed({
            AddMsgToDebugList.addMsg(
                "当前串口地址${controlSerialHelper.port},获取继电器状态",
                hex
            )
            try {
                if (hex.startsWith(Constants.ControlOrder.StartWith.getStatus)) {
                    setControlStatus(hex)
                }
            } catch (e : Exception) {
                AddMsgToDebugList.addMsg("错误", e.message.toString())
            }
        }, 0)
        if (receivedData.length > 16 && receivedData.startsWith(Constants.ControlOrder.StartWith.getStatus)) {
            setControlStatus(receivedData)
            bottomHandler.post {
                AddMsgToDebugList.addMsg(
                    "当前串口地址${controlSerialHelper.port},获取继电器状态",
                    receivedData
                )
            }
        }
    }
//    private fun setDataListener(){
//        controlSerialHelper.setOnDataReceivedListener(object : OnDataReceivedListener {
//            override fun onControlDataReceived(receivedData : String, hex : String) {
//                bottomHandler.post {
//                    AddMsgToDebugList.addMsg("接收到了控制模块数据", hex)
//                }
//                if (receivedData.length > 16 && receivedData.startsWith(Constants.ControlOrder.StartWith.getStatus)) {
//                    setControlStatus(receivedData)
//                    bottomHandler.post {
//                        AddMsgToDebugList.addMsg(
//                            "当前串口地址${controlSerialHelper.port},获取继电器状态",
//                            hex
//                        )
//                    }
//                }
//            }
//        })
//    }

    fun isShow() {
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val layoutMap = mapOf(
            differentGaugeLayout to Pair(Constants.DisplayState.differentGauge, true),
            spare1Layout to Pair(Constants.DisplayState.spare1, true),
            spare2Layout to Pair(Constants.DisplayState.spare2, false),
            spare3Layout to Pair(Constants.DisplayState.spare3, false),
            spare4Layout to Pair(Constants.DisplayState.spare4, false)
        )

        layoutMap.forEach { (layout, pair) ->
            val displayState = pair.component1()
            val defaultValue = pair.component2()
            val display = sharedPreferencesManager.readBoolean(displayState, defaultValue)
            layout.visibility = if (! display) View.GONE else View.VISIBLE
        }
    }

    /**
     * 处理消毒延时和照明延时事件
     */
    private fun setTimeSettings() {
        if (controlSerialHelper.isOpen) {
            // 消毒延时
            val disinfectTime = sharedPreferencesManager.readString(
                Constants.SmartSettings.disinfectTime,
                Constants.SmartSettings.Default.disinfectTime
            )
            if (disinfectTime != null) {
                if (disinfectTime.toInt() == 0) {
                    controlSerialHelper.sendHex("FF0600090000CC11")
                    AddMsgToDebugList.addMsg("下发设置消毒时间为0指令", "FF0600090000CC11")
                } else if (disinfectTime.toInt() > 0) {
                    val timeHex = ByteUtil.intToHex(disinfectTime.toInt(), 4)
                    val hex = "FF060009${timeHex}CC11"
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发设置消毒时间为${disinfectTime}分钟指令", hex)
                }
            }

            // 照明延时
            val lightDelayTime = sharedPreferencesManager.readString(
                Constants.SmartSettings.lightDelayTime,
                Constants.SmartSettings.Default.lightDelayTime
            )
            if (lightDelayTime != null) {
                if (lightDelayTime.toInt() == 0) {
                    controlSerialHelper.sendHex("FF060008000A9DD1")
                    AddMsgToDebugList.addMsg("下发设置照明延时时间为0指令", "FF060008000A9DD1")
                } else {
                    val timeHex = ByteUtil.intToHex(lightDelayTime.toInt(), 4)
                    val hex = "FF060008${timeHex}9DD1"
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发设置照明延时时间为${disinfectTime}分钟指令", hex)
                }
            }
        }
    }

    private fun setControlStatus(hex : String) {
        val data = hex.substring(6, hex.length - 4)
        val dataList = data.chunked(4)

        for ((index, value) in dataList.withIndex()) {
            when (index) {
                // 照明1
                0 -> {
                    lighting1Flag = if (value.lastOrNull() == '1') {
                        binding.lighting1Btn.setImageResource(R.drawable.lights1_selected)
                        true
                    } else {
                        binding.lighting1Btn.setImageResource(R.drawable.lights1_unselected)
                        false
                    }
                }

                // 照明2
                1 -> {
                    lighting2Flag = if (value.lastOrNull() == '1') {
                        binding.lighting2Btn.setImageResource(R.drawable.lights2_selected)
                        true
                    } else {
                        binding.lighting2Btn.setImageResource(R.drawable.lights2_unselected)
                        false
                    }
                }

                // 观片灯
                2 -> {
                    viewingLightsFlag = if (value.lastOrNull() == '1') {
                        binding.viewingLightsBtn.setImageResource(R.drawable.viewing_lights_selected)
                        true
                    } else {
                        binding.viewingLightsBtn.setImageResource(R.drawable.viewing_lights_unselected)
                        false
                    }
                }

                // 无影灯
                3 -> {
                    noShadowLightsFlag = if (value.lastOrNull() == '1') {
                        binding.noShadowLightsBtn.setImageResource(R.drawable.no_shadow_lights_selected)
                        true
                    } else {
                        binding.noShadowLightsBtn.setImageResource(R.drawable.no_shaodw_lights_unselected)
                        false
                    }
                }

                // 手术灯
                4 -> {
                    surgicalLightsFlag = if (value.lastOrNull() == '1') {
                        binding.surgicalLightsBtn.setImageResource(R.drawable.surgical_lights_selected)
                        true
                    } else {
                        binding.surgicalLightsBtn.setImageResource(R.drawable.surgical_lights_unselected)
                        false
                    }
                }

                // 排风机
                5 -> {
                    exhaustFanFlag = if (value.lastOrNull() == '1') {
                        binding.exhaustFanBtn.setImageResource(R.drawable.exhaust_fan_selected)
                        true
                    } else {
                        binding.exhaustFanBtn.setImageResource(R.drawable.exhaust_fan_unselected)
                        false
                    }
                }

                // 备用1
                6 -> {
                    spare1Flag = if (value.lastOrNull() == '1') {
                        binding.spare1Btn.setImageResource(R.drawable.spare_selected)
                        true
                    } else {
                        binding.spare1Btn.setImageResource(R.drawable.spare_unselected)
                        false
                    }
                }

                // 消毒
                7 -> {
                    disinfectFlag = if (value.lastOrNull() == '1') {
                        binding.disinfectBtn.setImageResource(R.drawable.spare_selected)
                        true
                    } else {
                        binding.disinfectBtn.setImageResource(R.drawable.spare_unselected)
                        false
                    }
                }
            }

        }
    }

    /**
     * 处理按钮点击事件
     */
    private fun setBtnClickListeners() {

        val debounceInterval = 500L

        // 照明1
        lighting1Btn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                lighting1Flag = if (! lighting1Flag) {
                    val hex = Constants.ControlOrder.Open.lights1Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开照明1指令", hex)
                    lighting1Btn.setImageResource(R.drawable.lights1_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.lights1Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭照明1指令", hex)
                    lighting1Btn.setImageResource(R.drawable.lights1_unselected)
                    val lightDelayTime = sharedPreferencesManager.readString(
                        Constants.SmartSettings.lightDelayTime,
                        Constants.SmartSettings.Default.lightDelayTime
                    )
                    if (lightDelayTime != null) {
                        if (! lighting2Flag) {
                            MyToast().info(
                                binding.root.context,
                                "照明还有${lightDelayTime}分钟关闭"
                            )
                        }
                    }
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))


        // 照明2
        lighting2Btn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                lighting2Flag = if (! lighting2Flag) {
                    val hex = Constants.ControlOrder.Open.lights2Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开照明2指令", hex)
                    lighting2Btn.setImageResource(R.drawable.lights2_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.lights2Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭照明2指令", hex)
                    lighting2Btn.setImageResource(R.drawable.lights2_unselected)
                    val lightDelayTime = sharedPreferencesManager.readString(
                        Constants.SmartSettings.lightDelayTime,
                        Constants.SmartSettings.Default.lightDelayTime
                    )
                    if (lightDelayTime != null) {
                        if (! lighting1Flag) {
                            MyToast().info(
                                binding.root.context,
                                "照明还有${lightDelayTime}分钟关闭"
                            )
                        }
                    }
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 观片灯
        viewingLightsBtn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                viewingLightsFlag = if (! viewingLightsFlag) {
                    val hex = Constants.ControlOrder.Open.viewingLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开观片灯指令", hex)
                    viewingLightsBtn.setImageResource(R.drawable.viewing_lights_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.viewingLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭观片灯指令", hex)
                    viewingLightsBtn.setImageResource(R.drawable.viewing_lights_unselected)
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 无影灯
        noShadowLightsBtn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                noShadowLightsFlag = if (! noShadowLightsFlag) {
                    val hex = Constants.ControlOrder.Open.noShadowLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开无影灯指令", hex)
                    noShadowLightsBtn.setImageResource(R.drawable.no_shadow_lights_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.noShadowLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭无影灯指令", hex)
                    noShadowLightsBtn.setImageResource(R.drawable.no_shaodw_lights_unselected)
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 手术灯
        surgicalLightsBtn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                surgicalLightsFlag = if (! surgicalLightsFlag) {
                    val hex = Constants.ControlOrder.Open.surgicalLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发开启手术灯指令", hex)
                    surgicalLightsBtn.setImageResource(R.drawable.surgical_lights_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.surgicalLightsHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭手术灯指令", hex)
                    surgicalLightsBtn.setImageResource(R.drawable.surgical_lights_unselected)
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 排风机
        exhaustFanBtn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                exhaustFanFlag = if (! exhaustFanFlag) {
                    val hex = Constants.ControlOrder.Open.exhaustFanHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开排风机指令", hex)
                    exhaustFanBtn.setImageResource(R.drawable.exhaust_fan_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.exhaustFanHex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭排风机指令", hex)
                    exhaustFanBtn.setImageResource(R.drawable.exhaust_fan_unselected)
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 备用1
        spare1Btn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                spare1Flag = if (! spare1Flag) {
                    val hex = Constants.ControlOrder.Open.spare1Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发打开备用1指令", hex)
                    spare1Btn.setImageResource(R.drawable.spare_selected)
                    true
                } else {
                    val hex = Constants.ControlOrder.Close.spare1Hex
                    controlSerialHelper.sendHex(hex)
                    AddMsgToDebugList.addMsg("下发关闭备用1指令", hex)
                    spare1Btn.setImageResource(R.drawable.spare_unselected)
                    false
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))

        // 消毒
        disinfectBtn.setOnClickListener(DebounceClickListener({
            if (controlSerialHelper.isOpen) {
                val disinfectTime = sharedPreferencesManager.readString(
                    Constants.SmartSettings.disinfectTime,
                    Constants.SmartSettings.Default.disinfectTime
                )
                if (disinfectTime != null) {
                    if (disinfectTime.toInt() != 0) {
                        MyToast().error(binding.root.context, "设置的消毒时间不为0，无法手动关闭")
                    } else {
                        disinfectFlag = if (! disinfectFlag) {
                            val hex = Constants.ControlOrder.Open.disinfectHex
                            controlSerialHelper.sendHex(hex)
                            AddMsgToDebugList.addMsg("下发打开消毒指令", hex)
                            disinfectBtn.setImageResource(R.drawable.spare_selected)
                            true
                        } else {
                            val hex = Constants.ControlOrder.Close.disinfectHex
                            controlSerialHelper.sendHex(hex)
                            AddMsgToDebugList.addMsg("下发关闭消毒指令", hex)
                            disinfectBtn.setImageResource(R.drawable.spare_unselected)
                            false
                        }
                    }
                }
            } else {
                MyToast().error(binding.root.context, "继电器模块串口未打开")
            }
        }, debounceInterval))
    }

    fun serialIsOpen() : Boolean {
        return controlSerialHelper.isOpen
    }


    fun closeHelper() {
        controlSerialHelper.close()
    }
}