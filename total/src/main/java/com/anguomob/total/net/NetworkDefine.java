package com.anguomob.total.net;

/**
 * 网络相关的一些枚举定义
 * 
 * @author wl
 */
public class NetworkDefine {
	
	/**
	 * 网络状态定义
	 *0:未连接    1：wifi   2：2g/3g/4g
	 * @author wl
	 */
	public enum NetworkStatus {
		MC_NETWORK_STATUS_NONE(0),
		MC_NETWORK_STATUS_WIFI(1), 
		MC_NETWORK_STATUS_WWAN(2);
		
		int _value;
		NetworkStatus(int value){
			this._value = value;
		}
		public int value(){
			return _value;
		}
	}

}
