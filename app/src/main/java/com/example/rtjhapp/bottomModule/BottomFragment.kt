package com.example.rtjhapp.bottomModule

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.MainActivity
import com.example.rtjhapp.databinding.BottomBinding
import com.example.rtjhapp.dialog.SettingDialog
import com.example.rtjhapp.event.UpdateBottomFragmentUIEvent
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.SharedPreferencesManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class BottomFragment: Fragment() {

    private lateinit var binding: BottomBinding
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = BottomBinding.inflate(inflater,container,false)
        isShow()
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

    private fun isShow(){
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val layoutMap = mapOf(
            binding.differentGauge to Constants.DisplayState.differentGauge,
            binding.spare1Layout to Constants.DisplayState.spare1,
            binding.spare2Layout to Constants.DisplayState.spare2,
            binding.spare3Layout to Constants.DisplayState.spare3,
            binding.spare4Layout to Constants.DisplayState.spare4
        )
        layoutMap.forEach{(layout,displayState) ->
            val display = sharedPreferencesManager.readBoolean(displayState,true)
            layout.visibility = if (!display) View.GONE else View.VISIBLE
        }
    }

    @Subscribe
    fun upDateUI(event:UpdateBottomFragmentUIEvent){
        isShow()
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