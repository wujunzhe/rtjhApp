package com.example.rtjhapp.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.AddSpecialCallDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddSpecialCallDialog(context : Context) {
    private val binding = AddSpecialCallDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .create()

    init {
        dialog.setOnShowListener {
            val width = context.resources.getDimensionPixelSize(R.dimen.small_dialog_width)
            val height = context.resources.getDimensionPixelSize(R.dimen.small_dialog_height)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = width
            layoutParams.height = height
            dialog.window?.attributes = layoutParams
        }
        dialog.setView(binding.root)
    }

    fun show(){
        dialog.show()
    }
}