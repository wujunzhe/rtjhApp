package com.example.rtjhapp.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.rtjhapp.R
import com.example.rtjhapp.adapter.DisplaySettingsAdapter
import com.example.rtjhapp.databinding.DisplaySettingsDialogBinding
import com.example.rtjhapp.databinding.TimeSettingDialogBinding
import com.example.rtjhapp.event.UpdateAirConditionFragmentUIEvent
import com.example.rtjhapp.event.UpdateBottomFragmentUIEvent
import com.example.rtjhapp.utils.GlobalData
import com.example.rtjhapp.utils.MyToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.greenrobot.eventbus.EventBus

class TimeDialog(context : Context) {
    private val binding = TimeSettingDialogBinding.inflate(LayoutInflater.from(context))
    private val dialog = MaterialAlertDialogBuilder(context)
        .setBackground(ColorDrawable(ContextCompat.getColor(context, R.color.white)))
        .create()

    val min = binding.timeEditText
    val dds = binding.timeSettingSaveBtn
    init {

        dialog.setOnShowListener {
            val width = context.resources.getDimensionPixelSize(R.dimen.dialog_width)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = width
            dialog.window?.attributes = layoutParams
        }

        dds.setOnClickListener{
            saveSettings()
        }
        dialog.setView(binding.root)
    }

    fun show() {
        dialog.show()
    }

    private fun saveSettings() {
        val mzSetting = min.text//设置麻醉时间
        mzSetting?.let{
            val mzSettings = min.text.toString().toInt()//设置麻醉时间

            if(mzSettings >720){
                MyToast().error(binding.root.context, "最大值为12小时(720分钟)")
            }else{
                GlobalData.mzStartTime = mzSettings * 60
                GlobalData.mzTimeBuff = mzSettings * 60
                dialog.dismiss()
            }
        }?:run{
            MyToast().error(binding.root.context, "尚为设置时间")
        }
        GlobalData.mzPd = true
    }
}