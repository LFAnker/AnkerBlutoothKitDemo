package com.peng.ppscale.util

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 *    @author : whs
 *    e-mail : haisilen@163.com
 *    date   : 2023/6/19 11:37
 *    desc   :读取Json的工具类
 */
object ReadJsonUtils {

    /**
     * 从 assets 目录中读取指定文件的内容，返回字符串形式的内容。
     *
     * @param context 上下文对象
     * @param filePath 文件路径，相对于 assets 目录，例如 "example.json"
     * @return 文件内容的字符串形式
     * @throws IOException 如果读取文件失败
     */
    fun readLanguageJsonFromAssets(context: Context, filePath: String): String {
        val sb = StringBuilder()
        try {
            context.assets.open(filePath).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                }
            }
        } catch (e: IOException) {
            Log.d("AnkerBlutoothKitDemo","json失败")
            e.printStackTrace()
        }
        return sb.toString()
    }

}