package com.example.rtjhapp

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.example.rtjhapp.module.bottomModule.BottomFragment
import com.example.rtjhapp.databinding.ActivityMainBinding
import com.example.rtjhapp.module.debugModule.DebugFragment
import com.example.rtjhapp.module.timeModule.TimeFragment
import com.example.rtjhapp.module.titleModule.TitleFragment

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private var clickCount = 0
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding !!.root)

        // 隐藏顶部状态栏
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

}