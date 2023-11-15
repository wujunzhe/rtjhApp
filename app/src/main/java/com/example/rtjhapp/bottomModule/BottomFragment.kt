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

class BottomFragment: Fragment() {

    private lateinit var binding: BottomBinding
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = BottomBinding.inflate(inflater,container,false)
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
}