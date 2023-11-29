package com.example.rtjhapp.model

import androidx.databinding.BaseObservable

class MusicListItemViewModel(private val songName: String, private val songPath: String) : BaseObservable() {
    fun getSongName() : String{
        return songName
    }

    fun getSongPath(): String {
        return songPath
    }
}