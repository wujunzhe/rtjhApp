package com.example.rtjhapp.utils

import com.example.rtjhapp.event.UpdateDebugMessageEvent
import org.greenrobot.eventbus.EventBus

object AddMsgToDebugList {
    fun addMsg(title:String,msg:String){
        EventBus.getDefault().post(UpdateDebugMessageEvent("$title:$msg"))
    }
}