package com.example.rtjhapp.adapter

import android.widget.ImageButton
import android.widget.TextView
import com.example.rtjhapp.databinding.CallBinding
import com.example.rtjhapp.event.UpdateDebugMessageEvent
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.MySerialHelper
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.SharedPreferencesManager
import org.greenrobot.eventbus.EventBus
import java.lang.Exception


class CallAdapter(private val binding : CallBinding){
    private lateinit var phoneNumberTextView: TextView
    private val digitButtons: MutableList<ImageButton> = mutableListOf()
    private lateinit var delBtn: ImageButton
    private lateinit var callBtn: ImageButton
    private lateinit var hangUpBtn: ImageButton
    private lateinit var sharedPreferencesManager : SharedPreferencesManager
    private lateinit var callSerialHelper: MySerialHelper

    fun initViews() {
        phoneNumberTextView = binding.phoneNumText
        digitButtons.add(binding.num1)
        digitButtons.add(binding.num2)
        digitButtons.add(binding.num3)
        digitButtons.add(binding.num4)
        digitButtons.add(binding.num5)
        digitButtons.add(binding.num6)
        digitButtons.add(binding.num7)
        digitButtons.add(binding.num8)
        digitButtons.add(binding.num9)
        digitButtons.add(binding.numStar)
        digitButtons.add(binding.num0)
        digitButtons.add(binding.numJin)

        delBtn = binding.delNum
        callBtn = binding.callBtn
        hangUpBtn = binding.hangUpBtn

        sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val serialAddress = sharedPreferencesManager.readString(Constants.SerialPort.call,Constants.SerialPort.Default.call)
        val iBaudRate = Constants.SerialPortDefaultConfig.baudRate
        callSerialHelper = serialAddress?.let { MySerialHelper(it, iBaudRate) } !!
        try {
            callSerialHelper.open()
        } catch (e:Exception) {
            MyToast().error(binding.root.context,"电话模块串口未打开")
        }
        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        for (i in digitButtons.indices) {
            digitButtons[i].setOnClickListener {
                handleButtonClick((i + 1).toString())
            }
        }

        delBtn.setOnClickListener {
            handleDelBtnClick()
        }

        callBtn.setOnClickListener {
            if (callSerialHelper.isOpen){
                handleCallBtnClick()
            }else {
                MyToast().error(binding.root.context,"电话模块串口未打开")
            }
        }
        hangUpBtn.setOnClickListener {
            if(callSerialHelper.isOpen){
                handleHangUpBtnClick()
            }
        }
    }

    private fun handleCallBtnClick(){
        val number = phoneNumberTextView.text.toString()
        var msg = ""
        msg = if (number.isEmpty()){
            // 发送接听指令
            val answerCommand = "FE10000A000810EEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF"
            callSerialHelper.sendHex(answerCommand)
            "下发接听指令：$answerCommand"
        } else {
            val phoneNumber = handlePhoneNumberToHex(number)
            callSerialHelper.sendHex(phoneNumber.toString())
            "下发拨号指令：$phoneNumber"
        }

        EventBus.getDefault().post(UpdateDebugMessageEvent(msg))
    }

    private fun handlePhoneNumberToHex(phoneNumber : String) : StringBuffer {
        val header = "FE10000A000810"
        val stringBuffer = StringBuffer()
        stringBuffer.append(header)
        stringBuffer.append("FF")
        var phone = ""
        for (i in phoneNumber.indices) {
            phone = phone + "0" + phoneNumber.substring(i, i + 1)
        }
        if (phone.length < 30) {
            for (i in phone.length..29) {
                phone += "F"
            }
        }
        stringBuffer.append(phone)
        return stringBuffer
    }

    /**
     * 处理拒接按键
     */
    private fun handleHangUpBtnClick(){
        val hex = "FE06000900018C07"
        callSerialHelper.sendHex(hex)
        EventBus.getDefault().post(UpdateDebugMessageEvent("拒接：$hex"))
    }

    /**
     * 处理删除按键
     */
    private fun handleDelBtnClick(){
        val currentPhoneNumber = phoneNumberTextView.text.toString()
        if (currentPhoneNumber.isNotEmpty()){
            val newPhoneNumber = currentPhoneNumber.substring(0,currentPhoneNumber.length -1)
            phoneNumberTextView.text = newPhoneNumber
        }
    }

    /**
     * 处理数字按键
     */
    private fun handleButtonClick(digit: String){
        val currentPhoneNumber = phoneNumberTextView.text.toString()
        var digits = digit
        when(digit){
            "10" -> {
                digits = "*"
            }
            "11" -> {
                digits = "0"
            }
            "12" -> {
                digits = "#"
            }
        }
        val newPhoneNumber = currentPhoneNumber + digits
        phoneNumberTextView.text = newPhoneNumber
    }
}