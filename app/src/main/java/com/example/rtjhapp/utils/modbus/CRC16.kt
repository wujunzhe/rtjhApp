package com.example.rtjhapp.utils.modbus

import java.util.Locale


object CRC16 {
    private val crc16_tab_h = byteArrayOf(
        0x00.toByte(),
        0xC1.toByte(),
        0x81.toByte(),
        0x40.toByte(),
        0x01.toByte(),
        0xC0.toByte(),
        0x80.toByte(),
        0x41.toByte(),
        0x01.toByte(),
        0xC0.toByte(),
        0x80.toByte(),
        0x41.toByte(),
        0x00.toByte(),
        0xC1.toByte(),
        0x81.toByte(),
        0x40.toByte(),
        // ...（数组的其余部分）
    )

    private val crc16_tab_l = byteArrayOf(
        0x00.toByte(),
        0xC0.toByte(),
        0xC1.toByte(),
        0x01.toByte(),
        0xC3.toByte(),
        0x03.toByte(),
        0x02.toByte(),
        0xC2.toByte(),
        0xC6.toByte(),
        0x06.toByte(),
        0x07.toByte(),
        0xC7.toByte(),
        0x05.toByte(),
        0xC5.toByte(),
        0xC4.toByte(),
        0x04.toByte(),
        // ...（数组的其余部分）
    )

    fun getCRC(data : String) : String {
        val newData : String = data.replace(" ", "")
        val len = newData.length
        if (len % 2 != 0) {
            return "0000"
        }
        val num = len / 2
        val para = ByteArray(num)
        for (i in 0 until num) {
            val value = Integer.valueOf(newData.substring(i * 2, 2 * (i + 1)), 16)
            para[i] = value.toByte()
        }
        return getCRC(para)
    }

    /**
     * 计算CRC16校验码
     *
     * @param bytes
     * 字节数组
     * @return [String] 校验码
     * @since 1.0
     */
    fun getCRC(bytes : ByteArray) : String {
        // CRC寄存器全为1
        var CRC = 0x0000ffff
        // 多项式校验值
        val POLYNOMIAL = 0x0000a001
        var i : Int
        var j : Int
        i = 0
        while (i < bytes.size) {
            CRC = CRC xor (bytes[i].toInt() and 0x000000ff)
            j = 0
            while (j < 8) {
                if (CRC and 0x00000001 != 0) {
                    CRC = CRC shr 1
                    CRC = CRC xor POLYNOMIAL
                } else {
                    CRC = CRC shr 1
                }
                j ++
            }
            i ++
        }
        // 结果转换为16进制
        var result = Integer.toHexString(CRC).uppercase(Locale.getDefault())
        if (result.length != 4) {
            val sb = StringBuffer("0000")
            result = sb.replace(4 - result.length, 4, result).toString()
        }
        //高位在前地位在后
        //return result.substring(2, 4) + " " + result.substring(0, 2);
        // 交换高低位，低位在前高位在后
        return result.substring(2, 4) + result.substring(0, 2)
    }

    /**
     * 计算CRC16校验
     *
     * @param data 需要计算校验和的数组
     * @return CRC16校验和值
     */
    fun compute(data : ByteArray) : Int {
        return compute(data, 0, data.size)
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算校验和的数组
     * @param offset 起始位置
     * @param len    长度
     * @return CRC16校验和值
     */
    fun compute(data : ByteArray, offset : Int, len : Int) : Int {
        return compute(data, offset, len, 0xffff)
    }

    /**
     * 计算CRC16校验
     *
     * @param data   需要计算校验和的数组
     * @param offset 起始位置
     * @param len    长度
     * @param preval 之前的校验和值
     * @return CRC16校验和值
     */
    fun compute(data : ByteArray, offset : Int, len : Int, preval : Int) : Int {
        var ucCRCHi = (preval and 0xff00) shr 8
        var ucCRCLo = preval and 0x00ff
        var iIndex : Int
        for (i in 0 until len) {
            iIndex = (ucCRCLo xor data[offset + i].toInt()) and 0x00ff
            ucCRCLo = (ucCRCHi xor crc16_tab_h[iIndex].toInt()).toByte().toInt()
            ucCRCHi = crc16_tab_l[iIndex].toInt()
        }
        return ((ucCRCHi and 0x00ff) shl 8 or (ucCRCLo and 0x00ff)) and 0xffff
    }
}



