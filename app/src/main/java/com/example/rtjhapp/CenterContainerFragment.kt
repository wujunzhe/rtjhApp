package com.example.rtjhapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.airContidionModule.AirConditionFragment
import com.example.rtjhapp.callModule.CallFragment
import com.example.rtjhapp.databinding.FragmentCenterBinding
import com.example.rtjhapp.gasModule.GasFragment
import com.example.rtjhapp.musicModule.MusicFragment

class CenterContainerFragment : Fragment() {
    private lateinit var binding : FragmentCenterBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = FragmentCenterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 添加空调模块
        childFragmentManager.beginTransaction()
            .replace(binding.containerAirCondition.id, AirConditionFragment())
            .commit()

        // 添加电话模块
        childFragmentManager.beginTransaction()
            .replace(binding.containerCall.id, CallFragment())
            .commit()

        // 添加音乐模块
        childFragmentManager.beginTransaction()
            .replace(binding.containerMusic.id, MusicFragment())
            .commit()

        //添加气体模块
        childFragmentManager.beginTransaction()
            .replace(binding.containerGas.id, GasFragment())
            .commit()
    }


}