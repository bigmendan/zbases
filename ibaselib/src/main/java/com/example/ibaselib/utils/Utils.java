package com.example.ibaselib.utils;

/**
 * 全局使用 的 工具类 ;
 */
public class Utils {

    private static long lastClickTime;
    private static long clickDuration = 1000;

    // 判断是否在一秒内 多次点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < clickDuration) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
