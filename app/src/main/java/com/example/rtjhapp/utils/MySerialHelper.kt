package com.example.rtjhapp.utils

import com.example.rtjhapp.utils.ByteUtil.comBeanToHex
import tp.xmaihh.serialport.SerialHelper
import tp.xmaihh.serialport.bean.ComBean
import java.nio.charset.Charset

interface OnDataReceivedListener {
    fun onDataReceived(receivedData: String) {}

    fun onControlDataReceived(receivedData : String,hex : String) {}
}

open class MySerialHelper(sPort: String?, iBaudRate: Int) : SerialHelper(sPort, iBaudRate) {
    private var onDataReceivedListener: OnDataReceivedListener? = null

    fun setOnDataReceivedListener(listener: OnDataReceivedListener) {
        this.onDataReceivedListener = listener
    }

    override fun onDataReceived(comBean: ComBean) {
        val data = comBean.bRec
        val receivedString = String(data, Charset.forName("UTF-8"))
        onDataReceivedListener?.onDataReceived(receivedString)
    }
}

open class ControlSerialHelper(sPort: String?, iBaudRate: Int) : SerialHelper(sPort, iBaudRate) {
    private var onDataReceivedListener: OnDataReceivedListener? = null

    fun setOnDataReceivedListener(listener: OnDataReceivedListener) {
        this.onDataReceivedListener = listener
    }

    override fun onDataReceived(comBean: ComBean) {
        val data = comBean.bRec
        fun byteArrayToHex(byteArray: ByteArray): String {
            val hexChars = CharArray(byteArray.size * 2)
            for (i in byteArray.indices) {
                val v = byteArray[i].toInt() and 0xFF
                hexChars[i * 2] = "0123456789ABCDEF"[v ushr 4]
                hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
            }
            return String(hexChars)
        }
        val receivedString = String(data, Charset.forName("UTF-8"))
        onDataReceivedListener?.onControlDataReceived(receivedString,byteArrayToHex(data))
    }
}


open class AirConditionSerialHelper(sPort: String?, iBaudRate: Int) : SerialHelper(sPort, iBaudRate) {
    private var onDataReceivedListener: OnDataReceivedListener? = null

    fun setOnDataReceivedListener(listener: OnDataReceivedListener) {
        this.onDataReceivedListener = listener
    }

    override fun onDataReceived(comBean: ComBean) {
        val receivedString = ByteUtil.comBeanToHex(comBean)
        onDataReceivedListener?.onDataReceived(receivedString)
    }
}

open class VTOSerialHelper(sPort : String?, iBaudRate : Int) : SerialHelper(sPort, iBaudRate){
    private var onDataReceivedListener: OnDataReceivedListener? = null

    fun setOnDataReceivedListener(listener: OnDataReceivedListener) {
        this.onDataReceivedListener = listener
    }

    override fun onDataReceived(comBean: ComBean) {
        val receivedString = comBeanToHex(comBean)
        onDataReceivedListener?.onDataReceived(receivedString)
    }
}

