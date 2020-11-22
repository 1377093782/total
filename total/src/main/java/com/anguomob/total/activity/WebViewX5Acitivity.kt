package com.anguomob.total.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife

import com.anguomob.total.view.X5WebView
import com.anguomob.total.R
import com.anguomob.total.utils.StatusBarUtil
import com.anguomob.total.utils.ToolbarUtils
import com.tencent.smtt.sdk.WebView

/**
 * Create by: liuan
 * Create date: 2020-10-19 0019
 * Describe:
 */
class WebViewX5Acitivity : AppCompatActivity() {
    lateinit var mWebView: X5WebView
    lateinit var mToolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5_webview)
        mWebView = findViewById<X5WebView>(R.id.forum_context);
        mToolbar = findViewById<Toolbar>(R.id.toolbar)

        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        val toobar_bg = intent.getIntExtra("toobar_bg_id", -1)

        ToolbarUtils.setToobar(title, mToolbar, this)
        if (toobar_bg != -1) {
            StatusBarUtil.initStatusBar(this, false, toobar_bg)
            mToolbar.setBackground(resources.getDrawable(toobar_bg))
        } else {
            mToolbar.setBackgroundColor(resources.getColor(R.color.color_main))
        }

        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url)
        }





    }

    // 5、覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack() //goBack()表示返回WebView的上一页面
                true
            } else {
                finish()
                true
            }
        } else false
    }

}