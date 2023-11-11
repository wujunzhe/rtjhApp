package com.example.rtjhapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import com.example.rtjhapp.databinding.ActivityMainBinding

 class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding!!.vAirCondition.tempCircle.setValue("30",36f)
        binding!!.vAirCondition.humidityCircle.setValue("50",70f)
        setContentView(binding!!.root)

        // 在 Activity 的 onCreate 方法中添加以下代码
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())
    }
}