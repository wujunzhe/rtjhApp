package com.example.rtjhapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.rtjhapp.databinding.ActivityMainBinding

 class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.tvTest.text = "你好呀"
        // 在 Activity 的 onCreate 方法中添加以下代码
        val controller = window.insetsController
        controller?.hide(WindowInsets.Type.statusBars())
    }
}