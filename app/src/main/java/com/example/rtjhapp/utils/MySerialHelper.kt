package com.example.rtjhapp.utils

import tp.xmaihh.serialport.SerialHelper
import tp.xmaihh.serialport.bean.ComBean
import tp.xmaihh.serialport.stick.AbsStickPackageHelper

class MySerialHelper(sPort: String, iBaudRate: Int) : SerialHelper(sPort,iBaudRate) {
    override fun onDataReceived(comBean: ComBean) {
        // 在这里处理接收到的数据
        // comBean 包含串口数据，可以从 comBean.buffer 中获取字节数组
        // 例如：val data = comBean.buffer
        val data = comBean.bRec

        val receivedString = String(data, charset("UTF-8"))


    }
}