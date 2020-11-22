package com.anguomob.total.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * Create by: liuan
 * Create date: 2020-10-29 0029
 * Describe:
 */
object ManifestUtils {
    fun getMetadata(context: Context, key: String?): String {
        try {
            val metaData = context.packageManager
                    .getApplicationInfo(context.packageName,
                            PackageManager.GET_META_DATA).metaData
            return metaData[key].toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getPackageName(context: Context): String {
        try {
            return context.packageManager.getPackageInfo(
                    context.packageName, 0).packageName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }
}


