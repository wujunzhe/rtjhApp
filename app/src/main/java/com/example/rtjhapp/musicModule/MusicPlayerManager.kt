package com.example.rtjhapp.musicModule

import android.media.AudioManager
import android.media.MediaPlayer
import com.example.rtjhapp.R
import com.example.rtjhapp.databinding.MusicBinding
import com.example.rtjhapp.utils.MyToast

class MusicPlayerManager {
    private var mediaPlayer : MediaPlayer? = null
    private var currentSongPath : String? = null
    private var paused = false
    private var songList : List<String>? = null
    private var currentSongIndex : Int = - 1

    /**
     * 设置要播放的歌曲列表。
     */
    fun setSongList(songs : List<String>) {
        songList = songs
        currentSongIndex = - 1
    }

    /**
     * 播放歌曲
     */
    fun playSong(binding : MusicBinding, songPath : String) {
        if (songList != null && songList !!.isNotEmpty()) {
            // 停止当前正在播放的歌曲
            stopCurrentSong()

            // 初始化新的 MediaPlayer
            mediaPlayer = MediaPlayer().apply {
                setDataSource(songPath)
                prepare()
                start()
                setOnCompletionListener {
                    // 播放结束时的回调，您可以在这里实现循环播放或其他逻辑
                    playNextSong(binding)
                }
                paused = false
            }

            // 更新当前播放的歌曲路径
            currentSongPath = songPath

            binding.playBtn.setImageResource(R.drawable.pause)
        } else {
            MyToast().info(binding.root.context, "暂无音乐可播放")
        }
    }

    /**
     * 播放下一曲
     */
    fun playNextSong(binding : MusicBinding) {
        if (songList != null && songList !!.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songList !!.size
            val nextSongPath = songList !![currentSongIndex]
            playSong(binding, nextSongPath)
        } else {
            MyToast().info(binding.root.context, "暂无音乐可播放")
        }
    }

    /**
     * 播放上一曲
     */
    fun playPreviousSong(binding : MusicBinding) {
        if (songList != null && songList !!.isNotEmpty()) {
            currentSongIndex = (currentSongIndex - 1 + songList !!.size) % songList !!.size
            val previousSongPath = songList !![currentSongIndex]
            playSong(binding, previousSongPath)
        } else {
            MyToast().info(binding.root.context, "暂无音乐可播放")
        }
    }


    fun pauseOrResumeSong(binding : MusicBinding) {
        mediaPlayer?.apply {
            if (isPlaying) {
                pause()
                paused = true
                binding.playBtn.setImageResource(R.drawable.play)

            } else {
                start()
                paused = false
                binding.playBtn.setImageResource(R.drawable.pause)
            }
        }
    }

    /**
     * 停止播放
     */
    fun stopCurrentSong() {
        mediaPlayer?.apply {
            if (isPlaying || paused) {
                stop()
            }
            release()
        }
        mediaPlayer = null
        paused = false
    }

    /**
     * 下一曲
     */

    fun setVolume(audioManager : AudioManager, volume : Int) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)
    }

    fun isPaused() : Boolean {
        return paused
    }

    fun isPlaying() : Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun getCurrentSongPath() : String? {
        return currentSongPath
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentSongPath = null
    }

}