package com.example.ibaselib.network.httpclient;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.ibaselib.R;
import com.example.ibaselib.baseapp.BaseApplication;
import com.example.ibaselib.utils.StringUtils;
import com.example.ibaselib.utils.ToastUtils;
import com.example.ibaselib.utils.netutils.NetUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * 封装一个 网络框架 ，可以作为base library 使用的那种
 * <p>
 * 当前 封装的是 Retrofit
 * <p>
 * <p>
 * 使用方法  ：
 * HttpClient client = new HttpClient.Builder()
 * .baseUrl(Constants.GAN_HUO_API)
 * .url("福利/" + size + "/" + page)
 * .bodyType(DataType.JSON_OBJECT, GirlsParser.class)
 * .build();
 * <p>
 * client.get(new OnResultListener<T>() {     T 是返回数据的类
 *
 * @Override public void onSuccess(T result) {
 * callback.onGirlsLoaded(result);
 * }
 * @Override public void onError(int code, String message) {
 * callback.onDataNotAvailable();
 * }
 * @Override public void onFailure(String message) {
 * callback.onDataNotAvailable();
 * }
 */

public class HttpClient {

    /*用户设置的BASE_URL*/
    private static String BASE_URL = "";
    /*本地使用的baseUrl*/
    private String baseUrl = "";
    private static OkHttpClient httpClient;
    private Builder mBuilder;
    private Retrofit retrofit;
    private Call<ResponseBody> mCall;
    private static final Map<String, Call> CALL_MAP = new HashMap<>();

    /**
     * 获取HttpClient的单例
     *
     * @return HttpClient的唯一对象
     */
    private static HttpClient getIns() {
        return HttpClientHolder.sInstance;
    }

    /**
     * 单例模式中的静态内部类写法
     */
    private static class HttpClientHolder {
        private static final HttpClient sInstance = new HttpClient();
    }

    public HttpClient() {
        //  配置 OkHttpClient
        httpClient = new OkHttpClient.Builder()
                //  .addInterceptor(mHeaderInterceptor)     //添加头部信息拦截器
                .addInterceptor(mLogInterceptor)         //添加log拦截器
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public void setBuilder(Builder mBuilder) {
        this.mBuilder = mBuilder;
    }

    public Builder getBuilder() {
        return mBuilder;
    }


    /**
     * 获取的Retrofit的实例，
     * 引起Retrofit变化的因素只有静态变量BASE_URL的改变。
     */
    private void getRetrofit() {
        if (!BASE_URL.equals(baseUrl) || retrofit == null) {
            baseUrl = BASE_URL;
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(httpClient)
                    .build();
        }
    }

    public void post(final OnResultListener onResultListener) {
        Builder builder = mBuilder;
        mCall = retrofit.create(ApiService.class).executePost(builder.url, builder.params);
        putCall(builder, mCall);
        request(builder, onResultListener);
    }


    public void get(final OnResultListener onResultListener) {
        Builder builder = mBuilder;
        if (!builder.params.isEmpty()) {
            String value = "";
            for (Map.Entry<String, String> entry : builder.params.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                String span = value.equals("") ? "" : "&";
                String part = StringUtils.buffer(span, mapKey, "=", mapValue);
                value = StringUtils.buffer(value, part);
            }
            builder.url(StringUtils.buffer(builder.url, "?", value));
        }
        mCall = retrofit.create(ApiService.class).executeGet(builder.url);
        putCall(builder, mCall);
        request(builder, onResultListener);
    }


    private void request(final Builder builder, final OnResultListener onResultListener) {
        if (!NetUtil.isNetworkAvailable()) {
            ToastUtils.showLongToastSafe(R.string.current_internet_invalid);
            onResultListener.onFailure(BaseApplication.getContext().getResources().getString(R.string.current_internet_invalid));
            return;
        }
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (200 == response.code()) {
                    try {
                        String result = response.body().string();
//                        parseData(result, builder.clazz, builder.bodyType, onResultListener);
                    } catch (IOException | IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
                if (!response.isSuccessful() || 200 != response.code()) {
                    onResultListener.onError(response.code(), response.message());
                }
                if (null != builder.tag) {
                    removeCall(builder.url);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                onResultListener.onFailure(t.getMessage());
                if (null != builder.tag) {
                    removeCall(builder.url);
                }
            }

        });
    }


    /**
     * 添加某个请求
     */
    private synchronized void putCall(Builder builder, Call call) {
        if (builder.tag == null)
            return;
        synchronized (CALL_MAP) {
            CALL_MAP.put(builder.tag.toString() + builder.url, call);
        }
    }


    /**
     * 取消某个界面都所有请求，或者是取消某个tag的所有请求;
     * 如果要取消某个tag单独请求，tag需要传入tag+url
     *
     * @param tag 请求标签
     */
    public synchronized void cancel(Object tag) {
        if (tag == null)
            return;
        List<String> list = new ArrayList<>();
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.startsWith(tag.toString())) {
                    CALL_MAP.get(key).cancel();
                    list.add(key);
                }
            }
        }
        for (String s : list) {
            removeCall(s);
        }

    }

    /**
     * 移除某个请求
     *
     * @param url 添加的url
     */
    private synchronized void removeCall(String url) {
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.contains(url)) {
                    url = key;
                    break;
                }
            }
            CALL_MAP.remove(url);
        }
    }


    /**
     * 链式结构 配置参数
     */
    public static final class Builder {
        private String builderBaseUrl = "";
        private String url;
        private Object tag;
        private Map<String, String> params = new HashMap<>();

        /*返回数据的类型,默认是string类型*/
        @DataType.Type
        private int bodyType = DataType.STRING;
        /*解析类*/
        private Class clazz;

        public Builder() {
        }

        /**
         * 请求地址的baseUrl，最后会被赋值给HttpClient的静态变量BASE_URL；
         *
         * @param baseUrl 请求地址的baseUrl
         */
        public Builder baseUrl(String baseUrl) {
            this.builderBaseUrl = baseUrl;
            return this;
        }

        /**
         * 除baseUrl以外的部分，例如："mobile/login"
         *
         * @param url path路径
         */
        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 给当前网络请求添加标签，用于取消这个网络请求
         *
         * @param tag 标签
         */
        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 添加请求参数
         *
         * @param key   键
         * @param value 值
         */
        public Builder params(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * 响应体类型设置,如果要响应体类型为STRING，请不要使用这个方法
         *
         * @param bodyType 响应体类型，分别:STRING，JSON_OBJECT,JSON_ARRAY,XML
         * @param clazz    指定的解析类
         * @param <T>      解析类
         */
        public <T> Builder bodyType(@DataType.Type int bodyType, @NonNull Class<T> clazz) {
            this.bodyType = bodyType;
            this.clazz = clazz;
            return this;
        }

        /**
         * 响应体类型设置,如果要响应体类型为STRING，请不要使用这个方法
         *
         * @param clazz 指定的解析类
         * @param <T>   解析类
         */
        public <T> Builder bodyType(@NonNull Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }


        public HttpClient build() {
            if (!TextUtils.isEmpty(builderBaseUrl)) {
                BASE_URL = builderBaseUrl;
            }
            HttpClient client = HttpClient.getIns();
            client.getRetrofit();
            client.setBuilder(this);
            return client;
        }
    }

    /**
     * 数据解析方法
     *
     * @param data             要解析的数据
     * @param clazz            解析类
     * @param bodyType         解析数据类型
     * @param onResultListener 回调方数据接口
     */
    @SuppressWarnings("unchecked")
    private void parseData(String data, Class clazz, @DataType.Type int bodyType, OnResultListener onResultListener) {
        switch (bodyType) {
            case DataType.STRING:
                onResultListener.onSuccess(data);
                break;
            case DataType.JSON_OBJECT:
                onResultListener.onSuccess(DataParseUtil.parseObject(data, clazz));
                break;
            case DataType.JSON_ARRAY:
                onResultListener.onSuccess(DataParseUtil.parseToArrayList(data, clazz));
                break;
            // Xml 要不就先不解析了吧；
//            case DataType.XML:
//                onResultListener.onSuccess(DataParseUtil.parseXml(data, clazz));
//                break;
            default:
                Log.e("HttpClient 网络封装框架", "噢噢噢噢");
                break;
        }
    }



    /*============================  对 OkHttpClient 的 一些配置 =================================*/
    /**
     * 请求访问 request 和 response 拦截器
     * <p>
     * 只是方便用 Log 显示
     */
    private Interceptor mLogInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //  获取到请求体
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            // 获取到响应体
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();

//            Log.e(TAG, "拦截器显示请求 = " + request.toString() + ",Response:" + content + ",时间 = " + duration);

            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    };


    /**
     * 增加头部信息的拦截器
     * <p>
     * 因为拦截器可以获取到 Request  和 Response  在这里可以人为的修改请求头
     * 具体怎么修改 我不知道  学会了再来补充
     */
    private Interceptor mHeaderInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.108 Safari/537.36 2345Explorer/8.0.0.13547");
            builder.addHeader("Cache-Control", "max-age=0");
            builder.addHeader("Upgrade-Insecure-Requests", "1");
            builder.addHeader("X-Requested-With", "XMLHttpRequest");
            builder.addHeader("Cookie", "uuid=\"w:f2e0e469165542f8a3960f67cb354026\"; __tasessionId=4p6q77g6q1479458262778; csrftoken=7de2dd812d513441f85cf8272f015ce5; tt_webid=36385357187");
            return chain.proceed(builder.build());
        }
    };
}
