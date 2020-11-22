package com.anguomob.total.utils;

import android.content.Intent;

import com.anguomob.total.init.AnguoUtils;

public class IntentUtils {
    public static void onKeyHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_HOME);
        AnguoUtils.getContext().startActivity(intent);
    }

    public static void fromA2B(Class cls) {
        Intent intent = new Intent(AnguoUtils.getContext(), cls);
        AnguoUtils.getContext().startActivity(intent);
    }

    public static void fromA2BAddNewtask(Class cls) {
        Intent intent = new Intent(AnguoUtils.getContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AnguoUtils.getContext().startActivity(intent);
    }
}
