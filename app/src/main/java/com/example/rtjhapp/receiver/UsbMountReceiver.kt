package com.example.rtjhapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.rtjhapp.utils.AddMsgToDebugList
import com.example.rtjhapp.utils.MyToast
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class UsbMountReceiver : BroadcastReceiver() {

    private val handler = Handler(Looper.getMainLooper())
    private var isImporting = false
    override fun onReceive(context : Context?, intent : Intent?) {
        if (intent != null && intent.action == Intent.ACTION_MEDIA_MOUNTED) {
            val usbPath = intent.data?.path
            usbPath?.let {
                if (hasMp3Files(it)) {
                    isImporting = true
                        context?.let { it1 ->
                            handler.post {
                                MyToast().info(it1, "正在导入mp3文件")
                            }
                        }
                        try {
                            importMp3FilesFromUsb(
                                it,
                                context?.filesDir?.absolutePath ?: "",
                                context
                            )
                        } catch (e : Exception) {
                            AddMsgToDebugList.addMsg("导入出错", e.toString())
                        } finally {
                            isImporting = false
                        }
                }
            }
        }
    }

    private fun hasMp3Files(usbPath : String) : Boolean {
        val usbDirectory = File(usbPath)
        if (usbDirectory.isDirectory) {
            val mp3Files = usbDirectory.listFiles { file ->
                file.isFile && file.extension.equals("mp3", ignoreCase = true)
            }
            isImporting = true
            return ! mp3Files.isNullOrEmpty()
        }
        return false
    }

    private fun importMp3FilesFromUsb(
        usbPath : String?,
        targetDirectory : String,
        context : Context?
    ) {
        usbPath?.let {
            val usbDirectory = File(usbPath)
            if (usbDirectory.isDirectory) {
                val mp3Files = usbDirectory.listFiles { file ->
                    file.isFile && file.extension.equals("mp3", ignoreCase = true)
                }

                mp3Files?.forEach { mp3File ->
                    try {
                        // 动态创建目标目录
                        val targetDir = File(targetDirectory)
                        if (! targetDir.exists()) {
                            targetDir.mkdirs()
                        }
                        val targetFile = File(targetDirectory, mp3File.name)
                        if (! targetFile.exists()) {
                            Files.copy(
                                mp3File.toPath(),
                                targetFile.toPath(),
                                StandardCopyOption.REPLACE_EXISTING
                            )
                        } else {
                            // 如果文件已存在，则不导入，可以选择记录日志或其他逻辑
                            context?.let { it1 ->
                                handler.post {
                                    MyToast().info(it1, "文件 ${mp3File.name} 已存在，不导入")
                                }
                            }
                        }
                    } catch (e : IOException) {
                        e.printStackTrace()
                        // 处理复制过程中可能的异常
                    } finally {
                        isImporting = false
                    }
                }

                context?.let { it2 ->
                    handler.post {
                        MyToast().success(it2, "导入完成")
                    }
                }
            }
        }
    }
}

