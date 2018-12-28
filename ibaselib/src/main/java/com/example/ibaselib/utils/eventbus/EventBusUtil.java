package com.example.ibaselib.utils.eventbus;


import org.greenrobot.eventbus.EventBus;

/**
 * Created by EDZ on 2018/5/14.
 * 处理 EventBus 的工具类
 */

public class EventBusUtil {

    /**
     * 注册 EventBus
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 注销掉 EventBus 在 Activity 的 onDestroy() ，防止泄露；
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发送广播
     */
    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }


    /**
     * 发送粘性广播
     */
    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

    // 其他 .....

}
