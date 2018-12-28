package com.example.ibaselib.network.httpclient;

/**
 * 作为封装后 网络请求工具返回 的回调
 * <p>
 *  至 于为什么不使用 interface, 我也不知道 ，毕竟代码是我抄的，
 *  原作者是说在Retrofit 中接口泛型会被擦掉，我就不明白了
 */

public class OnResultListener<T> {

    /**
     * 请求成功的情况
     *
     * @param result 需要解析的解析类
     */
    public void onSuccess(T result) {
    }

    /**
     * 响应成功，但是出错的情况
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public void onError(int code, String message) {
    }

    /**
     * 请求失败的情况
     *
     * @param message 失败信息
     */
    public void onFailure(String message) {
    }
}
