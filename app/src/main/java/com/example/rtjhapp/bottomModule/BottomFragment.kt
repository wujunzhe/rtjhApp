package com.example.rtjhapp.bottomModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.BottomBinding

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
}