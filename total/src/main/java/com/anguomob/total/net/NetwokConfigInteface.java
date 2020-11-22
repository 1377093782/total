/***************************************************************
 * Created by wxm on 2014-1-8.
 * Modified by
 * Copyright (c) 2014年 Beijing Mooc Technology Center ltd. All rights reserved.
 ***************************************************************/

package com.anguomob.total.net;

import android.content.Context;

/**
 *网络连接的配置，包括基URL、超时、Token等。
 * @author wl
 */
public interface NetwokConfigInteface {
	public String getHttpServiceBaseUrl();
	public int getHttpConnectTimeOut();
	public String getToken(Context context, String method, String token);
}
