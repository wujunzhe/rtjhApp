package com.example.rtjhapp.musicModule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rtjhapp.adapter.MusicAdapter
import com.example.rtjhapp.databinding.MusicBinding
import com.example.rtjhapp.databinding.MusicListDialogBinding
import com.example.rtjhapp.model.MusicListItemViewModel
import com.example.rtjhapp.receiver.UsbMountReceiver
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.Constants
import com.example.rtjhapp.utils.MySerialHelper
import com.example.rtjhapp.utils.MyToast
import com.example.rtjhapp.utils.SharedPreferencesManager
import com.example.rtjhapp.utils.modbus.CRC16.getCRC
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.util.Locale

class MusicFragment: Fragment() {
    private lateinit var binding: MusicBinding
    private lateinit var usbMountReceiver: UsbMountReceiver
    private lateinit var musicPlayerManager : MusicPlayerManager
    private lateinit var musicSerialHelper : MySerialHelper
    private lateinit var adapter : MusicAdapter
    private lateinit var audioManager: AudioManager
    private lateinit var mainHandler:Handler
    private lateinit var sharedPreferencesManager:SharedPreferencesManager
    private lateinit var playBtn: ImageButton
    private lateinit var musicListBtn: ImageButton
    private lateinit var nextBtn: ImageButton
    private lateinit var previousBtn: ImageButton
    private lateinit var backgroundMusicBtn: ImageButton
    private lateinit var volumeSeekBar: SeekBar
    private var isBackgroundMusic = false
    var volumeLevel = arrayOf(
        "1档",
        "2档",
        "3档",
        "4档",
        "5档",
        "6档",
        "7档",
        "8档",
        "9档",
        "10档",
        "11档",
        "12档",
        "13档",
        "14档"
    )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        // 注册usb广播
        usbMountReceiver = UsbMountReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED)
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED)
        filter.addDataScheme("file")
        requireContext().registerReceiver(usbMountReceiver, filter)

    }

    override fun onCreateView(
        inflater : LayoutInflater,
        container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        binding = MusicBinding.inflate(inflater,container,false)

        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        sharedPreferencesManager = SharedPreferencesManager(binding.root.context)
        val musicSerialPort = sharedPreferencesManager.readString(Constants.SerialPort.music,Constants.SerialPort.Default.music)
        musicSerialHelper = musicSerialPort?.let { MySerialHelper(it,Constants.SerialPortDefaultConfig.baudRate) } !!

        try {
            musicSerialHelper.open()
        } catch (e: Exception) {
            MyToast().error(binding.root.context,"音乐模块串口没打开")
        }


        musicPlayerManager = MusicPlayerManager()
        val mp3Files = getMp3Files()
        musicPlayerManager.setSongList(mp3Files.map { it.absolutePath })
        mainHandler = Handler(Looper.getMainLooper())

        adapter = MusicAdapter(
            binding,
            createMusicListItems(mp3Files),
            musicPlayerManager,
            onSongClickListener = { songPath ->
                if (musicSerialHelper.isOpen) {
                    musicPlayerManager.playSong(binding, songPath)
                    binding.songName.text = getMetadata(File(songPath))
                } else {
                    MyToast().info(binding.root.context, "音乐模块串口未打开")
                }
            },
            onVolumeChangedListener = { volume ->
                // 设置音乐流的音量
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
            }
        )

        playBtn = binding.playBtn
        previousBtn = binding.previousBtn
        nextBtn = binding.netxBtn
        musicListBtn = binding.musicList
        volumeSeekBar = binding.musicSeekbar
        backgroundMusicBtn = binding.backgroundMusic

        try {
            volumeSeekBar.min = 1
            volumeSeekBar.max = 14
            volumeSeekBar.progress = 1
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            audioManager.getStreamVolume(AudioManager.STREAM_RING)
        } catch (e: Exception) {

        }
        // 本地音乐列表按钮监听
        musicListBtn.setOnClickListener {
            val mp3FileList = getMp3Files()
            if (mp3FileList.isEmpty()) {
                MyToast().info(binding.root.context,"暂无可播放列表")
            } else {
                showMusicListDialog(binding)
            }
        }

        // 播放按钮监听
        playBtn.setOnClickListener {
                if (musicPlayerManager.isPlaying()) {
                    musicPlayerManager.pauseOrResumeSong(binding)
                } else {
                        val selectedItem = adapter.getSelectedItem()
                        selectedItem?.let {
                            musicPlayerManager.playSong(binding,it.getSongPath())
                        }
                }
        }

        // 上一曲按钮监听
        previousBtn.setOnClickListener {
            musicPlayerManager.playPreviousSong(binding)
        }

        nextBtn.setOnClickListener {
            musicPlayerManager.playNextSong(binding)
        }

        // 背景音乐按钮监听
        backgroundMusicBtn.setOnClickListener {
            if (musicSerialHelper.isOpen){
                isBackgroundMusic = true
            } else {
                MyToast().error(binding.root.context,"音乐模块串口未打开")
            }
        }

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar : SeekBar?, progress : Int, fromUser : Boolean) {
               if (isBackgroundMusic){
                   if (musicSerialHelper.isOpen) {
                       volumeSeekBar.progress = progress
                       val hex = "FF05000" + Integer.toHexString(progress)
                           .uppercase(Locale.getDefault()) + "FF00"
                       val hexWithCRC = hex + getCRC(hex)
                       musicSerialHelper.sendHex(hexWithCRC)
                       mainHandler.post {
                           AddMsgToDebugList.addMsg("下发设置音量指令:",hexWithCRC)
                       }
                   } else {
                       MyToast().error(binding.root.context,"音乐模块串口没打开")
                       volumeSeekBar.progress = volumeSeekBar.progress
                   }
               } else {
                   musicPlayerManager.setVolume(audioManager,progress)
               }
            }

            override fun onStartTrackingTouch(seekBar : SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar : SeekBar?) {
                if (seekBar != null) {
                    MyToast().success(binding.root.context,"设置当前音量为${volumeLevel[seekBar.progress - 1]}")
                }
            }
        })

        return binding.root
    }


    @SuppressLint("InflateParams")
    private fun showMusicListDialog(binding : MusicBinding){
        val dialogBinding = MusicListDialogBinding.inflate(layoutInflater)
        val dialogView = dialogBinding.root

        val mp3Files = getMp3Files()

        adapter = MusicAdapter(
            binding,
            createMusicListItems(mp3Files),
            musicPlayerManager,
            onSongClickListener = {
                    songPath ->
                if (musicSerialHelper.isOpen){
                    musicPlayerManager.playSong(binding,songPath)
                    binding.songName.text = getMetadata(File(songPath))
                } else {
                    MyToast().info(binding.root.context,"音乐模块串口为打开")
                }
            },
            onVolumeChangedListener = {volume ->
                // 设置音乐流的音量
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
            })

        val recyclerView = dialogBinding.musicListView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .show()
    }



    private fun getMp3Files(): List<File> {
        val internalStorageDir = requireContext().filesDir
        return internalStorageDir.listFiles { _, name -> name.endsWith(".mp3", true) }?.toList()
            ?: emptyList()
    }

    private fun createMusicListItems(mp3Files: List<File>): List<MusicListItemViewModel>{
        val musicListItems = mutableListOf<MusicListItemViewModel>()

        for (mp3File in mp3Files){
            val songName = getMetadata(mp3File)
            val songPath = mp3File.absolutePath
            musicListItems.add(MusicListItemViewModel(songName,songPath))
        }
        return musicListItems
    }

    private fun getMetadata(mp3File: File): String {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(mp3File.absolutePath)
            return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: mp3File.name
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        return mp3File.name
    }

    override fun onDestroyView() {
        super.onDestroyView()
        musicPlayerManager.stopCurrentSong()
        // 解除 USB 挂载广播接收器的注册
        requireContext().unregisterReceiver(usbMountReceiver)
    }
}