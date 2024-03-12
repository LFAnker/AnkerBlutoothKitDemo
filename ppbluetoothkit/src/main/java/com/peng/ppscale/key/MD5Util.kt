package com.peng.ppscale.key

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object MD5Util {
    /**
     * Md5加密
     *
     * @param message
     * @return
     */
    fun md5Encoder(message: String): String {
        return try {
            val digest = MessageDigest.getInstance("md5")
            val encryption = digest.digest(message.toByteArray())
            val hexValue = StringBuffer()
            for (i in encryption.indices) {
                val `val` = encryption[i].toInt() and 0xff
                if (`val` < 16) {
                    hexValue.append("0")
                }
                hexValue.append(Integer.toHexString(`val`))
            }
            hexValue.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }
}