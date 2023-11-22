package com.example.rtjhapp.airContidionModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.AirConditionBinding
import com.example.rtjhapp.event.UpdateAirConditionFragmentUIEvent
import com.example.rtjhapp.utils.SharedPreferencesManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AirConditionFragment: Fragment() {
    private lateinit var binding: AirConditionBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = AirConditionBinding.inflate(inflater,container,false)
        return binding.root
    }

    @Subscribe
    fun onUpdateUI(event: UpdateAirConditionFragmentUIEvent){
        val dutyBtn = binding.airConditionDutyBtn
        val dutyImg = binding.dutyRunningImg
        val dutyText = binding.dutyRunningText
        val neBtn = binding.positiveNegativePressureBtn
        val neImg = binding.neRunningImg
        val neText = binding.neRunningText
        val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val showDutyFlag = sharedPreferencesManager.readBoolean("DutyDisplayState",true)
        val showNeFlag = sharedPreferencesManager.readBoolean("NeGeStressToggleDisplayState",true)

        if (!showDutyFlag){
            dutyBtn.visibility = View.INVISIBLE
            dutyImg.visibility = View.INVISIBLE
            dutyText.visibility = View.INVISIBLE
        }else {
            dutyBtn.visibility = View.VISIBLE
            dutyImg.visibility = View.VISIBLE
            dutyText.visibility = View.VISIBLE
        }

        if(!showNeFlag){
            neBtn.visibility = View.INVISIBLE
            neImg.visibility = View.INVISIBLE
            neText.visibility = View.INVISIBLE
        }else {
            neBtn.visibility = View.VISIBLE
            neImg.visibility = View.VISIBLE
            neText.visibility = View.VISIBLE
        }
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