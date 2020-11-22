package com.anguomob.total.init;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;


import com.anguomob.total.activity.UpdateActivity;
import com.anguomob.total.utils.Utils;
import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.tencent.mmkv.MMKV;
import com.umeng.commonsdk.UMConfigure;


public class AnguoUtils {
    /**
     * 初始化插件
     *
     * @param context
     */
    private static Context libContext;


    /**
     * 初始化插件
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (libContext == null) {
            libContext = context;
            //65535方法解决方案
            MultiDex.install(context);
            //为Toast 提供能力
            Utils.init(context);
        }


    }

    public static void checkUpDate(final Context context) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BDAutoUpdateSDK.cpUpdateCheck(context, new CPCheckUpdateCallback() {
                    @Override
                    public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {

                        if (null == appUpdateInfo && null == appUpdateInfoForInstall) {

                            // 最新版本

                        } else {

                            Intent intent = new Intent(context, UpdateActivity.class);
                            intent.putExtra("change", appUpdateInfo.getAppChangeLog());
                            intent.putExtra("versionName", appUpdateInfo.getAppVersionName());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);


                        }

                    }
                });
            }
        }, 5000);


    }

    public static void checkUpDate(final Context context,int deleayMillis) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                BDAutoUpdateSDK.cpUpdateCheck(context, new CPCheckUpdateCallback() {
                    @Override
                    public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {

                        if (null == appUpdateInfo && null == appUpdateInfoForInstall) {

                            // 最新版本

                        } else {

                            Intent intent = new Intent(context, UpdateActivity.class);
                            intent.putExtra("change", appUpdateInfo.getAppChangeLog());
                            intent.putExtra("versionName", appUpdateInfo.getAppVersionName());

                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            context.startActivity(intent);


                        }

                    }
                });
            }
        }, deleayMillis);


    }

    @RequiresApi(api = 28)
    public static void webviewSetPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);

            if (!context.getPackageName().equals(processName)) {//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    public static String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }


    public static Context getContext() {

        if (libContext == null) {
            try {
                throw new Exception("Must be called when the program starts------>LiuAnUtils.init(this);");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return libContext;
    }


}
