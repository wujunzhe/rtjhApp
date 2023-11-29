package com.example.rtjhapp.airContidionModule

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.adapter.AirConditionAdapter
import com.example.rtjhapp.databinding.AirConditionBinding
import com.example.rtjhapp.event.UpdateAirConditionFragmentUIEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AirConditionFragment : Fragment() {
    private lateinit var binding : AirConditionBinding
    private lateinit var adapter : AirConditionAdapter
    private var handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = AirConditionBinding.inflate(inflater, container, false)
        adapter = AirConditionAdapter(binding)
        if (adapter.serialPortIsOpen()) {
            startStatusPolling()
        }
        return binding.root
    }

    private fun startStatusPolling() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                adapter.getStatus()
                handler.postDelayed(this, 6000)
            }
        }, 6000)
    }

    @Subscribe
    fun onUpdateUI(event : UpdateAirConditionFragmentUIEvent) {
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