package com.example.ibaselib.network.retrofitclient;


import com.example.ibaselib.baseapp.BaseApplication;
import com.example.ibaselib.utils.netutils.NetUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by EDZ on 2018/4/24.
 * <p>
 * 如果要使用Retrofit 封装类 ，直接把 这个文件夹复制在项目中
 * 未经二次封装不能作为 library 使用
 */

public class ApiRetrofit {
    private static String TAG = "=== ApiRetrofit";
    private static ApiRetrofit mApiRetrofit;
    private static Retrofit mRetrofit;
    private OkHttpClient mClient;
//    private ApiServices mApiService;
    private static String baseUrl = "";

    /**
     * 缓存配置 拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();

            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();

            if (!NetUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }

            Response originalResponse = chain.proceed(request);
            if (NetUtil.isNetworkAvailable()) {
                int maxAge = 0;             // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")     //
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };


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


    private ApiRetrofit() {
        // 获取缓存文件
        File httpCacheDirectory = new File(BaseApplication.getContext().getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;           // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        //        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        //        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//请求/响应行 + 头 + 体

        mClient = new OkHttpClient.Builder()
                //  .addInterceptor(mHeaderInterceptor)     //添加头部信息拦截器
                .addInterceptor(mLogInterceptor)         //添加log拦截器
                /* .cache(cache)*/
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                /* .addConverterFactory(ScalarsConverterFactory.create())*/    //  这是啥
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())       //支持RxJava
                .client(mClient)
                .build();

//        mApiService = mRetrofit.create(ApiServices.class);


    }

    // 本类中使用
//    private static ApiRetrofit getInstance() {
//        if (mApiRetrofit == null) {
//            synchronized (Object.class) {
//                if (mApiRetrofit == null) {
//                    mApiRetrofit = new ApiRetrofit();
//                }
//            }
//        }
//        return mApiRetrofit;
//    }
//    // 通过 单例模式 获取到 网络请求工具的位移对象
//    public static ApiServices getApiService(String baseUrls) {
//        baseUrl = baseUrls;
//        return ApiRetrofit.getInstance().mApiService;
//    }

    public static Retrofit getInstance(String baseUrls) {
        baseUrl = baseUrls;
        if (mRetrofit == null) {
            new ApiRetrofit();

        }
        return mRetrofit;

    }


}
