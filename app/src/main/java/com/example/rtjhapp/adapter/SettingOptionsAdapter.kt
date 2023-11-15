package com.example.rtjhapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.CustomListItemBinding
import com.example.rtjhapp.dialog.AirConditionSettingsDialog
import com.example.rtjhapp.dialog.SerialPortSettingsDialog

class SettingOptionsAdapter(
    private val objects: Array<out String>,
    private val iconFontCodes: Array<out String>
) : RecyclerView.Adapter<SettingOptionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = objects[position]
        val iconFontCode = iconFontCodes[position]
        holder.binding.iconTextView.text = iconFontCode
        holder.binding.customTextView.text = item
        holder.binding.root.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.halfTransparent))

        holder.binding.root.setOnClickListener {
            when(item){
                "串口设置" -> {
                    val serialPortSettingsDialog = SerialPortSettingsDialog(holder.itemView.context)
                    serialPortSettingsDialog.show()
                }

                "空调设置" -> {
                    val airConditionSettingsDialog = AirConditionSettingsDialog(holder.itemView.context)
                    airConditionSettingsDialog.show()
                }
            }
        }
    }

    override fun getItemCount(): Int = objects.size

    inner class ViewHolder(val binding: CustomListItemBinding) : RecyclerView.ViewHolder(binding.root)
}
