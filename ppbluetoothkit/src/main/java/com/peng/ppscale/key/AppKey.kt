package com.peng.ppscale.key

import android.content.Context
import android.util.Base64
import com.peng.ppscale.util.ReadJsonUtils
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object AppKey {
    private const val ALGORITHM = "AES/ECB/PKCS5Padding"

    const val NORMAL_APPKEY = "lefub60060202a15ac8a"
    private const val NORMAL_APPSECRET = "UCzWzna/eazehXaz8kKAC6WVfcL25nIPYlV9fXYzqDM="

    /**
     * @param appKey
     * @param appSecret
     * @return
     */
    fun getKey(appKey: String, appSecret: String): String {
        val a = appKey + appSecret
        println(a)
        var hash = MD5Util.md5Encoder(a)
        println(hash)
        hash = hash.substring(10, 26)
        return hash
    }

    /**
     * 解密
     *
     * @param secretKey
     * @param cipherText base64
     * @return
     */
    fun decrypt(secretKey: String, cipherText: String?): String {
        // 将Base64编码的密文解码
        val encrypted = Base64.decode(cipherText, Base64.DEFAULT)
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val key = SecretKeySpec(secretKey.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, key)
            String(cipher.doFinal(encrypted))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun decodeConfig(appKey: String, appSecret: String, encryptStr: String?): String {
        // 获取 key
        val key = getKey(appKey, appSecret)

        // base64解密
        val decode = Base64.decode(encryptStr, Base64.DEFAULT)
        val str = String(decode)
//        println("base64解码后的结果：$str")
        val decrypt = decrypt(key, str)
//        println("解密结果$decrypt")
        return decrypt
    }

    fun decodeNormalConfig(context: Context, appKey: String, appSecret: String, encryptStr: String?): String {
        // 获取 key
        val key = getKey(NORMAL_APPKEY, NORMAL_APPSECRET)
        val encryptStr = ReadJsonUtils.readLanguageJsonFromAssets(context, "lefudemo.config")

        // base64解密
        val decode = Base64.decode(encryptStr, Base64.DEFAULT)
        val str = String(decode)
//        println("base64解码后的结果：$str")
        val decrypt = decrypt(key, str)
//        println("解密结果$decrypt")
        return decrypt
    }
}