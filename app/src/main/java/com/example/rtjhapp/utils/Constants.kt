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
        object DefaultSerialPort {
            const val call = "/dev/ttyS9"
            const val airCondition = "/dev/ttyS2"
            const val control = "/dev/ttyS0"
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
        object Order{ // 指令常量
            const val powerOn = "PowerOnOrder" // 开机指令
            const val powerOff = "PowerOffOrder" // 关机指令
        }
        object NeedCalculator{
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
            const val runningTime = "RunningTimeRegNum" // 运行时间
        }

        object Default {
            object Order {
                const val powerOn = "默认开机指令"
                const val powerOff = "默认关机指令"
            }
            object Register {
                const val returnHum = "默认回风湿度寄存器号"
                const val returnTem = "默认回风温度寄存器号"
                const val settingHum = "默认设置湿度寄存器号"
                const val settingTem = "默认设置湿度寄存器号"
                const val runningState = "默认运行状态寄存器号"
                const val unitFailure = "默认机组故障寄存器号"
                const val dutyMode = "默认值班模式寄存器号"
                const val neStressToggle = "默认正负压切换寄存器号"
                const val runningTime = "默认运势时间寄存器号"
            }
        }
    }

    /**
     * 常量-智能设置
     */
    object SmartSettings{
        const val disinfectTime = "DisinfectTime"
        const val powerOnDisinfect = "PowerOnDisinfect"
        object Default {
            const val disinfectTime = "0"
            const val powerOnDisinfect = false
        }
    }
}