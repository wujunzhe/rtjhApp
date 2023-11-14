package com.example.rtjhapp.callModule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rtjhapp.databinding.CallBinding

class CallFragment: Fragment() {
    private lateinit var binding: CallBinding

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = CallBinding.inflate(inflater,container,false)
        return binding.root
    }
}