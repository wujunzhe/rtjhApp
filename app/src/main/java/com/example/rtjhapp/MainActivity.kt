package com.example.rtjhapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.example.rtjhapp.bottomModule.BottomFragment
import com.example.rtjhapp.databinding.ActivityMainBinding
import com.example.rtjhapp.debugModule.DebugFragment
import com.example.rtjhapp.timeModule.TimeFragment
import com.example.rtjhapp.titleModule.TitleFragment
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.GlobalData
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.OnDataReceivedListener
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.example.rtjhapp.utils.VTOSerialHelper

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private lateinit var sharedPreferencesManager : SharedPreferencesManager
    private var clickCount = 0
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding !!.root)
        sharedPreferencesManager = SharedPreferencesManager(binding!!.root.context)


        // 在 Activity 的 onCreate 方法中添加以下代码
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())

        // 显示顶部时间模块
        showTopTime()

        // 显示中间部分
        showCenterContainer()

        // 显示底部模块
        showBottom()

        // 显示调试模块
        showDebug()
        binding !!.containerTitle.setOnClickListener {
            clickCount ++
            if (clickCount >= 10) {
                binding !!.debugListLayout.visibility = View.VISIBLE
            }
            if (clickCount >= 20) {
                binding !!.debugListLayout.visibility = View.GONE
                clickCount = 0
            }
        }

        showTitle()

        initVtoHex()
    }

    private fun showTitle() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_title, TitleFragment())
            .commit()
    }

    private fun showDebug() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.debug_list_layout, DebugFragment())
            .commit()
    }

    /**
     *  加载时间模块
     */
    private fun showTopTime() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_time, TimeFragment())
            .commit()
    }

    /**
     *  加载底部模块
     */
    private fun showBottom() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerBottom, BottomFragment())
            .commit()
    }

    private fun showCenterContainer() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_center, CenterContainerFragment())
            .commit()
    }

    private fun initVtoHex() {
        val defaultHex = "0".repeat(44)
        sharedPreferencesManager.writeString("VTOHex", defaultHex)
    }
}