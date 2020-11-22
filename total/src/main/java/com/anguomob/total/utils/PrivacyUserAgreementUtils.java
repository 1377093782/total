package com.anguomob.total.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.anguomob.total.R;
import com.anguomob.total.activity.ShowTextActivity;
import com.anguomob.total.activity.WebViewX5Acitivity;
import com.anguomob.total.dialog.DialogUtils;
import com.anguomob.total.view.round.RoundLinearLayout;

public class PrivacyUserAgreementUtils {
    //    打开用户协议
    public static void openUserAgreement(Context context, String app_name) {
        Intent intent2 = new Intent(context, ShowTextActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("text", context.getResources().getString(R.string.user_agreement_des).replace("[app_name]",  context.getResources().getString(R.string.app_name)));
        intent2.putExtra("sub_text", context.getResources().getString(R.string.user_agreement));
        intent2.putExtra("toobar_bg_id", R.color.color_main);
        context.startActivity(intent2);
    }

    //    打开隐私弹框
    public static void openPrivacyPolicy(Context context, String url) {

        Intent intent = new Intent(context, WebViewX5Acitivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("text", url);
        intent.putExtra("title", context.getResources().getString(R.string.privacy_policy));
        intent.putExtra("toobar_bg_id", R.color.color_main);
    }


    //   打开第一次初始化  打开隐私政策和用户协议对话框
    public static void initFirst(final Activity activity, final String url, final DialogUtils.DialogClickBack dialogClickBack) {
        if (SpUtils.getBoolean("isFirst", false)) {
            dialogClickBack.onClickDialog(true);
            return;
        }
// 自定义 title样式
        TextView title = new TextView(activity);
        title.setText(activity.getResources().getString(R.string.read_private_title));
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 30, 0, 30);
        title.setBackgroundColor(Color.parseColor("#B6DEF0"));
        // 中间的信息以一个view的形式设置进去

        TextView tv1 = new TextView(activity);


        tv1.setTextSize(16);
        tv1.setGravity(Gravity.CENTER);
        tv1.setPadding(20, 20, 20, 80);
        tv1.setText("《" + activity.getResources().getString(R.string.user_agreement) + "》");
        tv1.setTextColor(Color.BLUE);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(activity, ShowTextActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent2.putExtra("text", activity.getResources().getString(R.string.user_agreement_des).replace("[app_name]",  activity.getResources().getString(R.string.app_name)));
                intent2.putExtra("sub_text", activity.getResources().getString(R.string.user_agreement));
                intent2.putExtra("toobar_bg_id", R.color.color_main);
                activity.startActivity(intent2);
            }
        });

        TextView tv2 = new TextView(activity);

        tv2.setText("《" + activity.getResources().getString(R.string.privacy_policy) + "》");
        tv2.setTextSize(16);
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(Color.BLUE);
        tv2.setPadding(20, 20, 20, 80);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WebViewX5Acitivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("text", url);
                intent.putExtra("title", activity.getResources().getString(R.string.privacy_policy));
                intent.putExtra("toobar_bg_id", R.color.color_main);
                activity.startActivity(intent);
            }
        });

        LinearLayout linearLayout = new RoundLinearLayout(activity);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        linearLayout.addView(tv1);
        linearLayout.addView(tv2);
        setMargins(tv1, 50, 20, 0, 0);
        setMargins(tv2, 50, 20, 0, 0);

        LinearLayout linearLayout2 = new RoundLinearLayout(activity);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout2.setGravity(Gravity.CENTER);
        TextView msg = new TextView(activity);
        msg.setText(activity.getResources().getString(R.string.read_private));
        msg.setTextSize(16);
        msg.setGravity(Gravity.CENTER);
        msg.setPadding(40, 40, 40, 40);

        linearLayout2.addView(msg);
        linearLayout2.addView(linearLayout);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCustomTitle(title);
        builder.setView(linearLayout2);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getResources().getString(R.string.readok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SpUtils.putBoolean("isFirst", true);
                dialogClickBack.onClickDialog(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.later), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
                dialogClickBack.onClickDialog(false);
                dialog.dismiss();
            }
        });
        final AlertDialog show = builder.show();
        final Button positiveButton = show.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button negativeButton = show.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams positiveParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveParams.gravity = Gravity.CENTER;
        positiveParams.setMargins(30, 30, 30, 30);
        positiveParams.width = 0;
        // 安卓下面有三个位置的按钮，默认权重为 1,设置成 500或更大才能让两个按钮看起来均分
        positiveParams.weight = 500;
        LinearLayout.LayoutParams negativeParams = (LinearLayout.LayoutParams) negativeButton.getLayoutParams();
        negativeParams.gravity = Gravity.CENTER;
        negativeParams.setMargins(30, 30, 30, 30);
        negativeParams.width = 0;
        negativeParams.weight = 500;
        positiveButton.setLayoutParams(positiveParams);
        negativeButton.setLayoutParams(negativeParams);
        positiveButton.setBackgroundColor(activity.getResources().getColor(R.color.color_main));
        positiveButton.setTextColor(activity.getResources().getColor(R.color.white));
        negativeButton.setBackgroundColor(Color.parseColor("#DDDDDD"));
        negativeButton.setTextColor(activity.getResources().getColor(android.R.color.black));

    }

    private static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
}


