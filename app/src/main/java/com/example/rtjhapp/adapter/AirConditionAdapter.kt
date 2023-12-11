package com.example.rtjhapp.adapter

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.AirConditionBinding
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.AirConditionSerialHelper
import com.example.rtjhapp.utils.ByteUtil
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.DebounceClickListener
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.OnDataReceivedListener
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.example.rtjhapp.utils.modbus.CRC16
import kotlin.math.min

class AirConditionAdapter(private val binding : AirConditionBinding) {
    private val dutyRunningLayout : LinearLayout = binding.dutyRunningLayout
    private val neLayout : LinearLayout = binding.neRunningLayout
    private val airDutyLayout : LinearLayout = binding.airDutyLayout
    private val airNegeLayout : LinearLayout = binding.airNegeLayout
    private val reHumReg : String?
    private val reHumNeedDivision : Boolean
    private val reTemReg : String?
    private val reTemNeedDivision : Boolean
    private val setHumText : TextView = binding.settingHumText
    private val humPlus : ImageButton = binding.humidityPlus
    private val humReduce : ImageButton = binding.humidityReduce
    private val setHumReg : String?
    private val setHumMin : String?
    private val setHumMax : String?
    private val setHumNeedMulti : Boolean
    private val setTemText : TextView = binding.settingTemText
    private val tempPlus : ImageButton = binding.tempPlus
    private val tempReduce : ImageButton = binding.tempReduce
    private val setTemReg : String?
    private val setTemMax : String?
    private val setTemMin : String?
    private val setTemNeedMulti : Boolean
    private val dutyReg : String?
    private val dutyStatusImg : ImageView = binding.dutyRunningImg
    private val negeReg : String?
    private val negeStatusImg : ImageView = binding.neRunningImg
    private val systemNormalReg : String?
    private val systemNormalStatusImg : ImageView = binding.systemNormalImg
    private val systemWarningReg : String?
    private val systemWarningImg : ImageView = binding.systemWarningImg
    private val systemErrorReg : String?
    private val systemErrorImg : ImageView = binding.systemErrorImg
    private val airConditionDutyBtn : ImageButton = binding.airConditionDutyBtn
    private var airConditionDutyFlag : Boolean = false
    private val airConditionStartStopBtn : ImageButton = binding.airStartStopBtn
    private var airConditionStartStopFlag : Boolean = false
    private val airConditionNegeStressBtn : ImageButton = binding.positiveNegativePressureBtn
    private var airConditionNegeStressFlag : Boolean = false
    private val airConditionSerialHelper : AirConditionSerialHelper
    private val sharedPreferencesManager : SharedPreferencesManager =
        SharedPreferencesManager(binding.root.context)
    private val airConditionHandler = Handler(Looper.getMainLooper())

    init {
        reHumReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.returnHum,
            Constants.AirConditionSettings.Default.Register.returnHum
        )

        reHumNeedDivision = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.returnHum,
            true
        )

        reTemReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.returnTem,
            Constants.AirConditionSettings.Default.Register.returnTem
        )

        reTemNeedDivision = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.returnTem,
            true
        )

        setHumText.text = Constants.AirConditionSettings.Default.Min.hum

        setHumReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.settingHum,
            Constants.AirConditionSettings.Default.Register.settingHum
        )

        setHumMin = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Min.hum,
            Constants.AirConditionSettings.Default.Min.hum
        )

        setHumMax = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Max.hum,
            Constants.AirConditionSettings.Default.Max.hum
        )

        setHumNeedMulti = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.settingHum,
            true
        )

        setTemText.text = Constants.AirConditionSettings.Default.Min.tem

        setTemReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.settingTem,
            Constants.AirConditionSettings.Default.Register.settingTem
        )

        setTemMax = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Max.tem,
            Constants.AirConditionSettings.Default.Max.tem
        )

        setTemMin = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Min.tem,
            Constants.AirConditionSettings.Default.Min.tem
        )

        setTemNeedMulti = sharedPreferencesManager.readBoolean(
            Constants.AirConditionSettings.NeedCalculator.settingTem,
            true
        )

        dutyReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.dutyMode,
            Constants.AirConditionSettings.Default.Register.dutyMode
        )

        negeReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.neStressToggle,
            Constants.AirConditionSettings.Default.Register.neStressToggle
        )

        systemNormalReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.runningState,
            Constants.AirConditionSettings.Default.Register.runningState
        )

        systemWarningReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.unitFailure,
            Constants.AirConditionSettings.Default.Register.unitFailure
        )

        systemErrorReg = sharedPreferencesManager.readString(
            Constants.AirConditionSettings.Register.systemError,
            Constants.AirConditionSettings.Default.Register.systemError
        )

        val airConditionPort = sharedPreferencesManager.readString(
            Constants.SerialPort.airCondition,
            Constants.SerialPort.Default.airCondition
        )
        airConditionSerialHelper =
            AirConditionSerialHelper(airConditionPort, Constants.SerialPortDefaultConfig.baudRate)
        airConditionSerialHelper.setOnDataReceivedListener(object : OnDataReceivedListener {
            override fun onDataReceived(receivedData : String) {
                if (receivedData.length > 16 && receivedData.startsWith(Constants.AirConditionOrder.StartWith.getStatus)) {
                    setAirConditionStatus(receivedData, binding)
                    airConditionHandler.post {
                        AddMsgToDebugList.addMsg(
                            "获取空调状态",
                            receivedData
                        )
                    }
                }
            }
        })
        try {
            airConditionSerialHelper.open()
        } catch (e : Exception) {
            MyToast().error(binding.root.context, "空调模块串口未打开")
        }
        setOnClickListener()
    }

    fun isShow() {
        val showDutyFlag = sharedPreferencesManager.readBoolean("DutyDisplayState", true)
        val showNeFlag = sharedPreferencesManager.readBoolean("NeGeStressToggleDisplayState", true)

        if (! showDutyFlag) {
            dutyRunningLayout.visibility = View.GONE
            airDutyLayout.visibility = View.GONE
        } else {
            dutyRunningLayout.visibility = View.VISIBLE
            airDutyLayout.visibility = View.VISIBLE
        }

        if (! showNeFlag) {
            neLayout.visibility = View.GONE
            airNegeLayout.visibility = View.GONE
        } else {
            neLayout.visibility = View.VISIBLE
            airNegeLayout.visibility = View.VISIBLE
        }
    }

    fun getStatus() {
        if (airConditionSerialHelper.isOpen) {
            val hex = sharedPreferencesManager.readString(
                Constants.AirConditionSettings.Order.getStatus,
                Constants.AirConditionSettings.Default.Order.getStatus
            )
            airConditionSerialHelper.sendHex(hex)
        }
    }

    private fun setAirConditionStatus(hex : String, binding : AirConditionBinding) {
        val data = hex.substring(6, hex.length - 1)
        val dataList = data.chunked(4)
        for ((index, bytes) in dataList.withIndex()) {
            if (index + 1 == reTemReg !!.toInt()) {
                val reTemVal = if (reTemNeedDivision) {
                    ByteUtil.hexToDecimal(bytes).toString()

                } else {
                    bytes.toInt(16).toString()
                }
                airConditionHandler.post {
                    binding.tempCircle.setValue(reTemVal, setTemMax !!.toFloat())
                }
            } else if (index + 1 == reHumReg !!.toInt()) {
                val reHumVal = if (reHumNeedDivision) {
                    ByteUtil.hexToDecimal(bytes).toString()
                } else {
                    bytes.toInt(16).toString()
                }
                airConditionHandler.post {
                    binding.humidityCircle.setValue(reHumVal, setHumMax !!.toFloat())
                }
            } else if (index + 1 == dutyReg !!.toInt()) {
                val dutyVal = bytes.lastOrNull().toString()
                if (dutyVal.toInt() == 1) {
                    dutyStatusImg.setImageResource(R.drawable.air_system_green)
                }
            } else if (index + 1 == negeReg !!.toInt()) {
                val negeVal = bytes.lastOrNull().toString()
                if (negeVal.toInt() == 1) {
                    negeStatusImg.setImageResource(R.drawable.air_system_green)
                }
            } else if (index + 1 == systemNormalReg !!.toInt()) {
                val status = bytes.lastOrNull().toString()
                if (status.toInt() == 1) {
                    systemNormalStatusImg.setImageResource(R.drawable.air_system_green)
                }
            } else if (index + 1 == systemWarningReg !!.toInt()) {
                val status = bytes.lastOrNull().toString()
                if (status.toInt() == 1) {
                    systemWarningImg.setImageResource(R.drawable.air_system_yellow)
                }
            } else if (index + 1 == systemErrorReg !!.toInt()) {
                val status = bytes.lastOrNull().toString()
                if (status.toInt() == 1) {
                    systemErrorImg.setImageResource(R.drawable.air_system_red)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setOnClickListener() {
        val debounceInterval = 200L

        // 温度加号
        tempPlus.setOnClickListener(DebounceClickListener({
            val currentTempVal = setTemText.text.toString().toInt()
            if (currentTempVal == Constants.AirConditionSettings.Default.Max.tem.toInt()) {
                MyToast().info(binding.root.context, "已达温度最大值")
            } else {
                val newTempVal = if (setTemNeedMulti) {
                    (currentTempVal + 1) * 10
                } else {
                    currentTempVal + 1
                }

                val setTemRegHex = ByteUtil.intToHex((setTemReg !!.toInt() * 2), 4)
                val tempHex = ByteUtil.intToHex(newTempVal, 4)
                val hex = "0106${setTemRegHex}${tempHex}"
                val crc = CRC16.getCRC(hex)
                if (airConditionSerialHelper.isOpen) {
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        setTemText.text = (currentTempVal + 1).toString()
                        AddMsgToDebugList.addMsg("下发设置温度指令", hex + crc)
                    }
                } else {
                    MyToast().error(binding.root.context, "空调模块串口未打开")
                }
            }
        }, debounceInterval))

        // 温度减号
        tempReduce.setOnClickListener(DebounceClickListener({
            val currentTempVal = setTemText.text.toString().toInt()
            if (currentTempVal == Constants.AirConditionSettings.Default.Min.tem.toInt()) {
                MyToast().info(binding.root.context, "已达温度最小值")
            } else {
                val newTempVal = if (setTemNeedMulti) {
                    (currentTempVal - 1) * 10
                } else {
                    currentTempVal - 1
                }

                val setTemRegHex = ByteUtil.intToHex((setTemReg !!.toInt() * 2), 4)
                val tempHex = ByteUtil.intToHex(newTempVal, 4)
                val hex = "0106${setTemRegHex}${tempHex}"
                val crc = CRC16.getCRC(hex)
                if (airConditionSerialHelper.isOpen) {
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        setTemText.text = (currentTempVal - 1).toString()
                        AddMsgToDebugList.addMsg("下发设置温度指令", hex + crc)
                    }
                } else {
                    MyToast().error(binding.root.context, "空调模块串口未打开")
                }
            }
        }, debounceInterval))

        // 湿度加号
        humPlus.setOnClickListener(DebounceClickListener({
            val currentHumVal = setHumText.text.toString().toInt()
            if (currentHumVal == Constants.AirConditionSettings.Default.Max.hum.toInt()) {
                MyToast().info(binding.root.context, "已达湿度最大值")
            } else {
                val newHumVal = if (setHumNeedMulti) {
                    (currentHumVal + 1) * 10
                } else {
                    currentHumVal + 1
                }

                val setHumRegHex = ByteUtil.intToHex((setHumReg !!.toInt() * 2), 4)
                val tempHex = ByteUtil.intToHex(newHumVal, 4)
                val hex = "0106${setHumRegHex}${tempHex}"
                val crc = CRC16.getCRC(hex)
                if (airConditionSerialHelper.isOpen) {
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        setHumText.text = (currentHumVal + 1).toString()
                        AddMsgToDebugList.addMsg("下发设置湿度指令", hex + crc)
                    }
                } else {
                    MyToast().error(binding.root.context, "空调模块串口未打开")
                }
            }
        }, debounceInterval))

        // 湿度减号
        humReduce.setOnClickListener(DebounceClickListener({
            val currentHumVal = setHumText.text.toString().toInt()
            if (currentHumVal == Constants.AirConditionSettings.Default.Min.hum.toInt()) {
                MyToast().info(binding.root.context, "已达湿度最小值")
            } else {
                val newHumVal = if (setHumNeedMulti) {
                    (currentHumVal - 1) * 10
                } else {
                    currentHumVal - 1
                }

                val setHumRegHex = ByteUtil.intToHex((setHumReg !!.toInt() * 2), 4)
                val tempHex = ByteUtil.intToHex(newHumVal, 4)
                val hex = "0106${setHumRegHex}${tempHex}"
                val crc = CRC16.getCRC(hex)
                if (airConditionSerialHelper.isOpen) {
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        setHumText.text = (currentHumVal - 1).toString()
                        AddMsgToDebugList.addMsg("下发设置湿度指令", hex + crc)
                    }
                } else {
                    MyToast().error(binding.root.context, "空调模块串口未打开")
                }
            }
        }, debounceInterval))

        // 空调值班
        airConditionDutyBtn.setOnClickListener(DebounceClickListener({
            if (airConditionSerialHelper.isOpen) {
                if (! airConditionDutyFlag) {
                    val hex = Constants.AirConditionOrder.AirCondition.dutyStart
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发空调值班开启指令", hex + crc)
                        airConditionDutyBtn.setImageResource(R.drawable.air_condition_duty_selected)
                        airConditionDutyFlag = true
                    }
                } else {
                    val hex = Constants.AirConditionOrder.AirCondition.dutyStop
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发空调值班停止指令", hex + crc)
                        airConditionDutyBtn.setImageResource(R.drawable.air_condition_duty)
                        airConditionDutyFlag = false
                    }
                }
            } else {
                MyToast().error(binding.root.context, "空调模块串口未打开")
            }
        }, debounceInterval))

        // 空调启停
        airConditionStartStopBtn.setOnClickListener(DebounceClickListener({
            if (airConditionSerialHelper.isOpen) {
                if (! airConditionStartStopFlag) {
                    val hex = Constants.AirConditionOrder.AirCondition.start
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发空调启动指令", hex + crc)
                        airConditionStartStopBtn.setImageResource(R.drawable.air_condition_start_stop_selected)
                        airConditionStartStopFlag = true
                    }
                } else {
                    val hex = Constants.AirConditionOrder.AirCondition.stop
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发空调停止指令", hex + crc)
                        airConditionStartStopBtn.setImageResource(R.drawable.air_condition_start_stop)
                        airConditionStartStopFlag = false
                    }
                }
            } else {
                MyToast().error(binding.root.context, "空调模块串口未打开")
            }
        }, debounceInterval))

        // 正负压
        airConditionNegeStressBtn.setOnClickListener(DebounceClickListener({
            if (airConditionSerialHelper.isOpen) {
                if (! airConditionNegeStressFlag) {
                    val hex = Constants.AirConditionOrder.AirCondition.neStress
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发正负压指令", hex + crc)
                        airConditionNegeStressBtn.setImageResource(R.drawable.nege_stress_selected)
                        airConditionNegeStressFlag = true
                    }
                } else {
                    val hex = Constants.AirConditionOrder.AirCondition.geStress
                    val crc = CRC16.getCRC(hex)
                    airConditionHandler.post {
                        airConditionSerialHelper.sendHex(hex + crc)
                        AddMsgToDebugList.addMsg("下发正负压指令", hex + crc)
                        airConditionNegeStressBtn.setImageResource(R.drawable.positive_negative_pressure)
                        airConditionNegeStressFlag = false
                    }
                }
            } else {
                MyToast().error(binding.root.context, "空调模块串口未打开")
            }
        }, debounceInterval))
    }

    fun closeHelper() {
        airConditionSerialHelper.close()
    }
}