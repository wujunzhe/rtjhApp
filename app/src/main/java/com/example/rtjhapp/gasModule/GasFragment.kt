package com.example.rtjhapp.gasModule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.adapter.GasAdapter
import com.example.rtjhapp.databinding.MedicalStressStatusBinding
import com.example.rtjhapp.utils.GlobalData

class GasFragment : Fragment() {
    private lateinit var binding : MedicalStressStatusBinding
    private lateinit var adapter : GasAdapter
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = MedicalStressStatusBinding.inflate(inflater, container, false)
        adapter = GasAdapter(binding)
        // 启动定时任务
        startStatusPolling()
        return binding.root
    }

    private fun startStatusPolling() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                adapter.setStatus(GlobalData.airConditionStatusHex)
                handler.postDelayed(this, 6000)
            }
        }, 6000)
    }
}