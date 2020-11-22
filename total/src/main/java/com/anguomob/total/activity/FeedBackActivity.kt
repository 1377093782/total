package com.anguomob.total.activity

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.anguomob.total.R
import com.anguomob.total.net.OkManager
import com.anguomob.total.utils.StatusBarUtil
import com.anguomob.total.utils.ToastUtils
import com.anguomob.total.utils.ToolbarUtils
import com.lxj.xpopup.XPopup
import org.json.JSONObject

/**
 * Create by: liuan
 * Create date: 2020-10-16 0016
 * Describe:
 */
class FeedBackActivity : AppCompatActivity() {
    //将反馈内容提交给
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)


        val toobar_bg = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar);

        StatusBarUtil.initStatusBar(this, false, R.color.color_main)
        ToolbarUtils.setToobar(R.string.feed_back, toobar_bg, this)
        toobar_bg.setBackgroundColor(resources.getColor(R.color.color_main))

    }


    fun feedback_click(view: View) {
        val content = findViewById<EditText>(R.id.feedback_content_edit).text.toString().trim();
        val contact = findViewById<EditText>(R.id.feedback_contact_edit).text.toString().trim();
        if (content.length >= 1000) {
            ToastUtils.showShort(R.string.feed_word_more_1000)
            return
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(getString(R.string.feed_text_not_allow_empty))
            return
        }
        if (contact.length >= 100) {
            ToastUtils.showShort(getString(R.string.contact_more_100))
            return
        }


        //判断数据 然后提交
        Thread {
            kotlin.run {
                try {
                    val url = "https://www.yzdzy.com/app/lib/v1/feedback/feed_back.php"
                    val data = HashMap<String, String>()
                    val app_name_intent = intent.getStringExtra("app_name");
                    val app_name = if (TextUtils.isEmpty(app_name_intent)) resources.getString(R.string.app_name) else app_name_intent
                    data.put("app_name", app_name)
                    data.put("package_name", packageName)
                    data.put("content", content)
                    data.put("contact", contact)

                    OkManager.getInstance().sendStringByPostMethod(url, data, object : OkManager.CallBackJsonObject {
                        override fun onResponse(jsonObject: JSONObject, message: String, status: Boolean, data: String) {
                            runOnUiThread {
                                if (status) {


                                    var build = XPopup.Builder(this@FeedBackActivity).asConfirm(getString(R.string.feedback_success),message,{
                                        finish()
                                    })
                                    build.isHideCancel = true
                                    build.show()


                                } else {
                                    ToastUtils.showShort(message)
                                }

                            }

                        }
                    }, object : OkManager.CallBackError {
                        override fun onResponse(errorCode: Int) {
                            ToastUtils.showShort(R.string.net_err)
                        }
                    })

                } catch (e: Exception) {
                    ToastUtils.showShort(resources.getString(R.string.net_err) + e.message)
                }


            }
        }.start()

    }
}