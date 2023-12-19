package com.example.rtjhapp.timeModule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.TopTimeBinding
import com.example.rtjhapp.dialog.TimeDialog
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.GlobalData
import com.example.rtjhapp.utils.GlobalData.mzPd
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.example.rtjhapp.utils.VTOSerialHelper
import java.time.LocalDate
import java.time.LocalTime
import java.util.Timer
import java.util.TimerTask

class TimeFragment : Fragment() {
    private var mHandler : Handler = Handler()
    private var ssStartTime = 0//手术计时介质
    private var ssTimeBuff = 0//辅助
    private var mzStartTime = 0//麻醉计时介质
    private var mzTimeBuff = 0//辅助

    private var ssKaisiZt = false//正计时开始状态
    private var ssZhantZt = false//正计时暂停状态
    private var ssFuweiZt = false//正计时复位状态

    private var mzKaisiZt = false//麻醉开始状态
    private var mzZhantZt = false//麻醉暂停状态
    private var mzFuweiZt = false//麻醉复位状态

    private lateinit var binding : TopTimeBinding

    //手术正计时按钮获取+视图
    private lateinit var ssTimeStart : ImageButton
    private lateinit var ssTimeBackHome : ImageButton
    private lateinit var ssTimeWait : ImageButton
    private lateinit var ssShi : TextView
    private lateinit var ssFen : TextView
    private lateinit var ssMiao : TextView

    //手术正计时按钮获取+视图
    private lateinit var mzTimeStart : ImageButton
    private lateinit var mzTimeBackHome : ImageButton
    private lateinit var mzTimeWait : ImageButton
    private lateinit var mzShi : TextView
    private lateinit var mzFen : TextView
    private lateinit var mzMiao : TextView

    private lateinit var mzSetTime : LinearLayout

    private lateinit var runnable1 : Runnable
    private lateinit var runnable2 : Runnable

    private lateinit var sharedPreferencesManager : SharedPreferencesManager
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = TopTimeBinding.inflate(inflater, container, false)
        //手术计时视图时分秒
        ssShi = binding.surgeryTimingHours//视图：时
        ssFen = binding.surgeryTimingMinutes//视图：分
        ssMiao = binding.surgeryTimingSeconds//视图：秒
        ssTimeStart = binding.surgeryTimingStart//按钮：计时开始
        ssTimeBackHome = binding.surgeryTimingReset//按钮：计时复位
        ssTimeWait = binding.surgeryTimingStop//按钮：计时暂停
        //麻醉计时视图时分秒
        mzShi = binding.anaesthesiaTimingHours//视图：时
        mzFen = binding.anaesthesiaTimingMinutes//视图：分
        mzMiao = binding.anaesthesiaTimingSeconds//视图：秒
        mzTimeStart = binding.anaesthesiaTimingStart//按钮：麻醉开始
        mzTimeBackHome = binding.anaesthesiaTimingReset//按钮：麻醉复位
        mzTimeWait = binding.anaesthesiaTimingStop//按钮：麻醉暂停

        mzSetTime = binding.mzTimingStart//麻醉界面

        //手术正计时线程
        runnable1 = object : Runnable {
            override fun run() {
                ssStartTime ++
                val seconds = ssStartTime % 60 //秒
                val minutes = ssStartTime / 60 % 60 //分
                val hours = ssStartTime / 60 / 60 % 60//时
                val shi = String.format("%02d", hours)
                val fen = String.format("%02d", minutes)
                val miao = String.format("%02d", seconds)
                ssShi.text = shi
                ssFen.text = fen
                ssMiao.text = miao
                mHandler.postDelayed(this, 1000)
            }
        }
        //麻醉计时线程
        runnable2 = object : Runnable {
            override fun run() {
                mzStartTime --
                if (mzStartTime < 0) {
                    mzKaisiZt = false
                    mzZhantZt = false
                    mzFuweiZt = false
                    GlobalData.mzStartTime = 0
                    GlobalData.mzTimeBuff = 0
                    mzStartTime = 0
                    mzTimeBuff = 0
                    return
                }
                val seconds = mzStartTime % 60 //秒
                val minutes = mzStartTime / 60 % 60 //分
                val hours = mzStartTime / 60 / 60 % 60//时
                val shi = String.format("%02d", hours)
                val fen = String.format("%02d", minutes)
                val miao = String.format("%02d", seconds)
                mzShi.text = shi
                mzFen.text = fen
                mzMiao.text = miao
                mHandler.postDelayed(this, 1000)
                println(mzStartTime)
            }
        }
        //加载计时功能+当前时间
        togoStart(binding)
        toGetNowTime()

        sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        return binding.root//返回视图控制权限
    }

    //--------------------------------------当前时间------------------------------------------------------
    private fun toGetNowTime() {
        val timer = Timer()
        //当前时间时分秒
        val itShi = binding.localTimingHours
        val itFen = binding.localTimingMinutes
        val itMiao = binding.localTimingSeconds
        val years = binding.localTimingYears

        val handler = Handler(Looper.getMainLooper())
        val timerTask = object : TimerTask() {
            override fun run() {
                val nowTime = LocalTime.now()
                val nowDay = LocalDate.now()
                val shi = String.format("%02d", nowTime.hour)
                val fen = String.format("%02d", nowTime.minute)
                val miao = String.format("%02d", nowTime.second)
                val nian = nowDay.year
                val months = nowDay.monthValue
                val days = nowDay.dayOfMonth
                //轮询检查是否已设置麻醉时长
                val msys = GlobalData.mzStartTime !!
                if (mzPd !!) {
                    val seconds = msys % 60 //秒
                    val minutes = msys / 60 % 60 //分
                    val hours = msys / 60 / 60 % 60//时
                    val shi = String.format("%02d", hours)
                    val fen = String.format("%02d", minutes)
                    val miao = String.format("%02d", seconds)
                    handler.post {
                        // 在 UI 线程上运行的代码
                        mzShi.text = shi
                        mzFen.text = fen
                        mzMiao.text = miao
                    }
                }
                handler.post {
                    // 在 UI 线程上运行的代码
                    itShi.text = shi
                    itFen.text = fen
                    itMiao.text = miao
                    years.text = nian.toString() + "年" + months + "月" + days + "日"
                }
            }
        }
        timer.schedule(timerTask, 0, 1000)
    }

    //--------------------------------------按钮监听------------------------------------------------------
    //手术正计时按钮监听
    private fun togoStart(binding : TopTimeBinding) {

        //监听开始按钮：开始功能
        ssTimeStart.setOnClickListener {
            if (ssKaisiZt) {//如果开始程序在运行
                if (ssZhantZt) {//如果程序暂停了
                    ssZhantZt = false
                    ssKaisiZt = true
                    start1(runnable1)
                } else {
                    println("禁止访问，因为程序已经开始")
                }
            } else {
                ssKaisiZt = true
                ssTimeBuff = 0
                start1(runnable1)
                println("开始运行计时")
            }
        }
        //监听重置按钮：计时重置功能
        ssTimeBackHome.setOnClickListener {
            ssKaisiZt = false//正计时开始状态
            ssZhantZt = false//正计时暂停状态
            ssFuweiZt = false//正计时复位状态
            ssStartTime = 0
            ssTimeBuff = 0
            rest1(ssShi, ssFen, ssMiao, runnable1)
            println("已经全部复位")
        }
        //监听暂停按钮：计时暂停功能
        ssTimeWait.setOnClickListener {
            if (ssKaisiZt) {//如果程序已开始
                if (! ssZhantZt) {
                    ssTimeBuff = ssStartTime
                    ssStartTime = 0
                    ssZhantZt = true
                    println("暂停程序")
                } else {
                    println("重复点击")
                }
                pause1(runnable1)
            } else {
                println("程序尚为启动")
            }
        }
//-------------------------------------------------------------------------------------------------
        //麻醉开始按钮：麻醉计时开始
        mzTimeStart.setOnClickListener {
            val yys = GlobalData.mzStartTime !!
            val yyd = GlobalData.mzTimeBuff !!
            if (yys == 0) {
                println("禁止开始，尚未设置时间")
            } else {
                mzPd = false
                if (mzKaisiZt) {//如果开始程序在运行
                    if (mzZhantZt) {//如果程序暂停了
                        mzZhantZt = false
                        mzKaisiZt = true
                        start2(runnable2)
                    } else {
                        println("禁止访问，因为程序已经开始")
                    }
                } else {
                    mzKaisiZt = true
                    mzStartTime = yys
                    mzTimeBuff = yyd
                    mzTimeBuff = mzStartTime
                    start2(runnable2)
                    println("开始运行计时")
                }
            }
        }
        //麻醉重置按钮：麻醉重置功能
        mzTimeBackHome.setOnClickListener {
            mzKaisiZt = false//正计时开始状态
            mzZhantZt = false//正计时暂停状态
            mzFuweiZt = false//正计时复位状态
            mzStartTime = 120
            mzTimeBuff = 120
            rest2(mzShi, mzFen, mzMiao, runnable2)
            println("已经全部复位")
        }
        //麻醉暂停按钮：麻醉暂停功能
        mzTimeWait.setOnClickListener {
            if (mzKaisiZt) {//如果程序已开始
                if (! mzZhantZt) {
                    mzTimeBuff = mzStartTime
                    mzStartTime = 0
                    mzZhantZt = true
                    println("暂停程序")
                } else {
                    println("重复点击")
                }
                pause2(runnable2)
            } else {
                println("程序尚为启动")
            }
        }
        //设置麻醉计算时间
        mzSetTime.setOnClickListener {
            if (mzKaisiZt) {
                println("禁止设置时间")
            } else {
                val dialog = TimeDialog(binding.root.context)
                dialog.show()

            }

        }
    }


    //--------------------------------正计时功能------------------------------------------------------
    private fun start1(runnable : Runnable) {//开始
        ssStartTime = ssTimeBuff
        mHandler.postDelayed(runnable, 0)
    }

    private fun pause1(runnable : Runnable) {//暂停
        mHandler.removeCallbacks(runnable)
    }

    @SuppressLint("SetTextI18n")
    private fun rest1(shi : TextView, fen : TextView, miao : TextView, runnable : Runnable) {//复位
        shi.text = "00"
        fen.text = "00"
        miao.text = "00"
        mHandler.removeCallbacks(runnable)
    }

    //------------------------------------麻醉功能-------------------------------------------------------
    private fun start2(runnable : Runnable) {//麻醉开始
        mzStartTime = mzTimeBuff
        mHandler.postDelayed(runnable, 0)
    }

    private fun pause2(runnable : Runnable) {//麻醉暂停
        mHandler.removeCallbacks(runnable)
    }

    @SuppressLint("SetTextI18n")
    private fun rest2(shi : TextView, fen : TextView, miao : TextView, runnable : Runnable) {//麻醉复位
        shi.text = "00"
        fen.text = "00"
        miao.text = "00"
        GlobalData.mzStartTime = 0
        GlobalData.mzTimeBuff = 0
        mHandler.removeCallbacks(runnable)
        println("已停止该进程")
    }
}