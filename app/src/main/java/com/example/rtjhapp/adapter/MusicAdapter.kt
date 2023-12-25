package com.example.rtjhapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rtjhapp.databinding.MusicBinding
import com.example.rtjhapp.databinding.MusicListItemBinding
import com.example.rtjhapp.model.MusicListItemViewModel
import com.example.rtjhapp.module.musicModule.MusicPlayerManager

class MusicAdapter(
    private val binding : MusicBinding,
    private val musicList : List<MusicListItemViewModel>,
    private val musicPlayerManager : MusicPlayerManager,
    private val onSongClickListener : (String) -> Unit,
    private val onVolumeChangedListener : (Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.ViewHolder>() {

    interface OnVolumeChangedListener {
        fun onVolumeChanged(volume : Int)
    }

    private val songNameText : TextView = binding.songName
    private var selectedItemPosition : Int = RecyclerView.NO_POSITION

    inner class ViewHolder(private val itemBinding : MusicListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        val volumeSeekBar : SeekBar = binding.musicSeekbar
        fun bind(item : MusicListItemViewModel) {
            itemBinding.musicListItemView.text = item.getSongName()
            // 根据选中状态更新UI
            binding.root.isSelected = position == selectedItemPosition

            itemBinding.root.setOnClickListener {
                // 更新选中项的位置
                val previousSelectedItemPosition = selectedItemPosition
                selectedItemPosition = adapterPosition

                // 通知适配器更新UI
                notifyItemChanged(previousSelectedItemPosition)
                notifyItemChanged(selectedItemPosition)

                onSongClickListener(item.getSongPath())
            }

            if (musicPlayerManager.isPlaying() && musicPlayerManager.getCurrentSongPath() == item.getSongPath()) {
                // 当前项正在播放
                songNameText.text = item.getSongName()
            }
        }
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ViewHolder {
        val itemBinding =
            MusicListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        val viewModel = musicList[position]
        holder.bind(viewModel)

        // 设置 SeekBar 的监听器
        holder.volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar : SeekBar?, progress : Int, fromUser : Boolean) {
                if (fromUser) {
                    // 当用户拖动 SeekBar 时，回调音量变化事件
                    onVolumeChangedListener.invoke(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar : SeekBar?) {
                // 用户开始拖动 SeekBar
            }

            override fun onStopTrackingTouch(seekBar : SeekBar?) {
                // 用户停止拖动 SeekBar
            }
        })
    }

    override fun getItemCount() : Int {
        return musicList.size
    }

    fun getSelectedItem() : MusicListItemViewModel? {
        return if (selectedItemPosition != RecyclerView.NO_POSITION) {
            musicList[selectedItemPosition]
        } else {
            null
        }
    }
}