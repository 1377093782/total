package com.anguomob.total.net;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.anguomob.total.R;
import com.anguomob.total.init.AnguoUtils;
import com.anguomob.total.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OkManager {
    private Gson gson = null;
    private OkHttpClient client;
    private volatile static OkManager manager;
    private static final String TAG = "OkManager";
    private Handler handler;
    private Request.Builder builder = new Request.Builder();
    //提交json数据
    private static final MediaType JSON_UTF8 = MediaType.parse("application/json;charset=utf-8");
    //提交字符串
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
    //提交json非utf-8数据
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType application_JSON = MediaType.parse("Content-Type:application/json");

    private OkManager() {
        client = new OkHttpClient()
                .newBuilder()

                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        client.writeTimeoutMillis();
        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());

        //        builder.header("Accept","application/json");
//        builder.header("Accept-Encoding", "application/json");
//        builder.header("Accept", "*/*");
        builder.header("Cache-Control", "no-cache");


    }

    public static OkManager instance = null;

    //采用单例模式获取对象
    public static OkManager getInstance() {


        if (instance == null) {
            instance = new OkManager();
        }
        return instance;
    }

    /**
     * 同步请求数据 在android 开发中不常用，因为阻塞UI线程
     *
     * @param url
     * @return
     */

    public String syncGitByURL(String url) {
        if (!com.anguomob.total.net.NetworkUtils.checkedNetwork(AnguoUtils.getContext())) {// 网络请求之前先检查网络是否可用
            ToastUtils.showShort(AnguoUtils.getContext().getString(R.string.net_err));
            return "";
        }
        Request request = builder.url(url).get().build();
        //构建一个request请求
        Response response = null;
        try {
            //同步请求数据
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getLocation(String finalPlayUrl, final CallBackString callback) {
        String res = null;
        if (!com.anguomob.total.net.NetworkUtils.checkedNetwork(AnguoUtils.getContext())) {// 网络请求之前先检查网络是否可用
            ToastUtils.showShort(AnguoUtils.getContext().getString(R.string.net_err));
            return;
        }
        Request.Builder builder = new Request.Builder();
        builder.header("accept", "*/*");
        builder.header("connection", "Keep-Alive");
        Request request = builder.url(finalPlayUrl).get().build();
        Response response = null;

        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .followRedirects(false)
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        client.writeTimeoutMillis();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 302) {
                    String location = response.headers().get("Location");
                    onSuccessJsonStringMethod(location, callback);
                }
            }
        });

    }


    /**
     * 请求指定的url返回的记过是json字符串
     *
     * @param url
     * @param callBack
     */
    public void asyncJsonStringByURL(String url, final CallBackString callBack,final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        final Request request = builder.url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callBack);
                } else {
                    onFailureCallBack(code, response.body().string(), errorCallBack);
                }
            }
        });

    }


    /**
     * 请求返回的结果json 字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue,final CallBackString callBack) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static String getTopActivity(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ComponentName cn = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            cn = am.getRunningTasks(1).get(0).topActivity;
        }
//        Log.d("Chunna.zheng", "pkg:"+cn.getPackageName());//包名
//        Log.d("Chunna.zheng", "cls:"+cn.getClassName());//包名加类名
        if (cn != null && cn.getClass() != null && cn.getClass().getSimpleName() != null) {
            return cn.getClass().getSimpleName();
        }
        return "";
    }

    private void onFailureCallBack(final int errorCode, String errorMsage,final CallBackError callBack) {
        String message = "";
        Log.e(TAG, "onFailureCallBack: " + errorMsage + errorCode);
        try {
            JSONObject jsonObject = new JSONObject(errorMsage);
            message = jsonObject.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String simpleName = getTopActivity(AnguoUtils.getContext());
        Log.e(TAG, "onFailureCallBack: " + simpleName);


        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onResponse(errorCode);
                }
            }
        });
    }


    /**
     * 请求返回的结果json 对象
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSucessJsonObjectMethod(final String jsonValue, final CallBackJsonObject callBack) {
//        Log.e(TAG, "onSucessJsonObjectMethod: jsonValue" + jsonValue);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null && !TextUtils.isEmpty(jsonValue)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(jsonValue);
                    } catch (JSONException e) {
                        throw new UnsupportedOperationException("json 解析错误" + jsonValue + e.toString());
                    }
                    boolean status = false;
                    try {
                        status = jsonObject.getBoolean("status");

                    } catch (JSONException e) {
                        throw new UnsupportedOperationException("json 解析错误 status的为空");
                    }
                    String msg = "";
                    try {
                        msg = jsonObject.getString("msg");

                    } catch (JSONException e) {
                        throw new UnsupportedOperationException("json 解析错误 msg");
                    }
                    String data = "";
                    try {
                        data = jsonObject.getString("data");

                    } catch (JSONException e) {
//                        throw new UnsupportedOperationException("json 解析错误 data");
                    }

                    callBack.onResponse(jsonObject, msg, status, data);
                }
            }
        });
    }

    public void sayncJsonObkectByURL(String url, final CallBackJsonObject callBack,final  CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        final Request request = builder.url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callBack);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    public void asyncGetByteByURL(String url, final CallBackByte callBack,final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        final Request request = builder.url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSucessByteMethod(response.body().bytes(), callBack);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    /**
     * 请求返回json对象
     *
     * @param url
     * @param callback
     * @param errorCallBack
     */

    public void asyncGetJsonObjectByURL(String url, final CallBackJsonObject callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        Request request = builder.url(url).get().build();
        Log.e(TAG, "onResponse:url   " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers requestHeaders = response.networkResponse().request().headers();
                Log.e(TAG, "onResponse:requestHeaders   " + requestHeaders);
                Log.e(TAG, "onResponse:response   " + response.toString());
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    /**
     * 请求返回json对象
     *
     * @param url
     * @param callback
     * @param errorCallBack
     */
    public void asyncGetStringByURL(String url, final CallBackString callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort(R.string.net_err);
            return;
        }
        Request request = builder.url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers requestHeaders = response.networkResponse().request().headers();
                HttpUrl url1 = response.networkResponse().request().url();
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }


    private boolean initNetWork(CallBackError errorCallBack) {
        if (!com.anguomob.total.net.NetworkUtils.checkedNetwork(AnguoUtils.getContext())) {// 网络请求之前先检查网络是否可用
            String net_error = AnguoUtils.getContext().getString(R.string.net_err);
//            ToastUtils.showShort(net_error);
            onFailureCallBack(-1, net_error, errorCallBack);
            return true;
        }
        return false;
    }

    public void asyncGetJsonObjectByURL(String url, HashMap hm, final CallBackJsonObject callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }

        Iterator<Map.Entry<String, String>> iterator = hm.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            index++;
            Map.Entry<String, String> next = iterator.next();
            if (index == 1) {
                url += "?" + next.getKey() + "=" + next.getValue();
            } else {
                url += "&" + next.getKey() + "=" + next.getValue();
            }
        }
        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(hm));
        Request request = builder.url(url).method("get", requestBody).get().build();
        Log.e(TAG, "onResponse:url   " + url);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers requestHeaders = response.networkResponse().request().headers();
                Log.e(TAG, "onResponse:requestHeaders   " + requestHeaders);
                Log.e(TAG, "onResponse:response   " + response.toString());
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }


    /**
     * 采用post提交表单
     *
     * @param url
     * @param params
     * @param callBack
     * @param errorCallBack
     */

    public void sendComplexForm(String url, Map<String, String> params, final CallBackJsonObject callBack, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        //表单对象，包含以input开始的对象
        FormBody.Builder builder1 = new FormBody.Builder();
        if (params != null && params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder1.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody requestBody = builder1.build();
        //采用post提交
        Request request = builder.url(url).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callBack);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    /**
     * 向服务器提交数据
     *
     * @param url
     * @param content
     * @param callback
     * @param callBackError
     */
    public void sendStringByPostMethod(String url, String content, final CallBackJsonObject callback, final CallBackError callBackError) {
        if (initNetWork(callBackError)) {
            return;
        }
        Context context = AnguoUtils.getContext();


        final Request request = builder.url(url).post(RequestBody.create(JSON, content)).build();
        Log.e(TAG, "onResponse:sendStringByPostMethod:response " + content);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse:sendStringByPostMethod:response " + response);
                Headers headers = response.networkResponse().request().headers();
                Log.e(TAG, "onResponse:sendStringByPostMethod:headers " + headers);
                if (response != null && response.isSuccessful()) {

                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), callBackError);
                }
            }
        });

    }

    /**
     * 向服务器提交数据
     *
     * @param url
     * @param hashMap
     * @param callback
     */
    public void sendStringByPostMethod(String url, HashMap hashMap, final CallBackJsonObject callback, final CallBackError callBackError) {
        if (initNetWork(callBackError)) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            throw new UnsupportedOperationException("url 不得为空");
        }
        //{"password":"ayysyhsys","username":"ahhshshh"}

        RequestBody requestBody = RequestBody.create(JSON, gson.toJson(hashMap));
        FormBody.Builder builder = new FormBody.Builder();

        Iterator<Map.Entry<String, String>> iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            if (next.getValue() != null) {
                builder.add(next.getKey(), next.getValue());
            }

        }
        FormBody build = builder.build();
        Request request = this.builder.url(url).post(build).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Headers headers = response.networkResponse().request().headers();
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), callBackError);
                }

            }
        });


//

    }


    /**
     * 向服务器提交数据
     *
     * @param url
     * @param content
     * @param callback
     * @param callBackError
     */
    public void sendStringByPostMethodApplicationJson(String url, String content, final CallBackJsonObject callback, final CallBackError callBackError) {
        if (initNetWork(callBackError)) {
            return;
        }
        RequestBody requestBody = new RequestBody() {

            @Override
            public MediaType contentType() {
                return MediaType.parse("application/json");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

            }
        };
        MediaType mediaType = requestBody.contentType();

        builder.header("Content-Type", "application/json");
        final Request request = builder.url(url).post(FormBody.create(application_JSON, content)).build();
        builder.header("Content-Type", "");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.networkResponse().request().headers();
                if (response != null && response.isSuccessful()) {

                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), callBackError);
                }
            }
        });

    }

    public void sendStringByPutMethod(String url, String o, final CallBackJsonObject callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        final Request request = builder.url(url).put(RequestBody.create(JSON, o)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.networkResponse().request().headers();
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    public void sendStringByPutMethod(String url, Object o, final CallBackJsonObject callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        Request request = builder.url(url).put(RequestBody.create(JSON, gson.toJson(o))).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Headers headers = response.networkResponse().request().headers();
                if (response != null && response.isSuccessful()) {
                    onSucessJsonObjectMethod(response.body().string(), callback);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    public void asyncDownLoadImageByURL(String url, final CallBackBitmap callback, final CallBackError errorCallBack) {
        if (initNetWork(errorCallBack)) {
            return;
        }
        final Request request = builder.url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                netWordFaild(call, e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] data = response.body().bytes();
//                    Bitmap bitmap = new CropSquareTrans().transform(BitmapFactory.decodeByteArray(data, 0, data.length));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    callback.onResponse(bitmap);
                } else {
                    onFailureCallBack(response.code(), response.body().string(), errorCallBack);
                }
            }
        });
    }

    private void netWordFaild(Call call, IOException e) {
        if (e instanceof SocketTimeoutException) {//判断超时异常
            ToastUtils.showShort("Socket网络请求超时");
            return;
        }
        if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144
            ToastUtils.showShort("网络请求超时");
            return;
        }
        ToastUtils.showShort("网络错误");


        Log.e(TAG, "网络错误" + e.toString());
    }

    /**
     * 请求返回的字节集数组对象
     *
     * @param data
     * @param callBack
     */
    private void onSucessByteMethod(final byte[] data,final CallBackByte callBack) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addHeader(String access_token, String accessToken) {
        if (builder != null) {
            builder.header(access_token, accessToken);
        }
    }


    public interface CallBackString {
        void onResponse(String result);
    }

    public interface CallBackByte {
        void onResponse(byte[] result);
    }

    public interface CallBackBitmap {
        void onResponse(Bitmap bitmap);
    }

    public interface CallBackJsonObject {
        void onResponse(JSONObject jsonObject, String message, boolean status, String data);
    }

    public interface CallBackError {
        void onResponse(int errorCode);
    }


}
