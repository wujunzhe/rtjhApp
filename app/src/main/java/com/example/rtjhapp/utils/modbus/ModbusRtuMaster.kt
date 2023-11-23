package com.example.rtjhapp.utils.modbus

import tp.xmaihh.serialport.SerialHelper

class ModbusRtuMaster(private val serialHelper: SerialHelper) {

    /**
     * 组装modbus RTU消息帧
     * @param slave 从站号
     * @param functionCode 功能码
     * @param startingAddress 读取寄存器起始地址 / 写入寄存器地址 / 写入寄存器起始地址
     * @param quantityOfX 读取寄存器个数 / 写入寄存器个数
     * @param outputValue 需要写入单个寄存器的数值
     * @param outputValues 需要写入多个寄存器的值的数组
     * @return 将整个消息帧转换成byte[]
     * @throws ModbusError Modbus错误
     */
    @Synchronized
    private fun execute(
        slave: Int,
        functionCode: Int,
        startingAddress: Int,
        quantityOfX: Int,
        outputValue: Int,
        outputValues: IntArray?
    ) : ByteArray{
        // 检查参数是否符合协议规定
        require(!(slave < 0 || slave > 0xff)) {"Invalid slave $slave"}
        require(!(startingAddress < 0 || startingAddress > 0xffff)) {"Invalid starting_address $startingAddress"}
        require(!(quantityOfX < 1 || quantityOfX > 0xff)) { "Invalid quantity_of_x $quantityOfX" }

        //构造request
        val request = ByteArrayWriter()
        // 写入从站地址号
        request.writeInt8(slave)

        // 根据功能码组装数据区
        when (functionCode){
            ModbusFunction.READ_COILS,
            ModbusFunction.READ_DISCRETE_INPUTS,
            ModbusFunction.READ_INPUT_REGISTERS,
            ModbusFunction.READ_HOLDING_REGISTERS -> {
                request.writeInt8(functionCode)
                request.writeInt16(startingAddress)
                request.writeInt16(quantityOfX)
            }
            ModbusFunction.WRITE_SINGLE_COIL,
            ModbusFunction.WRITE_SINGLE_REGISTER -> {
                // 写单个寄存器指令
                var updateOutputValue = outputValue
                if(functionCode === ModbusFunction.WRITE_SINGLE_COIL && outputValue != 0) {
                    updateOutputValue = 0xFF00 // 如果为线圈寄存器（写1时为FF 00，写0时为00 00）
                }
                request.writeInt8(functionCode)
                request.writeInt16(startingAddress)
                request.writeInt16(updateOutputValue)
            }
            ModbusFunction.WRITE_COILS -> {
                // 写多个线圈寄存器
                request.writeInt8(functionCode)
                request.writeInt16(startingAddress)
                request.writeInt16(quantityOfX)

                // 计算写入字节数
                var writeByteCount = (quantityOfX / 8) + 1
                // 写入数量 == 8，则写入字节数为1
                if(quantityOfX % 8 == 0) {
                    writeByteCount -= 1
                }
                request.writeInt8(writeByteCount)

                var index = 0
                // 如果写入数量 > 8，则需要拆分开来
                var start = 0 // 数组开始位置
                var end = 7 // 数组结束位置
                val splitData = IntArray(8)
                // 循环写入拆分数组，知道剩下最后一个数组元素个数 <= 8 的数据
                while (writeByteCount > 1){
                    writeByteCount--
                    var sIndex = 0
                    for (i in start..end) {
                        splitData[sIndex++] = outputValues!![index++]
                    }
                    // 数据反转，对于是否需要反转要看你传过来的数据，如果高低位顺序正确则不用反转
                    splitData.reverse()
                    // 写入拆分数组
                    request.writeInt8(toDecimal(splitData))
                    start = index
                    end += 8
                }
                // 写入最后剩下的数据
                val last = quantityOfX - index
                val tData = IntArray(last)
                outputValues?.let { System.arraycopy(it, index, tData, 0, last) }
                // 数据反转 对于是否要反转要看你传过来的数据，如果高低位顺序正确则不用反转
                tData.reverse()
                request.writeInt8(toDecimal(tData))
            }
            ModbusFunction.WRITE_HOLDING_REGISTERS -> {
                // 写入多个保持寄存器
                request.writeInt8(functionCode)
                request.writeInt16(startingAddress)
                request.writeInt16(quantityOfX)
                request.writeInt8(2 * quantityOfX)
                // 写入数据
                for (v in outputValues!!){
                    request.writeInt16(v)
                }
            }
            else -> throw ModbusError(ModbusErrorType.ModbusFunctionNotSupportedError,"Not support function $functionCode")
        }
        var bytes = request.toByteArray()
        // 计算CRC校验码
        val crc = CRC16.compute(bytes)
        request.writeInt16Reversal(crc)
        bytes = request.toByteArray()
        return bytes
    }

    /**
     * 读多个线圈寄存器
     */
    fun readCoils(slave : Int, startingAddress : Int, numberOfPoints: Int){
        val sendBytes = execute(slave,ModbusFunction.READ_COILS,startingAddress,numberOfPoints,0,null)
        serialHelper.send(sendBytes)
    }

    /**
     * 读单个线圈寄存器
     */
    fun readCoil(slave: Int, address: Int){
        readCoils(slave,address,1)
    }

    /**
     * 读多个保持寄存器
     */
    fun readHoldingRegisters(slave : Int,startingAddress : Int,numberOfPoints : Int) {
        val sendBytes = execute(slave,ModbusFunction.READ_HOLDING_REGISTERS,startingAddress,numberOfPoints,0,null)
        serialHelper.send(sendBytes)
    }

    /**
     * 读单个保持寄存器
     */
    fun readHoldingRegister(slave : Int, address : Int){
        readHoldingRegisters(slave,address,1)
    }

    /**
     * 读多个输入寄存器
     */
    fun readInputRegisters(slave: Int, startAddress: Int, numberOfPoints: Int) {
        val sendBytes = execute(slave, ModbusFunction.READ_INPUT_REGISTERS, startAddress, numberOfPoints, 0, null)
        serialHelper.send(sendBytes)
    }

    // 读单个输入寄存器
    fun readInputRegister(slave: Int, address: Int) {
        readInputRegisters(slave, address, 1)
    }

    /**
     * 读多个离散输入寄存器
     */
    fun readDiscreteInputs(slave: Int, startAddress: Int, numberOfPoints: Int) {
        val sendBytes = execute(slave, ModbusFunction.READ_DISCRETE_INPUTS, startAddress, numberOfPoints, 0, null)
        serialHelper.send(sendBytes)
    }

    // 读单个离散输入寄存器
    fun readDiscreteInput(slave: Int, address: Int) {
        readDiscreteInputs(slave, address, 1)
    }

    /**
     * 写单个线圈寄存器
     */
    fun writeSingleCoil(slave: Int, address: Int, value: Boolean) {
        val sendBytes = execute(slave, ModbusFunction.WRITE_SINGLE_COIL, address, 1, if (value) 1 else 0, null)
        serialHelper.send(sendBytes)
    }

    /**
     * 写单个保持寄存器
     */
    fun writeSingleRegister(slave: Int, address: Int, value: Int) {
        val sendBytes = execute(slave, ModbusFunction.WRITE_SINGLE_REGISTER, address, 1, value, null)
        serialHelper.send(sendBytes)
    }

    /**
     * 写入多个保持寄存器
     */
    fun writeHoldingRegisters(slave: Int, address: Int, sCount: Int, data: IntArray) {
        val sendBytes = execute(slave, ModbusFunction.WRITE_HOLDING_REGISTERS, address, sCount, 0, data)
        serialHelper.send(sendBytes)
    }

    /**
     * 写入多个位
     */
    fun writeCoils(slave: Int, address: Int, bCount: Int, data: IntArray) {
        val sendBytes = execute(slave, ModbusFunction.WRITE_COILS, address, bCount, 0, data)
        serialHelper.send(sendBytes)
    }

    // 将数组反转
    private fun reverseArr(arr: IntArray): IntArray {
        val tem = IntArray(arr.size)
        for (i in arr.indices) {
            tem[i] = arr[arr.size - 1 - i]
        }
        return tem
    }

    // 将 int[1,0,0,1,1,0] 数组转换为十进制数据
    private fun toDecimal(data: IntArray): Int {
        var result = 0
        if (data.isNotEmpty()) {
            val sData = StringBuilder()
            for (d in data) {
                sData.append(d)
            }
            result = try {
                Integer.parseInt(sData.toString(), 2)
            } catch (e: NumberFormatException) {
                -1
            }
        }
        return result
    }
}