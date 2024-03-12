package com.peng.ppscale.util

import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

object FileUtilsKotlin {

    var job: Job? = null

    /**
     * 创建日志目录
     *             val fileFath = context.filesDir.absolutePath + "/Log/DeviceLog"
     *
     * @return
     * @throws IOException
     */
    fun createFileAndWrite(
        filePath: String, deviceName: String, address: String, content: String,
        callback: FileUtilCallBack?
    ) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
            val dateTime = simpleDateFormat.format(Date())
            val fileName = deviceName + "_" + address + "_" + dateTime + ".txt"
            val dirFile = File(filePath)
            if (!dirFile.exists()) {
                dirFile.mkdirs()
            } else {
                dirFile.delete()
                dirFile.mkdirs()
            }
            val file = File(filePath + File.separator + fileName)
            if (file.exists()) {
                file.delete()
            }
            try {
                file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val data = content.toByteArray(Charset.defaultCharset())
            writeBytes(file.path, data)
            withContext(Dispatchers.Main) {
                callback?.callBack(file.path)
            }
            job?.cancel()
            job = null
        }


    }

    /**
     * 向文件中写入数据
     *
     * @param filePath 目标文件全路径
     * @param data     要写入的数据
     * @return true表示写入成功  false表示写入失败
     */
    fun writeBytes(filePath: String?, data: ByteArray?): Boolean {
        try {
            val fos = FileOutputStream(filePath)
            fos.write(data)
            fos.close()
            return true
        } catch (e: Exception) {
            println(e.toString())
        }
        return false
    }


}

interface FileUtilCallBack {
    fun callBack(filePath: String?)
}


