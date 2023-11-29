package com.example.rtjhapp.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.DisplaySettingsAdapter
import com.example.rtjhapp.databinding.DisplaySettingsDialogBinding
import com.example.rtjhapp.event.UpdateAirConditionFragmentUIEvent
import com.example.rtjhapp.event.UpdateBottomFragmentUIEvent
import com.example.rtjhapp.utils.MyToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

class DisplaySettingsDialog(context : Context) {
    private val binding = DisplaySettingsDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .setBackground(ColorDrawable(ContextCompat.getColor(context, R.color.white)))
        .create()

    private val adapter = DisplaySettingsAdapter(binding)

    init {
        dialog.setOnShowListener {
            val width = context.resources.getDimensionPixelSize(R.dimen.dialog_width)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = width
            dialog.window?.attributes = layoutParams
        }
        dialog.setView(binding.root)
        adapter.initSettings()
        adapter.setSaveButtonClickListener { saveSettings() }
    }

    fun show() {
        dialog.show()
    }

    private fun saveSettings() {
        adapter.saveSettings()
        MyToast().success(binding.root.context, "保存成功")
        EventBus.getDefault().post(UpdateAirConditionFragmentUIEvent())
        EventBus.getDefault().post(UpdateBottomFragmentUIEvent())
        dialog.dismiss()
    }
}
