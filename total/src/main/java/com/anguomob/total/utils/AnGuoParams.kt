package com.anguomob.ads.utils

import android.text.TextUtils
import android.util.Log
import com.anguomob.total.bean.DataForYzdzy
import com.anguomob.total.utils.UmUtils
import com.anguomob.total.init.AnguoUtils
import com.anguomob.total.utils.OKHttpUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

/**
 * Create by: liuan
 * Create date: 2020-08-11 0011
 * Describe: 网络参数 是否显示
 */
class AnGuoParams {
    companion object {
        private const val TAG = "AnGuoParams"

        fun initNetWorkParams() {
            Thread({
                kotlin.run {

                    try {
                        val loadStringFromUrl =
                            OKHttpUtils.loadStringFromUrl("https://www.yzdzy.com/app/ad/v3/index.php?package_name=${(AnguoUtils.getContext().packageName)}");
                        MMKV.defaultMMKV().encode("net_ad_params", loadStringFromUrl)

                        //            1.String转JSON
                        var chanelAd: DataForYzdzy =
                            Gson().fromJson<DataForYzdzy>(
                                loadStringFromUrl,
                                DataForYzdzy::class.java
                            )
                        val data = chanelAd.data.get(0);

                        val chaneladDataOne = chanelAd.data.get(0);
                        AnguoAds.showAd = "1".equals(data.show_ad);
                        AnguoAds.mAdType = data.ad_type;
//                        AnguoGames.show_game = "1".equals(data.show_game);
//                        AnguoGames.game_type = data.game_type;
                        Log.e(TAG, ": " + data.toString())

                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "initNetWorkParams: " + e.toString())

                    }
                }
            }).start()

        }

        fun getNetParamsByChannel(): Boolean {
            val loadStringFromUrl = MMKV.defaultMMKV().decodeString("net_ad_params");
            if (TextUtils.isEmpty(loadStringFromUrl)) {
                return false;
            }
//            1.String转JSON
            val chanelAd: DataForYzdzy =
                Gson().fromJson<DataForYzdzy>(loadStringFromUrl, DataForYzdzy::class.java)
            try {
                val data = chanelAd.data.get(0);
                val split = data.channel.split("|");
                val contains = split.contains(UmUtils.getUmChannel());
                return contains;
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "getNetParamsByChannel: " + e.toString())

            }
            return false

        }

        fun getNetParamsByParams(key: String): Boolean {
            val loadStringFromUrl = MMKV.defaultMMKV().decodeString("net_ad_params");
            if (TextUtils.isEmpty(loadStringFromUrl)) {
                return false;
            }
//            1.String转JSON
            val chanelAd: DataForYzdzy =
                Gson().fromJson<DataForYzdzy>(loadStringFromUrl, DataForYzdzy::class.java)
            try {
                val data = chanelAd.data.get(0);
                val split = data.params.split("|");
                split.forEach { i ->
                    if (i.contains(key)) {
                        return true
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e(TAG, "getNetParamsByParams: " + e.toString())

            }
            return false

        }


        fun getNetWorkParams(): DataForYzdzy? {
            val loadStringFromUrl = MMKV.defaultMMKV().decodeString("net_ad_params");
            if (TextUtils.isEmpty(loadStringFromUrl)) {
                return null;
            }
//            1.String转JSON
            val chanelAd: DataForYzdzy =
                Gson().fromJson<DataForYzdzy>(loadStringFromUrl, DataForYzdzy::class.java)

            return chanelAd;
        }


    }
}