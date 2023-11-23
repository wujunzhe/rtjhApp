package com.example.rtjhapp.utils.modbus

import java.lang.Exception

class ModbusError(val code: Int, message: String?) : Exception(
    if (! message.isNullOrEmpty()) message else "Modbus Error: Exception code = $code"
) {

    constructor(code: Int) : this(code, null)

    constructor(type: ModbusErrorType, message: String?) : this(type.ordinal, message)

    constructor(message: String?) : this(-1, message)

    // 其余部分的代码...
}
