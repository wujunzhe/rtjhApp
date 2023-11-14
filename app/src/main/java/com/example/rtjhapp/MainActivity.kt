package com.example.rtjhapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import com.example.rtjhapp.bottomModule.BottomFragment
import com.example.rtjhapp.databinding.ActivityMainBinding
import com.example.rtjhapp.timeModule.TimeFragment

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        binding!!.vAirCondition.tempCircle.setValue("30",36f)
//        binding!!.vAirCondition.humidityCircle.setValue("50",70f)
        setContentView(binding!!.root)

        // 在 Activity 的 onCreate 方法中添加以下代码
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())

        // 显示顶部时间模块
        showTopTime()

        // 显示中间部分
        showCenterContainer()

        // 显示底部模块
        showBottom()
    }

    /**
     *  加载时间模块
     */
    private fun showTopTime(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_time,TimeFragment())
            .commit()
    }

    /**
     *  加载底部模块
     */
    private fun showBottom() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerBottom,BottomFragment())
            .commit()
    }

    private fun showCenterContainer() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_center,CenterContainerFragment())
            .commit()
    }
}