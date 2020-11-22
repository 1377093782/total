package com.anguomob.total.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * 用于网络链接请求、网络状态判断等。
 * 
 * @author wl
 */
public class NetworkUtils {
	private static String TAG = "Network";

	/** 网络配置 包括超时、token、BaseURL */
	private static NetwokConfigInteface config;


	/**
	 * 设置网络连接的配置
	 *
	 * @param newConfig
	 *            新的配置
	 */
	public static void setMCNetworkConfig(NetwokConfigInteface newConfig) {
		config = newConfig;
	}

	/**
	 * 获取当前的网络连接配置。
	 * @return
	 */
	public static NetwokConfigInteface getMCNetworkConfig(){
		return config;
	}
	
	/**
	 * 获取网络连接状态
	 * 
	 * @param context
	 * @return 网络是否连接
	 */
	public static boolean checkedNetwork(Context context) {
		try {
			ConnectivityManager con = (ConnectivityManager) context
					.getSystemService(Activity.CONNECTIVITY_SERVICE);
			if(null != con.getActiveNetworkInfo())
				return con.getActiveNetworkInfo().isAvailable();
			return false;
//			boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//					.isConnectedOrConnecting();
//			NetworkInfo netinfo = con
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			boolean internet = false;
//			if (netinfo != null) {
//				internet = netinfo.isConnectedOrConnecting();
//			}
//			if (wifi | internet) {
//				return true;
//			} else {
//				return false;
//			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取当前的网络类型NetworkStatus
	 * 
	 * @param context
	 * @return 网络类型
	 */
	public static NetworkDefine.NetworkStatus currentNetwork(Context context) {
		NetworkDefine.NetworkStatus network = NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_NONE;
		try {
			ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			Log.e("wangli","conn:"+con.toString());
			NetworkInfo activeNetwork = con.getActiveNetworkInfo();
			Log.e("wangli","activeNetwork:"+activeNetwork.getTypeName());
			if (activeNetwork != null) { // connected to the internet
				if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
					// connected to wifi
//					Toast.makeText(context,"有可用的WIFI", Toast.LENGTH_SHORT).show();
					return network = NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_WIFI;
				} else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
					// connected to the mobile provider's data plan
//					Toast.makeText(context,"有可用的移动网络", Toast.LENGTH_SHORT).show();
					return network = NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_WWAN;
				}
			} else {
//				Toast.makeText(context,"没有可用的网络", Toast.LENGTH_SHORT).show();
				// not connected to the internet
			}

		} catch (Exception e) {
		}
		return network;
	}

	public static NetworkDefine.NetworkStatus getNetWorkStatus(Context context){
		switch (getNetWorkType(context)){
			case NETWORK_WIFI:
				return NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_WIFI;
			case NETWORK_4G:
			case NETWORK_3G:
			case NETWORK_2G:
				return NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_WWAN;
			case NETWORK_NO:
				return NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_NONE;
			default:
				return NetworkDefine.NetworkStatus.MC_NETWORK_STATUS_NONE;
		}
	}

	private static NetworkInfo getActiveNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	public static final int NETWORK_WIFI = 1;    // wifi network
	public static final int NETWORK_4G = 4;    // "4G" networks
	public static final int NETWORK_3G = 3;    // "3G" networks
	public static final int NETWORK_2G = 2;    // "2G" networks
	public static final int NETWORK_UNKNOWN = 5;    // unknown network
	public static final int NETWORK_NO = -1;   // no network

	private static final int NETWORK_TYPE_GSM = 16;
	private static final int NETWORK_TYPE_TD_SCDMA = 17;
	private static final int NETWORK_TYPE_IWLAN = 18;

	public static int getNetWorkType(Context context) {
		int netType = NETWORK_NO;
		NetworkInfo info = getActiveNetworkInfo(context);
		if (info != null && info.isAvailable()) {

			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				netType = NETWORK_WIFI;
			} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {

					case NETWORK_TYPE_GSM:
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN:
						netType = NETWORK_2G;
						break;

					case NETWORK_TYPE_TD_SCDMA:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						netType = NETWORK_3G;
						break;

					case NETWORK_TYPE_IWLAN:
					case TelephonyManager.NETWORK_TYPE_LTE:
						netType = NETWORK_4G;
						break;
					default:
						String subtypeName = info.getSubtypeName();
						if (subtypeName.equalsIgnoreCase("TD-SCDMA")
								|| subtypeName.equalsIgnoreCase("WCDMA")
								|| subtypeName.equalsIgnoreCase("CDMA2000")) {
							netType = NETWORK_3G;
						} else {
							netType = NETWORK_UNKNOWN;
						}
						break;
				}
			} else {
				netType = NETWORK_UNKNOWN;
			}
		}
		return netType;
	}

}
