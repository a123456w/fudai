package com.qlzx.mylibrary.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 87901 on 2016/10/29.
 */

public class EventBusUtil {
    public static void post(Object object){
        EventBus.getDefault().post(object);
    }
    public static void register(Object object){
        if (!EventBus.getDefault().isRegistered(object)){
            EventBus.getDefault().register(object);
        }
    }
    public static void unregister(Object object){
        if (EventBus.getDefault().isRegistered(object)){
            EventBus.getDefault().unregister(object);
        }
    }
}
