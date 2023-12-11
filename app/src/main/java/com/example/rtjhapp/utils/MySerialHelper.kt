package com.example.rtjhapp.utils

import tp.xmaihh.serialport.SerialHelper
import tp.xmaihh.serialport.bean.ComBean
import java.nio.charset.Charset

interface OnDataReceivedListener {
    fun onDataReceived(receivedData : String)
}

open class MySerialHelper(sPort : String?, iBaudRate : Int) : SerialHelper(sPort, iBaudRate) {

    private var onDataReceivedListener : OnDataReceivedListener? = null

    fun setOnDataReceivedListener(listener : OnDataReceivedListener) {
        this.onDataReceivedListener = listener
    }

    override fun onDataReceived(comBean : ComBean) {
        // 在这里处理接收到的数据
        val data = comBean.bRec
        val receivedString = String(data, Charset.forName("UTF-8"))

        // 通过回调或其他方式传递数据给需要处理的地方
        onDataReceivedListener?.onDataReceived(receivedString)
    }
}