package com.example.rtjhapp.bottomModule

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.MainActivity
import com.example.rtjhapp.adapter.BottomAdapter
import com.example.rtjhapp.databinding.BottomBinding
import com.example.rtjhapp.dialog.SettingDialog
import com.example.rtjhapp.event.UpdateBottomFragmentUIEvent
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BottomFragment: Fragment() {

    private lateinit var binding: BottomBinding
    private lateinit var adapter: BottomAdapter
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = BottomBinding.inflate(inflater,container,false)
        adapter = BottomAdapter(binding)
        adapter.isShow()
        // 启动定时任务
        if (adapter.serialIsOpen()){
            startStatusPolling()
        }
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 监听综合控制按钮
        val integratedControlBtn = binding.integratedControl
        integratedControlBtn.setOnClickListener {
            showSettingDialog(requireContext())
        }


    }

    /**
     * 显示设置对话框
     * @param context
     */
    private fun showSettingDialog(context : Context){
        val dialog = SettingDialog(context)
        dialog.show()
    }

    private fun startStatusPolling(){
        handler.postDelayed(object : Runnable {
            override fun run() {
                adapter.getStatus()
                handler.postDelayed(this,6000)
            }
        },6000)
    }

    @Subscribe
    fun upDateUI(event:UpdateBottomFragmentUIEvent){
        adapter.isShow()
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}