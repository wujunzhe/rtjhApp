package com.example.rtjhapp.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.SpecialCallAdapter
import com.example.rtjhapp.databinding.SpecialCallDialogBinding
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SpecialCallDialog(context : Context) {
    private val binding = SpecialCallDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .create()

    private val sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
    private val specialCallList = sharedPreferencesManager.readSpecialCallList()
    private val adapter = SpecialCallAdapter(binding,specialCallList,binding.specialCallListView)
    init {
        dialog.setOnShowListener {
            val width = context.resources.getDimensionPixelSize(R.dimen.special_call_dialog_width)
            val height = context.resources.getDimensionPixelSize(R.dimen.special_call_dialog_height)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = width
            layoutParams.height = height
            dialog.window?.attributes = layoutParams
        }
        dialog.setView(binding.root)
    }

    private fun setupListView() {
        val listView = binding.specialCallListView
    }
    fun show(){
        dialog.show()
    }

}