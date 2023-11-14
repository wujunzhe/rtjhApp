package com.example.rtjhapp.gasModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.MedicalStressStatusBinding

class GasFragment: Fragment() {
    private lateinit var binding: MedicalStressStatusBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = MedicalStressStatusBinding.inflate(inflater,container,false)
        return binding.root
    }
}