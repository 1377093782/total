package com.anguomob.total

import android.app.Activity
import android.app.Application
import android.os.Build
import android.text.TextUtils
import android.view.View
import com.anguomob.total.activity.OtherAppActivity
import com.anguomob.ads.utils.AnGuoParams
import com.anguomob.ads.utils.AnguoAds.Companion.initChuanShanJiaId
import com.anguomob.ads.utils.AnguoAds.Companion.initChuanShanJiaPermission
import com.anguomob.total.bean.DataForYzdzy
import com.anguomob.total.dialog.DialogUtils
import com.anguomob.total.init.AnguoUtils
import com.anguomob.total.utils.PrivacyUserAgreementUtils
import com.anguomob.total.utils.SpUtils
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.umeng.commonsdk.UMConfigure

/**
 * Create by: liuan
 * Create date: 2020-11-17 0017
 * Describe:
 */
class Anguo() {
    companion object {
        fun init(context: Application) {
            //初始化工具类
            AnguoUtils.init(context)
            //设置webview缓存路径
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                AnguoUtils.webviewSetPath(context)
            }
            //初始化腾讯mmkv
            MMKV.initialize(context)
            //初始化友盟。key配置在清单文件
            UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, "")
            //初始化字记得网络参数
            AnGuoParams.initNetWorkParams()
            val netWorkParams: DataForYzdzy? = AnGuoParams.getNetWorkParams();
            //初始化穿山甲广告  第一版无法加载的。
            var appId = netWorkParams?.data?.get(0)?.pangolin_app_id
            var appName = netWorkParams?.data?.get(0)?.name
            if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(appName)) {
                initChuanShanJiaId(appId!!, appName!!)
            }
            //bugly
            CrashReport.initCrashReport(context)
        }

        //检查百度云更新,第一次隐私政策弹框
        fun initMainActivityCreate(context: Activity, url: String) {
            AnguoUtils.checkUpDate(context)
            PrivacyUserAgreementUtils.initFirst(context, url, DialogUtils.DialogClickBack { isAgree ->
                if (isAgree) {
                    initChuanShanJiaPermission()
                }
            })
        }

        //安果移动退出
        fun initMainActivityDestory(activity: Activity, onShareClick: View.OnClickListener, isShowShareApp: Boolean,  chuanshanJiaId:String) {
            DialogUtils.exitDialog(activity, OtherAppActivity::class.java, onShareClick, AnGuoParams.getNetParamsByChannel(), isShowShareApp,chuanshanJiaId)
        }
    }
}