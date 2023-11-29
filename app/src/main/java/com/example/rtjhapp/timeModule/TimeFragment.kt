package com.example.rtjhapp.timeModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.TopTimeBinding

class TimeFragment : Fragment() {
    private lateinit var binding : TopTimeBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = TopTimeBinding.inflate(inflater, container, false)
        return binding.root
    }
}