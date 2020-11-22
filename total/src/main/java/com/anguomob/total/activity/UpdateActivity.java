package com.anguomob.total.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anguomob.total.R;
import com.anguomob.total.utils.AppUtils;
import com.baidu.autoupdatesdk.AppUpdateInfo;
import com.baidu.autoupdatesdk.AppUpdateInfoForInstall;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.CPCheckUpdateCallback;
import com.baidu.autoupdatesdk.CPUpdateDownloadCallback;


public class UpdateActivity extends AppCompatActivity {
    Context mContext;
    private Button uploadBut;
    private TextView laterTv;
    private TextView ignoreTv;
    public CPUpdateDownloadCallback callBack;
    private TextView txtTip;
    private TextView txtTitle;

    private String change;
    private String versionName;

    AppUpdateInfo updateInfo;

    private ProgressBar processBar;
    private LinearLayout confirmLin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mContext = getApplicationContext();
        change = getIntent().getStringExtra("change");
        versionName = getIntent().getStringExtra("versionName");
        findViewById();
    }

    public void findViewById() {

        uploadBut = (Button) findViewById(R.id.btn_action_1);

        laterTv = (TextView) findViewById(R.id.txt_action_2);

        ignoreTv = (TextView) findViewById(R.id.txt_action_3);

        txtTip = (TextView) findViewById(R.id.txt_minor_tip);

        txtTitle = (TextView) findViewById(R.id.txt_title);

        processBar = (ProgressBar) findViewById(R.id.processBar);

        confirmLin = (LinearLayout) findViewById(R.id.confirmLin);

        BDAutoUpdateSDK.cpUpdateCheck(mContext, new CPCheckUpdateCallback() {
            @Override
            public void onCheckUpdateCallback(AppUpdateInfo appUpdateInfo, AppUpdateInfoForInstall appUpdateInfoForInstall) {


                if (!(null == appUpdateInfo && null == appUpdateInfoForInstall)) {

                    updateInfo = appUpdateInfo;

                    uploadBut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(mContext, R.string.bdp_update_alert, Toast.LENGTH_SHORT).show();

                            BDAutoUpdateSDK.cpUpdateDownload(mContext, updateInfo, new CPUpdateDownloadCallback() {
                                @Override
                                public void onStart() {

                                    processBar.setVisibility(View.VISIBLE);
                                    uploadBut.setBackgroundColor(Color.TRANSPARENT);

                                }

                                @Override
                                public void onPercent(int i, long l, long l1) {

                                    processBar.setProgress(i);
                                    processBar.setMax(100);
                                    uploadBut.setText(R.string.bdp_update_process);
                                }

                                @Override
                                public void onDownloadComplete(String s) {

                                    BDAutoUpdateSDK.cpUpdateInstall(mContext, s);
                                    finish();

                                }

                                @Override
                                public void onFail(Throwable throwable, String s) {

                                    Toast.makeText(mContext, R.string.bdp_update_request_net_error, Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onStop() {

                                }
                            });

                        }

                    });


                }

            }
        });


        txtTip.setText(Html.fromHtml(change));
        laterTv.setVisibility(View.INVISIBLE);
        ignoreTv.setVisibility(View.INVISIBLE);
        uploadBut.setText(R.string.bdp_update_action_download);
        txtTitle.setText(String.format(getResources().getString(R.string.bdp_update_title_download2), AppUtils.getVersionName(this),versionName));

        confirmLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
