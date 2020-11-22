package com.anguomob.ads.utils

import android.app.Activity
import android.os.SystemClock
import android.util.Log
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError


class QqAds {

    companion object {
        private const val TAG = "QqAds"
        lateinit var mRewardVideoAD: RewardVideoAD
        lateinit var tableplaqueAd: UnifiedInterstitialAD
        fun loadIncentiveVideo(
            activity: Activity,
            postId: String,
            lis: RewardVideoADListener
        ): RewardVideoAD {
            val res = RewardVideoAD(activity, postId, lis)
            res.loadAD()
            return res
        }

        //激励广告
        fun loadQQRewardVideoAD(activity: Activity, postId: String) {

            mRewardVideoAD = loadIncentiveVideo(activity, postId, object : RewardVideoADListener {
                override fun onADExpose() {
                    Log.e(TAG, "onADExpose: " )
                }

                override fun onADClick() {
                    Log.e(TAG, "onADClick: " )
                }

                override fun onVideoCached() {
                    Log.e(TAG, "onVideoCached: " )
                }

                override fun onReward() {
                    Log.e(TAG, "onReward: " )
                }

                override fun onADClose() {
                    Log.e(TAG, "onADClose: " )
                }

                override fun onADLoad() {
                    Log.e(TAG, "onADLoad: " )
                    if (SystemClock.elapsedRealtime() < (mRewardVideoAD.getExpireTimestamp() - 1000)) {
                        mRewardVideoAD.showAD()
                    }

                }

                override fun onVideoComplete() {
                    Log.e(TAG, "onVideoComplete: " )
                }

                override fun onError(p0: AdError) {
                    Log.e(TAG, "onError: errorMsg"+p0.errorMsg )
                    Log.e(TAG, "onError: errorCode"+p0.errorCode )
                }

                override fun onADShow() {
                    Log.e(TAG, "onADShow: " )
                }
            })


        }

        // 插屏广告加载
        fun loadQQtableplaqueAD(activity: Activity, postId: String) {
            tableplaqueAd =
                UnifiedInterstitialAD(activity, postId, object : UnifiedInterstitialADListener {
                    override fun onADExposure() {
                        Log.e(TAG, "onADExposure: " )
                    }

                    override fun onVideoCached() {
                        Log.e(TAG, "onVideoCached: " )
                    }

                    override fun onADOpened() {
                        Log.e(TAG, "onADOpened: " )
                    }

                    override fun onADClosed() {
                        Log.e(TAG, "onADClosed: " )
                    }

                    override fun onADLeftApplication() {
                        Log.e(TAG, "onADLeftApplication: " )
                    }

                    override fun onADReceive() {
                        Log.e(TAG, "onADReceive: " )
                        tableplaqueAd.show()
                    }

                    override fun onNoAD(p0: AdError?) {
                        Log.e(TAG, "onError: errorMsg"+p0?.errorMsg )
                        Log.e(TAG, "onError: errorCode"+p0?.errorCode )
                    }

                    override fun onADClicked() {
                        Log.e(TAG, "onADClicked: " )
                    }

                })


            val builder: VideoOption.Builder = VideoOption.Builder()
            var option: VideoOption = builder.build()
            tableplaqueAd.setVideoOption(option)
            tableplaqueAd.loadAD()


        }
    }


}




