package com.anguomob.total.utils.click;

/**
 *
 * 快速点击工具，防止暴力点击
 */
public class FastClickUtils {
    // 两次点击间隔默认值
    private static final long TWICE_INTERVAL = 500L;
    private static long sLastClickTime = 0;

    /**
     * 是否有效点击，2次点击默认间隔500毫秒
     *
     * @return true 有效，false 无效
     */
    public static boolean validClick() {
        long currentTime = System.currentTimeMillis();
        // 大于两次点击的间隔，返回 true 有效点击
        if ((currentTime - sLastClickTime) > TWICE_INTERVAL) {
            sLastClickTime = currentTime;
            return true;
        }
        return false;
    }

    /**
     * 时间间隔内是否有效的点击
     *
     * @param twiceInterval 两次点击间隔
     * @return true 有效，false 无效
     */
    public static boolean validClick(long twiceInterval) {
        long currentTime = System.currentTimeMillis();
        // 大于两次点击的间隔，返回 true 有效点击
        if ((currentTime - sLastClickTime) > twiceInterval) {
            sLastClickTime = currentTime;
            return true;
        }
        return false;
    }
}
