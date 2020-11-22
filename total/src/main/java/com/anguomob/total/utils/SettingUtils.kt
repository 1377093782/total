package com.anguomob.total.utils

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import com.anguomob.total.R
import com.anguomob.total.activity.FeedBackActivity
import java.io.File

/** 设置里面的工具类
 *
 */
object SettingUtils {
    /** 意见反馈
     *
     */
    fun feedBack(context: Context, appName: String?) {
        val data = Intent(context, FeedBackActivity::class.java)
        data.putExtra("app_name", appName)
        startActivity_(context, data)
    }

    /** 版本号
     *
     */
    fun version(context: Context, versionStr: String?) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val version = packageInfo.versionName
            val applicationInfo = packageInfo.applicationInfo
            val label = applicationInfo.loadLabel(packageManager).toString()
            AlertDialog.Builder(context, R.style.MainActivityAlertDialog)
                    .setTitle(versionStr)
                    .setMessage("$label ($version)")
                    .show()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    /** 跪求好评
     *
     */
    fun fiveStar(c: Context, packageName: String?) {
        try {
            var finalPackageName = if (!TextUtils.isEmpty(packageName)) packageName else c.packageName
            var uri = Uri.parse("market://details?id=${finalPackageName}")
            var intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            c.startActivity(intent)
        } catch (e: Exception) {
            ToastUtils.showShort(R.string.no_market)
            e.printStackTrace()
        }
    }

    fun shareMyApp(context: Context) {
        val f = File(context.packageResourcePath)
        LogUtils.e(context.packageResourcePath + "context.getPackageResourcePath()")
        //调用android分享窗口
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun startGoogleMyApp(context: Context) {
        if (isInstalled(context, "com.android.vending")) {
            //Analytics.getInstance(context)._sendEvent(RecommendManager.CATEGORY, RecommendManager.ACTION_AD, "有Google Play");
            val market = "market://details?id=" + context.packageName + "&referrer=utm_source%3D" + context.getString(R.string.app_name)
            val intent = Intent()
            intent.setPackage("com.android.vending")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse(market)
            intent.action = Intent.ACTION_VIEW
            context.startActivity(intent)
        } else {
            //Analytics.getInstance(context)._sendEvent(RecommendManager.CATEGORY, RecommendManager.ACTION_AD, "没有Google Play");
            startGooglePlayByHttpUrl(context, context.packageName, context.getString(R.string.app_name))
        }
    }

    //google 市场五星好评
    fun startGooglePlayByMarketUrl(context: Context, packageName: String) {
        //如果安装过google市场
        if (isInstalled(context, packageName)) {
            val pm = context.packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            context.startActivity(intent)
        } else if (isInstalled(context, "com.android.vending")) {
            //Analytics.getInstance(context)._sendEvent(RecommendManager.CATEGORY, RecommendManager.ACTION_AD, "有Google Play");
            val market = "market://details?id=" + packageName + "&referrer=utm_source%3D" + context.getString(R.string.app_name)
            val intent = Intent()
            intent.setPackage("com.android.vending")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.data = Uri.parse(market)
            intent.action = Intent.ACTION_VIEW
            context.startActivity(intent)
        } else {
            //Analytics.getInstance(context)._sendEvent(RecommendManager.CATEGORY, RecommendManager.ACTION_AD, "没有Google Play");
            startGooglePlayByHttpUrl(context, packageName, context.getString(R.string.app_name))
        }
    }

    fun startActivity_(context: Context?, intent: Intent?): Boolean {
        if (context == null || intent == null) return false
        try {
            context.startActivity(intent)
        } catch (activityNotFoundException: ActivityNotFoundException) {
            activityNotFoundException.printStackTrace()
            return false
        } catch (illegalArgumentException: IllegalArgumentException) {
            illegalArgumentException.printStackTrace()
            return false
        } catch (exception: Exception) {
            exception.printStackTrace()
            return false
        }
        return true
    }

    fun startGooglePlayByHttpUrl(context: Context, packageName: String, zui: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("http://play.google.com/store/apps/details?id=$packageName&referrer=utm_source%3D$zui")
        context.startActivity(intent)
    }

    fun isInstalled(context: Context, packageName: String?): Boolean {
        var bInstalled = false
        if (packageName == null) return false
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = context.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            packageInfo = null
            e.printStackTrace()
        }
        bInstalled = if (packageInfo == null) {
            false
        } else {
            true
        }
        return bInstalled
    }
}