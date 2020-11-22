package com.anguomob.total.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.anguomob.ads.utils.AnGuoParams;
import com.anguomob.ads.utils.AnguoAds;
import com.anguomob.total.Anguo;
import com.anguomob.total.R;
import com.anguomob.total.activity.ShowTextActivity;
import com.anguomob.total.bean.AnguoAdParams;
import com.anguomob.total.bean.DataForYzdzy;
import com.anguomob.total.init.AnguoUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTBannerAd;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


/**
 * Create by: liuan
 * Create date: 2020-05-19 0019
 * Describe:
 */
public class DialogUtils {
    public interface DialogClickBack {
        void onClickDialog(boolean isAgree);

    }


    //    上下文。标题，正文，toobar 内容
    public static void openShowTextPage(Context context, String sub_text, String text_content, int toobar_bg_color_id) {
        Intent intent2 = new Intent(context, ShowTextActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("text", text_content);
        intent2.putExtra("sub_text", sub_text);
        intent2.putExtra("toobar_bg_id", toobar_bg_color_id);
        context.startActivity(intent2);
    }


    public static void exitDialog(final Activity activity, final Class otherAppActivity, View.OnClickListener onShareClick, boolean isShowApplist, boolean isShowShareApp, String chuanshanJiaId) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View dialogView = View.inflate(activity, R.layout.anguo_dialog_action, null);
        RelativeLayout rl_da_share = dialogView.findViewById(R.id.rl_da_share);
        RelativeLayout rl_da_dev_applist = dialogView.findViewById(R.id.rl_da_dev_applist);
        rl_da_dev_applist.setVisibility(isShowApplist ? View.VISIBLE : View.GONE);
        rl_da_share.setVisibility(isShowShareApp ? View.VISIBLE : View.GONE);


        Button sharetextView = dialogView.findViewById(R.id.action_share);
        Button action_other_app = dialogView.findViewById(R.id.action_other_app);
        Button action_ok = dialogView.findViewById(R.id.action_ok);
        final FrameLayout flad = dialogView.findViewById(R.id.fl_adc_ad);

        //加载banner广告并显示
        try {
            DataForYzdzy netWorkParams = AnGuoParams.Companion.getNetWorkParams();
            ArrayList<AnguoAdParams> data = netWorkParams.getData();
            AnguoAdParams anguoAdParams = data.get(0);
            if (AnguoAds.Companion.getShowAd() && AnGuoParams.Companion.getNetParamsByChannel()) {

                if (AnguoAds.Companion.getMAdType().equals(AnguoAds.ENUM_AD_TYPE.ChuanShanJia)) {
                    TTAdSdk.getAdManager().requestPermissionIfNecessary(activity);
                    AdSlot adSlot = new AdSlot.Builder()
                            .setCodeId(chuanshanJiaId) //广告位id
                            .setSupportDeepLink(true)
                            .setAdCount(1) //请求广告数量为1到3条
                            .setExpressViewAcceptedSize(600, 500) //期望个性化模板广告view的size,单位dp
                            .setImageAcceptedSize(640, 500)//这个参数设置即可，不影响个性化模板广告的size
                            .build();
                    TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);
                    mTTAdNative.loadBannerAd(adSlot, new TTAdNative.BannerAdListener() {
                        @Override
                        public void onError(int i, String s) {
                            flad.removeAllViews();
                        }

                        @Override
                        public void onBannerAdLoad(TTBannerAd ttBannerAd) {
                            flad.removeAllViews();
                            flad.addView(ttBannerAd.getBannerView());

                        }
                    });

                } else if (AnguoAds.Companion.getMAdType().equals(AnguoAds.ENUM_AD_TYPE.QQ)) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        action_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                bottomSheetDialog.dismiss();
            }
        });


        action_other_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, otherAppActivity));
            }
        });

        sharetextView.setOnClickListener(onShareClick);
        bottomSheetDialog.setContentView(dialogView);
        bottomSheetDialog.show();
        setBottomSheetBehavior(bottomSheetDialog, dialogView, BottomSheetBehavior.STATE_EXPANDED);
    }

    public static void setBottomSheetBehavior(final BottomSheetDialog dialog, final View view, int beh) {
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setState(beh);
        mBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dialog.cancel();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }
}

