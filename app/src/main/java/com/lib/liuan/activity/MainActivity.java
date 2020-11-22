package com.lib.liuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.anguomob.total.utils.SettingUtils;
import com.anguomob.total.activity.ShowTextActivity;
import com.anguomob.total.activity.WebViewX5Acitivity;
import com.anguomob.total.dialog.DialogUtils;
import com.anguomob.total.init.AnguoUtils;
import com.anguomob.total.utils.LogUtils;
import com.anguomob.total.utils.OKHttpUtils;
import com.anguomob.total.utils.PrivacyUserAgreementUtils;
import com.anguomob.total.utils.ToastUtils;
import com.anguomob.total.Anguo;
import com.lib.liuan.R;

import org.json.JSONObject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private LinearLayout mMian;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        ButterKnife.bind(this);
        AnguoUtils.init(this);


    }


    private void showNetWork() {
        String httpUrl = "http://www.wanandroid.com/tools/mockapi/11904/freereader";

        try {
            String res = OKHttpUtils.loadStringFromUrl(httpUrl);
            if (TextUtils.isEmpty(res)) {
                return;
            }
            JSONObject jsonObj = new JSONObject(res);
            if (jsonObj.has("isShowCloseAd")) {
                boolean isShowCloseAd = jsonObj.getBoolean("isShowCloseAd");

                LogUtils.e(isShowCloseAd + "");
            }

            if (jsonObj.has("clickNextTime")) {
                int clickNextTime = jsonObj.getInt("clickNextTime");
                setTitle(clickNextTime + "");
                LogUtils.e(clickNextTime + "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.toString());
        }

    }

    private void initView() {

        mMian = (LinearLayout) findViewById(R.id.mian);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;

        }
    }

    public void btCheckUpdate(View view) {
//        Intent intent = new Intent(this, UpdateActivity.class);
//        intent.putExtra("change", "更新日志 吧啦吧啦");
//        intent.putExtra("versionName", "1.0.1");
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(intent);
        AnguoUtils.checkUpDate(this);
    }

    public void btnFeedBack(View view) {
        SettingUtils.INSTANCE.feedBack(this,"测试测试");
    }

    public void btnGuiQiuPraise(View view) {
        SettingUtils.INSTANCE.fiveStar(this,"com.liuan.videowallpaper");
    }

    public void btnExit(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {

    }

    public void btnBrowser(View view) {
        Intent intent2 = new Intent(this, WebViewX5Acitivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("title", "隐私政策");
        intent2.putExtra("url", "https://doc.liuan.wiki/web/#/8?page_id=40");
        intent2.putExtra("toobar_bg_id", R.color.color_main);
       startActivity(intent2);
    }
}
