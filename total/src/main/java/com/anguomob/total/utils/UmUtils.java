package com.anguomob.total.utils;

import com.anguomob.total.init.AnguoUtils;
import com.umeng.analytics.AnalyticsConfig;

public class UmUtils {
    public static String getUmChannel(){
        return   AnalyticsConfig.getChannel(AnguoUtils.getContext());
    }


}
