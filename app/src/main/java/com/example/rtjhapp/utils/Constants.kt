package com.example.rtjhapp.utils

object Constants {
    /**
     * 常量-串口
     */
    object SerialPort {
        const val call = "CallSerialPort" // 电话模块
        const val airCondition = "AirConditionSerialPort" // 空调模块
        const val control = "ControlSerialPort" // 控制模块
        const val music = "MusicSerialPort" // 音乐模块

        /**
         * 常量-默认串口号
         */
        object Default {
            const val call = "/dev/ttyS9"
            const val airCondition = "/dev/ttyS0"
            const val control = "/dev/ttyS2"
            const val music = "/dev/ttyS5"
        }
    }

    /**
     * 常量-显示控制
     */
    object DisplayState {
        const val duty = "DutyDisplayState" // 值班
        const val neStress = "NeGeStressToggleDisplayState" // 正负压
        const val differentGauge = "DifferentGaugeDisplayState" // 压差表
        const val spare1 = "Spare1DisplayState" // 备用1
        const val spare2 = "Spare2DisplayState" // 备用1
        const val spare3 = "Spare3DisplayState" // 备用1
        const val spare4 = "Spare4DisplayState" // 备用1
    }

    /**
     * 常量-空调设置
     */
    object AirConditionSettings {
        object Order { // 指令常量
            const val powerOn = "PowerOnOrder" // 开机指令
            const val powerOff = "PowerOffOrder" // 关机指令
            const val getStatus = "GetAirConditionStatus" // 获取状态指令
        }

        object NeedCalculator {
            const val returnTem = "ReturnTemNeedDivision"
            const val returnHum = "ReturnHumNeedDivision"
            const val settingHum = "SettingHumNeedMulti"
            const val settingTem = "SettingTemNeedMulti"
        }

        object Register { // 寄存器常量
            const val returnHum = "ReturnHumRegNum" // 回风湿度
            const val returnTem = "ReturnTemRegNum" // 回风温度
            const val settingHum = "SettingHumRegNum" // 设置湿度
            const val settingTem = "SettingTemRegNum" // 设置温度
            const val runningState = "RunningStateRegNum" // 运行状态
            const val unitFailure = "UnitFailureRegNum" // 机组故障
            const val dutyMode = "DutyModeRegNum" // 值班模式
            const val neStressToggle = "NeGeStressToggleRegNum" // 正负压切换
            const val systemError = "SystemError" // 机组报警
            const val runningTime = "RunningTimeRegNum" // 运行时间
        }

        object Max {
            const val hum = "HumMax"
            const val tem = "TemMax"
        }

        object Min {
            const val hum = "HumMin"
            const val tem = "TemMin"
        }

        object Default {
            object Order {
                const val powerOn = "默认开机指令"
                const val powerOff = "默认关机指令"
                const val getStatus = "0103000200156407"
            }

            object Register {
                const val returnHum = "2"
                const val returnTem = "1"
                const val settingHum = "4"
                const val settingTem = "3"
                const val runningState = "9"
                const val unitFailure = "6"
                const val dutyMode = "5"
                const val neStressToggle = "8"
                const val systemError = "7"
                const val runningTime = "默认运势时间寄存器号"
                const val oxygenStatus = "10" // 氧气
                const val negativePressureStatus = "11" // 负压吸引
                const val pressurizedAirStatus = "12" // 压缩空气
                const val carbonDioxideStatus = "13" // 二氧化碳
                const val nitrogenStatus = "14" // 氮气
                const val nitrousOxideStatus = "15" // 笑气
                const val hydrogenStatus = "16" // 氢气
            }

            object Max {
                const val hum = "80"
                const val tem = "30"
            }

            object Min {
                const val hum = "50"
                const val tem = "23"
            }
        }
    }

    /**
     * 常量-智能设置
     */
    object SmartSettings {
        const val disinfectTime = "DisinfectTime"
        const val powerOnDisinfect = "PowerOnDisinfect"
        const val lightDelayTime = "LightDelayTime"

        object Default {
            const val disinfectTime = "0"
            const val powerOnDisinfect = false
            const val lightDelayTime = "0"
        }
    }

    /**
     * 常量-串口设置一些默认值
     */
    object SerialPortDefaultConfig {
        const val baudRate = 9600
        const val dataBits = 8
        const val parity = 0
    }

    /**
     * 常量-继电器下发指令
     */
    object ControlOrder {
        object Open {
            const val lights1Hex = "FF050000FF0099E4" // 打开照明1
            const val lights2Hex = "FF050001FF00C824" // 打开照明2
            const val viewingLightsHex = "FF050002FF003824" // 打开观片灯
            const val noShadowLightsHex = "FF050003FF0069E4" // 打开无影灯
            const val surgicalLightsHex = "FF050004FF00D825" // 打开手术灯
            const val exhaustFanHex = "FF050005FF0089E5" // 打开排风机
            const val spare1Hex = "FF050006FF0079E5" // 打开备用1
            const val disinfectHex = "FF050007FF002825" // 打开消毒
        }

        object Close {
            const val lights1Hex = "FF0500000000D814" // 关闭照明1
            const val lights2Hex = "FF050001000089D4" // 关闭照明2
            const val viewingLightsHex = "FF050002000079D4" // 关闭观片灯
            const val noShadowLightsHex = "FF05000300002814" // 关闭无影灯
            const val surgicalLightsHex = "FF050004000099D5" // 关闭手术灯
            const val exhaustFanHex = "FF0500050000C815" // 关闭排风机
            const val spare1Hex = "FF05000600003815" // 关闭备用
            const val disinfectHex = "FF050007000069D5" // 关闭消毒
        }

        object Get {
            const val controlStatusHex = "FF03000A000871D0" // 获取继电器状态
            const val version = "FF0300FF0001A1E4" // 读取版本号
        }

        object StartWith {
            const val getStatus = "FF0310"
        }
    }

    /**
     * 常量-空调指令
     */
    object AirConditionOrder {
        object StartWith {
            const val getStatus = "0103"
        }

        object AirCondition {
            const val dutyStart = "" // 空调值班启动
            const val dutyStop = "" // 空调值班停止
            const val start = "01050000ff00" // 空调启动
            const val stop = "010500000000" // 空调停止
            const val neStress = "" // 正负压正
            const val geStress = "" // 正负压负
        }
    }

}