package com.anguomob.ads.utils

import android.R.attr.orientation
import android.app.Activity
import android.util.Log
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.TTAdNative.*
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd.FullScreenVideoAdInteractionListener
import com.bytedance.sdk.openadsdk.TTRewardVideoAd.RewardAdInteractionListener
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.rewardvideo.RewardVideoAD


class ChuanShanJiaAds {
    companion object {
        lateinit var mRewardVideoAD: RewardVideoAD
        lateinit var tableplaqueAd: UnifiedInterstitialAD

        private const val TAG = "ChuanShanJiaAds"

        //激励广告
        fun loadRewardVideoAD(activity: Activity, postId: String) {
            val adSlot = AdSlot.Builder()
                .setCodeId(postId)
                .setSupportDeepLink(true)
                .setExpressViewAcceptedSize(500f, 500f)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3) //奖励的数量
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                .setOrientation(TTAdConstant.VERTICAL) //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
                .build()

            val adManager = TTAdSdk.getAdManager();
            adManager.createAdNative(activity).loadRewardVideoAd(adSlot,
                object : RewardVideoAdListener {
                    override fun onError(code: Int, message: String) {
                        Log.e(TAG, "onError:message " + message)
                        Log.e(TAG, "onError:code " + code)
                    }

                    //视频广告加载后的视频文件资源缓存到本地的回调
                    override fun onRewardVideoCached() {
                        Log.e(TAG, "onRewardVideoCached: ")
                    }

                    //视频广告素材加载到，如title,视频url等，不包括视频文件
                    override fun onRewardVideoAdLoad(ad: TTRewardVideoAd) {
                        Log.e(TAG, "onRewardVideoAdLoad: ")
                        //mttRewardVideoAd.setShowDownLoadBar(false);
                        ad.showRewardVideoAd(activity)
                        ad.setRewardAdInteractionListener(object :
                            RewardAdInteractionListener {
                            override fun onAdShow() {
                                Log.e(TAG, "onAdShow: ")
                            }

                            override fun onAdVideoBarClick() {
                                Log.e(TAG, "onAdVideoBarClick: ")

                            }

                            override fun onAdClose() {
                                Log.e(TAG, "onAdClose: ")

                            }

                            override fun onVideoError() {
                                Log.e(TAG, "onVideoError: ")
                            }

                            override fun onRewardVerify(
                                p0: Boolean,
                                p1: Int,
                                p2: String?,
                                p3: Int,
                                p4: String?
                            ) {
                                Log.e(TAG, "onRewardVerify: ")
                            }

                            override fun onVideoComplete() {
                                Log.e(TAG, "onVideoComplete: ")
                            }


                            override fun onSkippedVideo() {

                            }
                        })
                        ad.setDownloadListener(object : TTAppDownloadListener {
                            override fun onIdle() {}
                            override fun onDownloadActive(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                            }

                            override fun onDownloadPaused(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                            }

                            override fun onDownloadFailed(
                                totalBytes: Long,
                                currBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                            }

                            override fun onDownloadFinished(
                                totalBytes: Long,
                                fileName: String,
                                appName: String
                            ) {
                            }

                            override fun onInstalled(
                                fileName: String,
                                appName: String
                            ) {
                            }
                        })
                    }
                })
        }

        // 插屏广告加载
        fun loadScreenVideoAD(activity: Activity, postId: String) {
            //设置广告参数

            //设置广告参数

            //设置广告参数
            val adSlot = AdSlot.Builder()
                .setCodeId(postId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(orientation)
                .build()
            //加载广告

            //加载广告
            val adManager = TTAdSdk.getAdManager();
            adManager.createAdNative(activity).loadFullScreenVideoAd(adSlot,
                object : FullScreenVideoAdListener {
                    override fun onError(code: Int, message: String) {
                        Log.e(TAG, "onError: " + message)
                        Log.e(TAG, "onError: " + code)
                    }

                    override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd) {
                        ad.showFullScreenVideoAd(activity)

                        ad.setFullScreenVideoAdInteractionListener(object :
                            FullScreenVideoAdInteractionListener {
                            override fun onAdShow() {
                                Log.e(TAG, "onAdShow: ")
                            }

                            override fun onAdVideoBarClick() {
                                Log.e(TAG, "onAdVideoBarClick: ")
                            }

                            override fun onAdClose() {
                                Log.e(TAG, "onAdClose: ")
                            }

                            override fun onVideoComplete() {
                                Log.e(TAG, "onVideoComplete: ")
                            }

                            override fun onSkippedVideo() {
                                Log.e(TAG, "onSkippedVideo: ")
                            }
                        })
                    }

                    override fun onFullScreenVideoCached() {
                        Log.e(TAG, "onFullScreenVideoCached: ")
                    }
                })

        }

        fun loadtableplaqueAd(activity: Activity, postIdChuanShanJia: String,width:Int,height:Int) {
            val adSlot = AdSlot.Builder()
                .setCodeId(postIdChuanShanJia) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(
                    width.toFloat(),
                    height.toFloat()
                ) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(width, height) //这个参数设置即可，不影响个性化模板广告的size
                .build()

            //加载广告
            val ttAdNative = TTAdSdk.getAdManager().createAdNative(activity);


            //step5:请求广告，对请求回调的广告作渲染处理
            ttAdNative.loadInteractionExpressAd(adSlot, object : NativeExpressAdListener {
                override fun onError(code: Int, message: String) {
                    //TToast.show(InteractionExpressActivity.this, "load error : " + code + ", " + message);
                    Log.e("插屏广告请求错误", message)
                }

                override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>) {
                    if (ads == null || ads.size == 0) {
                        return
                    }
                    bindAdListener(ads[0], activity)
                    //startTime = System.currentTimeMillis();
                    ads[0].render()
                }
            })


        }


        // private long startTime = 0;
        private var mHasShowDownloadActive1 = false
        private fun bindAdListener(ad: TTNativeExpressAd, activity: Activity) {
            ad.setExpressInteractionListener(object : TTNativeExpressAd.AdInteractionListener {
                override fun onAdDismiss() {
                    //TToast.show(mContext, "广告关闭");
                }

                override fun onAdClicked(view: View?, type: Int) {
                    //TToast.show(mContext, "广告被点击");
                }

                override fun onAdShow(view: View?, type: Int) {
                    //TToast.show(mContext, "广告展示");
                    Log.e("插屏广告显示", "onAdShow")
                }

                override fun onRenderFail(view: View?, msg: String?, code: Int) {
                    //Log.e("ExpressView","render fail:"+(System.currentTimeMillis() - startTime));
                    //TToast.show(mContext, msg+" code:"+code);
                }

                override fun onRenderSuccess(
                    view: View?,
                    width: Float,
                    height: Float
                ) {
                    //Log.e("ExpressView","render suc:"+(System.currentTimeMillis() - startTime));
                    //返回view的宽高 单位 dp
                    //TToast.show(mContext, "渲染成功");
                    Log.e("插屏广告渲染成功", "successful")
                    ad.showInteractionExpressAd(activity)
                }
            })
            if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                return
            }
            ad.setDownloadListener(object : TTAppDownloadListener {
                override fun onIdle() {
                    //TToast.show(InteractionExpressActivity.this, "点击开始下载", Toast.LENGTH_LONG);
                }

                override fun onDownloadActive(
                    totalBytes: Long,
                    currBytes: Long,
                    fileName: String,
                    appName: String
                ) {
                    if (!mHasShowDownloadActive1) {
                        mHasShowDownloadActive1 = true
                        //TToast.show(InteractionExpressActivity.this, "下载中，点击暂停", Toast.LENGTH_LONG);
                    }
                }

                override fun onDownloadPaused(
                    totalBytes: Long,
                    currBytes: Long,
                    fileName: String,
                    appName: String
                ) {
                    //TToast.show(InteractionExpressActivity.this, "下载暂停，点击继续", Toast.LENGTH_LONG);
                }

                override fun onDownloadFailed(
                    totalBytes: Long,
                    currBytes: Long,
                    fileName: String,
                    appName: String
                ) {
                    // TToast.show(InteractionExpressActivity.this, "下载失败，点击重新下载", Toast.LENGTH_LONG);
                }

                override fun onInstalled(fileName: String, appName: String) {
                    //TToast.show(InteractionExpressActivity.this, "安装完成，点击图片打开", Toast.LENGTH_LONG);
                }

                override fun onDownloadFinished(
                    totalBytes: Long,
                    fileName: String,
                    appName: String
                ) {
                    //TToast.show(InteractionExpressActivity.this, "点击安装", Toast.LENGTH_LONG);
                }
            })
        }


    }
}




