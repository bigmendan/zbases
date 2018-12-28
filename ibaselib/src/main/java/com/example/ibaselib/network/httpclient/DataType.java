package com.example.ibaselib.network.httpclient;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 服务器端响应的 返回值类型，
 * <p>
 * 包括 String,Xml,JsonObject,JsonArray
 * <p>
 * 但是就目前 大多数返回时局基本都是 jsonobject ,所以这个类先留着 ，有需要的时候在研究
 */

public class DataType {

    /*返回数据为String*/
    public static final int STRING = 1;
    /*返回数据为xml类型*/
    public static final int XML = 2;
    /*返回数据为json对象*/
    public static final int JSON_OBJECT = 3;
    /*返回数据为json数组*/
    public static final int JSON_ARRAY = 4;

    /**
     * 自定义一个播放器状态注解
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STRING, XML, JSON_OBJECT, JSON_ARRAY})
    public @interface Type {
    }

}
