package com.example.rtjhapp.timeModule

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.TopTimeBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class TimeFragment: Fragment() {
    private var mHandler:Handler = Handler()

    private var millisecondsRecord1 = 0L
    private var startTime1 = 0L
    private var timeBuff1 = 0L


    private var pauseTimeInMilliseconds = 0L

    private var millisecondsRecord2 = 0L
    private var startTime2 = 5L
    private var timeBuff2 = 0L
    private var pd1 = false
    private var pd2 = false
    private var dd1 = false
    private var dd2 = false

    private lateinit var binding: TopTimeBinding
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?

    ) : View {
       binding = TopTimeBinding.inflate(inflater,container,false)
        //计时功能
        togoStart(binding)
        toGetNowTime()
        return binding.root
    }
//------------------------------------当前时间方法----------------------------------------------------
    private fun toGetNowTime(){
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
                val shi = String.format("%02d",nowTime.hour)
                val fen = String.format("%02d",nowTime.minute)
                val miao = String.format("%02d",nowTime.second)
                val nian = nowDay.year
                val months = nowDay.monthValue
                val days = nowDay.dayOfMonth
                handler.post {
                    // 在 UI 线程上运行的代码
                    itShi.text = shi
                    itFen.text = fen
                    itMiao.text = miao
                    years.text = nian.toString()+"年"+months+"月"+days+"日"
                }

            }
        }
        timer.schedule(timerTask, 0, 1000)
    }
//------------------------------------两个计时方法----------------------------------------------------
    //手术正计时
    private fun togoStart(binding: TopTimeBinding) {
        //手术正计时按钮获取
        val ssTimeStart = binding.surgeryTimingStart//开始按钮
        val ssTimeBackHome = binding.surgeryTimingReset//重置按钮
        val ssTimeWait = binding.surgeryTimingStop//暂停按钮

        //手术计时视图时分秒
        val ssShi = binding.surgeryTimingHours
        val ssFen = binding.surgeryTimingMinutes
        val ssMiao = binding.surgeryTimingSeconds
        //计时方法
        val runnable1 = object: Runnable{
            override fun run() {
                millisecondsRecord1 = SystemClock.uptimeMillis() - startTime1
                val accumulatedTime = timeBuff1 + millisecondsRecord1
                val seconds = accumulatedTime / 1000 % 60//秒
                val minutes = accumulatedTime / 1000 / 60 % 60 //分
                val hours = accumulatedTime / 1000 / 60 /60 % 60//时
                val shi = String.format("%02d",hours)
                val fen = String.format("%02d",minutes)
                val miao = String.format("%02d",seconds)
                ssShi.text = shi
                ssFen.text = fen
                ssMiao.text = miao
                mHandler.postDelayed(this,0)
            }
        }
        //监听开始按钮：开始功能
        ssTimeStart.setOnClickListener {
            if(!pd1){
                start1(runnable1)
                println("开始计时")
                pd1 = true
                dd1 = false
            }else{
                pause1(runnable1)
                pd1 = false
                dd1 = true
            }
        }
        //监听重置按钮：计时重置功能
        ssTimeBackHome.setOnClickListener {
            rest1(ssShi,ssFen,ssMiao)
            println("重置计时")
        }
        //监听暂停按钮：计时暂停功能
        ssTimeWait.setOnClickListener {
            pause1(runnable1)
            println("暂停计时")
            pd1 = false
            dd1 = true
        }
//-------------------------------------------------------------------------------------------------
        //麻醉正计时按钮
        val mzTimeStart = binding.anaesthesiaTimingStart//开始按钮
        val mzTimeBackHome = binding.anaesthesiaTimingReset//重置按钮
        val mzTimeWait = binding.anaesthesiaTimingStop//暂停按钮

        //麻醉计时视图时分秒控件
        val mzShi = binding.anaesthesiaTimingHours
        val mzFen = binding.anaesthesiaTimingMinutes
        val mzMiao = binding.anaesthesiaTimingSeconds

    // 倒计时相关变量
        var targetTimeInMilliseconds = 10000L
        var handler = Handler(Looper.getMainLooper())
        val runnable2 = object : Runnable {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                val remainingTime = targetTimeInMilliseconds - currentTime

                val hours = (remainingTime / DateUtils.HOUR_IN_MILLIS) % 24
                val minutes = (remainingTime / DateUtils.MINUTE_IN_MILLIS) % 60
                val seconds = (remainingTime / DateUtils.SECOND_IN_MILLIS) % 60

                println(remainingTime)

                if (remainingTime > 0) {
                    ssShi.text = hours.toString()
                    ssFen.text = minutes.toString()
                    ssMiao.text = seconds.toString()
                    handler.postDelayed(this, DateUtils.SECOND_IN_MILLIS)
                    println("222")
                } else {
                    ssShi.text = "00"
                    ssFen.text = "00"
                    ssMiao.text = "00"
                    handler.removeCallbacks(this)
                    println("333")
                }
            }
        }
        //麻醉开始按钮：麻醉功能
        mzTimeStart.setOnClickListener {
            if(!pd2){
                start2(runnable2)
                println("开始计时")
                pd2 = true
                dd2 = false
            }else{
                pause2(runnable2)
                pd2 = false
                dd1 = true
            }
        }
        //麻醉重置按钮：麻醉重置功能
        mzTimeBackHome.setOnClickListener {
            reset2(mzShi,mzFen,mzMiao)
            println("重置计时")
        }
        //麻醉暂停按钮：麻醉暂停功能
        mzTimeWait.setOnClickListener {
            pause2(runnable2)
            println("暂停计时")
            pd2 = false
            dd2 = true
        }
    }
    //--------------------------------正计时部分------------------------------------------------------
    private fun start1(runnable:Runnable){
        startTime1 = SystemClock.uptimeMillis()
        mHandler.postDelayed(runnable,0)
    }
    private fun pause1(runnable:Runnable){
        if(!dd1){
            timeBuff1 += millisecondsRecord1
            mHandler.removeCallbacks(runnable)
            println("改变状态，判断开始")
            println(timeBuff1)
            dd1 = true
        }else{
            println("禁止访问")
        }
    }
    @SuppressLint("SetTextI18n")
    private fun rest1(shi:TextView, fen:TextView, miao:TextView){
        if(pd1){
            println("请暂停计时后，再重置")
        }else{
            millisecondsRecord1 = 0L
            timeBuff1 = 0L
            shi.text = "00"
            fen.text = "00"
            miao.text = "00"
        }
    }
//------------------------------------麻醉部分-------------------------------------------------------
    private fun start2(runnable:Runnable){
        pauseTimeInMilliseconds = System.currentTimeMillis()
        mHandler.postDelayed(runnable,0)
    }
    private fun pause2(runnable:Runnable){
        if(!dd2){
            timeBuff2 += millisecondsRecord2
            mHandler.removeCallbacks(runnable)
            println("改变状态，判断开始")
            dd2 = true
        }else{
            println("不可同步操作")
        }
    }
    @SuppressLint("SetTextI18n")
    private fun reset2(shi:TextView, fen:TextView, miao:TextView){
        if(pd2){
            println("请暂停计时后，再重置")
        }else{
            millisecondsRecord2 = 0L//设置的计时
            timeBuff2 = 0L//衰减的计时
            shi.text = "00"
            fen.text = "00"
            miao.text = "00"
        }
    }
}