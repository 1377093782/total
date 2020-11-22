package com.anguomob.total.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager

import com.anguomob.total.bean.DataForYzdzy

import com.anguomob.total.R
import com.anguomob.total.adapter.OtherAppAdapter
import com.anguomob.total.utils.OKHttpUtils
import com.anguomob.total.utils.StatusBarUtil
import com.anguomob.total.utils.ToastUtils
import com.anguomob.total.utils.ToolbarUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_other_app.*

class OtherAppActivity : AppCompatActivity() {
    private val TAG = "OtherAppActivity"
    var myadapter: OtherAppAdapter? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_other_app)
        var t: Toolbar = findViewById(R.id.toolbar);
        t.setBackgroundColor(resources.getColor(R.color.app_green))
        StatusBarUtil.initStatusBar(this,true,R.color.app_green)
        ToolbarUtils.setToobar(R.string.other_app, t, this)

        myadapter = OtherAppAdapter();
        rv_other_app.layoutManager = LinearLayoutManager(this);
        rv_other_app.adapter = myadapter;

        Thread({
            kotlin.run {
                try {


                    val loadStringFromUrl =
                        OKHttpUtils.loadStringFromUrl("https://www.yzdzy.com/app/ad/v3/index.php?market_type=android");
//            1.String转JSON
                    val chanelAd: DataForYzdzy =
                        Gson().fromJson<DataForYzdzy>(loadStringFromUrl, DataForYzdzy::class.java)
                    runOnUiThread({
                        myadapter?.setData(chanelAd.data)
                    })
                } catch (e: Exception) {
                    ToastUtils.showShort("网络出现了点问题，请稍后再试")


                }


            }
        }).start()

    }
}