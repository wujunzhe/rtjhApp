package com.example.rtjhapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.PdfPreviewActivity
import com.example.rtjhapp.databinding.CustomListItemBinding
import com.example.rtjhapp.dialog.AirConditionSettingsDialog
import com.example.rtjhapp.dialog.DisplaySettingsDialog
import com.example.rtjhapp.dialog.SerialPortSettingsDialog
import com.example.rtjhapp.dialog.SmartSettingsDialog

class SettingOptionsAdapter(
    private val objects : Array<out String>,
    private val iconFontCodes : Array<out String>,
) : RecyclerView.Adapter<SettingOptionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val binding =
            CustomListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // 获取屏幕宽度
        val displayMetrics = parent.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        // 计算你想要的宽度
        val desiredWidth = (screenWidth * 0.3).toInt()

        // 设置宽度
        val layoutParams = binding.root.layoutParams
        layoutParams.width = desiredWidth
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val item = objects[position]
        val iconFontCode = iconFontCodes[position]
        holder.binding.iconTextView.text = iconFontCode
        holder.binding.customTextView.text = item
        holder.binding.root.setOnClickListener {
            when (item) {
                "串口设置" -> {
                    val serialPortSettingsDialog = SerialPortSettingsDialog(holder.itemView.context)
                    serialPortSettingsDialog.show()
                }

                "空调设置" -> {
                    val airConditionSettingsDialog =
                        AirConditionSettingsDialog(holder.itemView.context)
                    airConditionSettingsDialog.show()
                }

                "显示设置" -> {
                    val displaySettingsDialog = DisplaySettingsDialog(holder.itemView.context)
                    displaySettingsDialog.show()
                }

                "智能模式及消毒" -> {
                    val smartModeSettingsDialog = SmartSettingsDialog(holder.itemView.context)
                    smartModeSettingsDialog.show()
                }

                "使用说明书" -> {
                    val intent = Intent(holder.itemView.context, PdfPreviewActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }

                "退出程序" -> {
                    (holder.itemView.context as? Activity)?.finish()
                }
            }
        }
    }

    override fun getItemCount() : Int = objects.size

    inner class ViewHolder(val binding : CustomListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
