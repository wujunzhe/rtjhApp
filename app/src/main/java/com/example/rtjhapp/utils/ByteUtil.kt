package com.example.rtjhapp.utils

import tp.xmaihh.serialport.bean.ComBean
import java.math.BigDecimal
import java.math.RoundingMode

object ByteUtil {

    fun toHexString(input : ByteArray?, separator : String) : String? {
        if (input == null) return null

        val sb = StringBuilder()
        for (i in input.indices) {
            if (separator.isNotEmpty() && sb.isNotEmpty()) {
                sb.append(separator)
            }
            var str = Integer.toHexString(input[i].toInt() and 0xff)
            if (str.length == 1) str = "0$str"
            sb.append(str)
        }
        return sb.toString()
    }

    fun toHexString(input : ByteArray?) : String? {
        return toHexString(input, " ")
    }

    fun fromInt32(input : Int) : ByteArray {
        val result = ByteArray(4)
        result[3] = (input shr 24 and 0xFF).toByte()
        result[2] = (input shr 16 and 0xFF).toByte()
        result[1] = (input shr 8 and 0xFF).toByte()
        result[0] = (input and 0xFF).toByte()
        return result
    }

    fun fromInt16(input : Int) : ByteArray {
        val result = ByteArray(2)
        result[0] = (input shr 8 and 0xFF).toByte()
        result[1] = (input and 0xFF).toByte()
        return result
    }

    fun fromInt16Reversal(input : Int) : ByteArray {
        val result = ByteArray(2)
        result[1] = (input shr 8 and 0xFF).toByte()
        result[0] = (input and 0xFF).toByte()
        return result
    }

    fun intToHex(number : Int, width : Int) : String {
        val hex = Integer.toHexString(number)
        return hex.padStart(width, '0')
    }

    fun comBeanToHex(comBean : ComBean) : String {
        val hexStringBuilder = StringBuilder()

        val data = comBean.bRec
        for (byteValue in data) {
            val hex = String.format("%02X", byteValue)
            hexStringBuilder.append(hex)
        }
        return hexStringBuilder.toString()
    }

    fun hexToDecimal(hexString : String) : BigDecimal {
        val intVal = hexString.toLong(16)
        return BigDecimal(intVal).setScale(1, RoundingMode.DOWN).divide(BigDecimal(10))
    }

}
