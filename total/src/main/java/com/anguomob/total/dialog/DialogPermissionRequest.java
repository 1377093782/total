package com.anguomob.total.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anguomob.total.R;


/**
 * 带titleAlertDialog
 */
public class DialogPermissionRequest extends Dialog {
    public static DialogPermissionRequest mInstance;
    private TextView bt_left, bt_right;
    private static Context mContext;
    private TextView tv_message;

    public DialogPermissionRequest(Context mContext) {
        super(mContext);
    }

    private static final String TAG = "DialogBindorRegister";

    public DialogPermissionRequest(Context context, int theme) {
        super(context, theme);
    }

    public static DialogPermissionRequest getIstance(Context context) {
        mContext = context;
        try {
            mInstance = new DialogPermissionRequest(context, R.style.MyDialog);
            return mInstance;
        } catch (Exception e) {
            Log.e(TAG, "getIstance: " + e.toString());
            mInstance = null;
            return null;
        }
    }

    //    @Override
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//需要在设置内容之前定义
//        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        setContentView(R.layout.dialog_setting_onetoggle);
//
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_request_permission);
        initViews();
    }

    /**
     * 确定按钮接口
     */
    public interface onNoOnclickListener {
        public void onNoClick();
    }

    /**
     * 取消按钮接口
     */
    public interface onYesOnclickListener {
        public void onYesOnclick();
    }


    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(onNoOnclickListener onNoOnclickListener) {

        this.noOnclickListener = onNoOnclickListener;
    }

    private static final int MSG_SET_TEXT = 1001;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_TEXT:
                    Bundle data = msg.getData();
                    String text = "";
                    if (data != null) {
                        text = data.getString("text");
                    }
                    setMessage(text);
                    break;
            }
        }
    };

    public void setMessage(String text) {
        if (tv_message != null) {
            tv_message.setText(text);
            handler.removeCallbacksAndMessages(null);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("text", text);
            Message message = new Message();
            message.setData(bundle);
            message.what = MSG_SET_TEXT;
            handler.sendMessageDelayed(message, 100);

        }

    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setYesOnclickListener(onYesOnclickListener yesOnclickListener) {
        this.yesOnclickListener = yesOnclickListener;
    }


    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器


    void initViews() {
        bt_left = (TextView) findViewById(R.id.tv_dso_canel);
        bt_right = (TextView) findViewById(R.id.tv_dso_ok);
        tv_message = (TextView) findViewById(R.id.tv_da_message);


        AssetManager mgr = mContext.getAssets();//得到AssetManager  
//        Typeface tf = Typeface.createFromAsset(mgr, "fonts/PingFang Medium.ttf");//根据路径得到Typeface  
//        bt_right.setTypeface(tf);//设置字体  
//        bt_left.setTypeface(tf);//设置字体  

        //设置确定按钮被点击后，向外界提供监听
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesOnclick();
                }
            }
        });


    }
}
