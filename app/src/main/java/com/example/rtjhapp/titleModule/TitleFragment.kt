package com.example.rtjhapp.titleModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.TitleBinding
import com.example.rtjhapp.databinding.TopTimeBinding

class TitleFragment: Fragment() {
    private lateinit var binding: TitleBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = TitleBinding.inflate(inflater,container,false)
        return binding.root
    }
}