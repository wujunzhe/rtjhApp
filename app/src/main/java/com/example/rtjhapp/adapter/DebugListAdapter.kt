package com.example.rtjhapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.DebugListItemBinding
import com.example.rtjhapp.model.DebugItemViewModel

class DebugListAdapter(private val debugList: MutableList<String>) :
    RecyclerView.Adapter<DebugListAdapter.DebugViewHolder>() {

    class DebugViewHolder(val binding: DebugListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: DebugListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.debug_list_item, parent, false)
        return DebugViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebugViewHolder, position: Int) {
        val debugInfo = debugList[position]

        // 设置数据绑定的 ViewModel
        holder.binding.viewModel = DebugItemViewModel(debugInfo)

        // 立即更新 UI
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return debugList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDebugMessage(debugInfo: String) {
        // 添加提供的调试信息
        debugList.add(debugInfo)
        notifyDataSetChanged()
        notifyItemInserted(debugList.size - 1)
    }
}
