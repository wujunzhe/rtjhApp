package com.example.rtjhapp.musicModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.MusicBinding

class MusicFragment: Fragment() {
    private lateinit var binding: MusicBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = MusicBinding.inflate(inflater,container,false)
        return binding.root
    }
}