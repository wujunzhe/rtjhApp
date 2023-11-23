package com.example.rtjhapp.utils.modbus

import com.example.rtjhapp.utils.ByteUtil
import java.io.ByteArrayOutputStream

class ByteArrayWriter: ByteArrayOutputStream() {
    fun writeInt8(b:Byte) {
        this.write(b.toInt())
    }

    fun writeInt8(b: Int) {
        this.write(b.toByte().toInt())
    }

    fun writeInt16(n: Int){
        val bytes = ByteUtil.fromInt16(n)
        this.write(bytes, 0, bytes.size)
    }

    fun writeInt16Reversal(n: Int){
        val bytes = ByteUtil.fromInt16Reversal(n)
        this.write(bytes, 0, bytes.size)
    }

    fun writeInt32(n: Int) {
        val bytes = ByteUtil.fromInt32(n)
        this.write(bytes, 0, bytes.size)
    }

    fun writeBytes(bs: ByteArray, len: Int) {
        this.write(bs, 0, len)
    }
}