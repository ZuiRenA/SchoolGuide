package com.example.schoolguide.util

import android.content.Context
import java.io.File
import java.lang.Exception
import java.math.BigDecimal

object CacheUtil {
    /**
     * 获取当前缓存
     */
    fun getTotalCacheSize(context: Context): String{
        val imageCacheDie = context.cacheDir.path
        val file: File = File(imageCacheDie)
        val cacheSize: Long = getFolderSize(file)

        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 删除缓存
     */
    fun clearAllCache(context: Context): Boolean{
        val imageCacheDie = context.cacheDir.path
        val file = File(imageCacheDie)
        return deleteDir(file)
    }

    private fun deleteDir(dir: File?): Boolean{
        if (dir != null && dir.isDirectory){
            val children = dir.list()
            var size = 0

            if (children != null){
                size = children.size

                children.forEach {
                    val success = deleteDir(File(dir, it))
                    if (!success){
                        return false
                    }
                }
            }
        }

        return dir?.delete() ?: true
    }

    /**
     * 格式化单位
     * 计算缓存大小
     */
    private fun getFormatSize(cacheSize: Double): String{
        val kiloByte = cacheSize / 1024
        if (kiloByte < 1){
            return "$cacheSize KB"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1){
            val resultKB: BigDecimal = BigDecimal(kiloByte.toString())
            return resultKB.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1){
            val resultMB = BigDecimal(gigaByte.toString())
            return resultMB.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }

        val teraByte = gigaByte / 1024
        if (teraByte < 1){
            val resultGB = BigDecimal(teraByte.toString())
            return resultGB.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }

        val resultTb = BigDecimal(teraByte)
        return resultTb.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "TB"
    }


    /**
     * 获取文件大小
     */
    private fun getFolderSize(cacheDir: File?): Long {
        var size: Long = 0
        try {
            val fileList = cacheDir?.listFiles()

            fileList?.forEach {
                if (it.isDirectory){
                    size += getFolderSize(it)
                }else{
                    size += fileList.size
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return size
    }
}