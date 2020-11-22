package com.anguomob.ads.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.widget.Toast

import com.anguomob.total.activity.ChuanShanJiaSplashActivity
import com.anguomob.total.activity.QQADSplashActivity
import com.anguomob.total.init.AnguoUtils
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.qq.e.comm.managers.GDTADManager


class AnguoAds private constructor() {
    //加载激励视频
    enum class ENUM_AD_TYPE {
        ChuanShanJia,
        QQ
    }


    companion object {
        //是否显示广告
        var showAd: Boolean = true;

        //当前ad 类型
        var mAdType: String = ENUM_AD_TYPE.ChuanShanJia.name

        //当前应用包名
        private var mPackageName: String = "";

        //当前应用状态
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            return@lazy AnguoAds()
        }


        ///强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        fun initAll(
                context: Context,
                appIdQQ: String,
                appIdChuanShanJia: String,
                appNameChuanShanJia: String
        ) {

            if (!showAd) return
            if (mAdType.equals(ENUM_AD_TYPE.QQ.name)) {
                GDTADManager.getInstance().initWith(context, appIdQQ)
            } else if (mAdType.equals(ENUM_AD_TYPE.ChuanShanJia.name)) {
                TTAdSdk.init(
                        context,
                        TTAdConfig.Builder()
                                .appId(appIdChuanShanJia)
                                .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                                .appName(appNameChuanShanJia)
                                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                                .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                                .supportMultiProcess(true) //是否支持多进程，true支持
                                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                                .build()
                )

            }


        }

        fun initQQ(appIdQQ: String) {
            if (!showAd) return
            GDTADManager.getInstance().initWith(AnguoUtils.getContext(), appIdQQ)
        }


        fun initChuanShanJiaId(appId: String, appName: String) {
            if (!showAd) return

            if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appName)) {
                return
            }


            TTAdSdk.init(
                    AnguoUtils.getContext(),
                    TTAdConfig.Builder()
                            .appId(appId)
                            .useTextureView(true) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                            .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                            .allowShowNotify(true) //是否允许sdk展示通知栏提示
                            .appName(appName)
                            .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                            .debug(true) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                            .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                            .supportMultiProcess(true) //是否支持多进程，true支持
                            //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                            .build()
            )
        }


        //为获取更好的广告推荐效果，以及提高激励视频广告、下载类广告等填充率，建议在广告请求前，合适的时机调用 SDK 提供的方法，如在用户第一次启动您的 app 后的主界面时调用如下方法：
        fun initChuanShanJiaPermission() {
            TTAdSdk.getAdManager().requestPermissionIfNecessary(AnguoUtils.getContext())
        }


        // 加载激励广告
        fun loadExcitationFullAD(activity: Activity, postIdQQ: String, postIdChuanShanJia: String) {
            if (!showAd) return
            if (mAdType.equals(ENUM_AD_TYPE.QQ.name)) {
                if (TextUtils.isEmpty(postIdQQ)) {
                    return
                }
                QqAds.loadQQRewardVideoAD(activity, postIdQQ)
            } else if (mAdType.equals(ENUM_AD_TYPE.ChuanShanJia.name)) {
                if (TextUtils.isEmpty(postIdChuanShanJia)) {
                    return
                }
                ChuanShanJiaAds.loadRewardVideoAD(activity, postIdChuanShanJia)
            }

        }

        // 加载插屏广告
        fun loadtableplaqueAD(activity: Activity, postIdByQQ: String, postIdByChuanShanJia: String, width: Int, height: Int) {
            if (!showAd) return
            if (mAdType.equals(ENUM_AD_TYPE.QQ.name)) {
                if (TextUtils.isEmpty(postIdByQQ)) {
                    return
                }
                QqAds.loadQQtableplaqueAD(activity, postIdByQQ)
            } else if (mAdType.equals(ENUM_AD_TYPE.ChuanShanJia.name)) {
                if (TextUtils.isEmpty(postIdByChuanShanJia)) {
                    return
                }
                ChuanShanJiaAds.loadtableplaqueAd(activity, postIdByChuanShanJia, width, height)
            }


        }

        fun <T> gotoMainPage(
                context: Activity,
                postIdQQ: String,
                postIdChuanshanjia: String,
                mainActivity: Class<T>
        ) {
            if (!showAd || !AnGuoParams.getNetParamsByChannel()) {
                val intent = Intent(context, mainActivity)
                context.startActivity(intent)
                // 修复 Android 9.0 下 Activity 跳转动画导致的启动页闪屏的问题
                context.overridePendingTransition(0, 0)
                context.finish()
                return
            }

            if (mAdType.equals(ENUM_AD_TYPE.QQ.name)) {
                val intent = Intent(context, QQADSplashActivity::class.java)
                intent.putExtra("postId", postIdQQ)
                intent.putExtra("mainActivity", mainActivity)
                context.startActivity(intent)
                // 修复 Android 9.0 下 Activity 跳转动画导致的启动页闪屏的问题
                context.overridePendingTransition(0, 0)
                context.finish()
            } else if (mAdType.equals(ENUM_AD_TYPE.ChuanShanJia.name)) {
                showChuanShanSplashAd(context, postIdChuanshanjia, mainActivity)
            }
        }

        fun <T> showChuanShanSplashAd(context: Activity, postIdChuanshanjia: String, mainActivity: Class<T>) {
            //不显示广告或者 不让在渠道显示 或者  穿山甲id是空的
            if (!showAd || !AnGuoParams.getNetParamsByChannel() || TextUtils.isEmpty(postIdChuanshanjia)) {
                val intent = Intent(context, mainActivity)
                context.startActivity(intent)
                // 修复 Android 9.0 下 Activity 跳转动画导致的启动页闪屏的问题
                context.overridePendingTransition(0, 0)
                context.finish()
                return
            }

            val intent = Intent(context, ChuanShanJiaSplashActivity::class.java)
            intent.putExtra("postId", postIdChuanshanjia)
            intent.putExtra("mainActivity", mainActivity)
            context.startActivity(intent)
            // 修复 Android 9.0 下 Activity 跳转动画导致的启动页闪屏的问题
            context.overridePendingTransition(0, 0)
            context.finish()
        }


    }


}