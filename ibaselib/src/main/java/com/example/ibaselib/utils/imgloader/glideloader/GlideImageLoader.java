package com.example.ibaselib.utils.imgloader.glideloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.ibaselib.utils.fileutils.FileUtil;
import com.example.ibaselib.utils.imgloader.loaderbase.IImageLoader;
import com.example.ibaselib.utils.imgloader.loaderbase.ImageLoaderOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by 大麻子
 * 将三方图片框架 封装， 为了方便以后更新  或者换框架；
 * <p>
 * 现在这个工具类 进行二次封装 的 Glide
 * <p>
 * 二次封装的原因 ：
 * 1，避免以后换框架的时候需要改的地方太多。如果封装了只需要改封装的方法而不会影响到所有的代码。
 * 2，入口统一，所有图片加载都在这一个地方管理，一目了然，即使有什么改动我也只需要改这一个类就可以了。
 * 3，虽然现在的第三方库已经非常好用，但是如果我们看到第三方库就拿来用的话，很可能在第三方库无法满足业务需求或者停止维护的时候，发现替换库，工作量巨大。
 * <p>
 * 这就是不封装在切库时面临的窘境！
 * <p>
 * 用法 ：
 * ImageLoaderOptions op=new ImageLoaderOptions.Builder(img1,url).imageRadiusDp(12).build();
 * GlideImageLoader.getInstance().showImage(op);
 */
public class GlideImageLoader implements IImageLoader {

    private static String TAG = "== GlideImageLoader";
    private static final GlideImageLoader INSTANCE = new GlideImageLoader();
    private File currentFile = null;

    public GlideImageLoader() {
    }

    public static GlideImageLoader getInstance() {
        return INSTANCE;
    }

    @Override
    public void showImage(@NonNull final ImageLoaderOptions options) {
        RequestOptions requestOptions = new RequestOptions();

        // 占位图片
        if (options.getHolderDrawable() != -1) {
            requestOptions.placeholder(options.getHolderDrawable());
        }
        //  加载错误时显示图片
        if (options.getErrorDrawable() != -1) {
            requestOptions.fallback(options.getErrorDrawable());
        }

        //   缓存策略
        if (options.getDiskCacheStrategy() != ImageLoaderOptions.DiskCacheStrategy.DEFAULT) {
            if (ImageLoaderOptions.DiskCacheStrategy.NONE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.All == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            } else if (ImageLoaderOptions.DiskCacheStrategy.SOURCE == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            } else if (ImageLoaderOptions.DiskCacheStrategy.RESULT == options.getDiskCacheStrategy()) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            }

        }
        if (options.isSkipMemoryCache()) {
            requestOptions.skipMemoryCache(true);
        }
        if (options.getImageSize() != null) {
            requestOptions.override(options.getImageSize().getWidth(), options.getImageSize().getHeight());
        }

        List<Transformation> list = new ArrayList<Transformation>();
        // 高斯模糊
        if (options.isBlurImage()) {
            list.add(new BlurTransformation(options.getBlurValue()));
        }
        // 圆角
        if (options.needImageRadius()) {
            list.add(new RoundedCorners(options.getImageRadius()));
        }

        //  是否为圆
        if (options.isCircle()) {
            list.add(new CircleTransformation());

        }
        if (list.size() > 0) {
            Transformation[] transformations = list.toArray(new Transformation[list.size()]);
            requestOptions.transforms(transformations);

        }


        RequestBuilder builder = getRequestBuilder(options);

        // 这里为啥还要 ...
        builder.listener(new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {

                if (options.getLoaderResultCallBack() != null) {
                    options.getLoaderResultCallBack().onFail();
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                if (options.getLoaderResultCallBack() != null) {
                    options.getLoaderResultCallBack().onSuccess();
                }
                return false;
            }
        });

        builder.apply(requestOptions).into((ImageView) options.getViewContainer());
    }

    @Override
    public void hideImage(@NonNull View view, int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void cleanMemory(Context context) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context).clearMemory();

        }
    }

    @Override
    public void pause(Context context) {
        Glide.with(context).pauseRequests();
    }

    @Override
    public void resume(Context context) {
        Glide.with(context).resumeRequests();

    }

    /**
     * 下载图片
     */
    @Override
    public void download(final ImageLoaderOptions options) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                Bitmap bitmap = null;

                Context context = options.getViewContainer().getContext().getApplicationContext();
                String url = options.getUrl();
                String fileName = options.getFileName();
                ImageLoaderOptions.ImageDownLoadCallBack callBack = options.getImageDownLoadCallBack();

                try {

                    // 通过Glide 获取到Bitmap  对象
                    bitmap = Glide.with(context)
                            .asBitmap()
                            .load(url)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();

                    if (bitmap != null) {

                        //TODO  保存图片
                        FileUtil fileUtil = new FileUtil(context);
                        currentFile = fileUtil.saveImageToGallery(bitmap, fileName, false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bitmap != null && currentFile.exists()) {
                        // 怎么在这里把监听事件暴露
                        if (callBack != null) {
                            callBack.onDownLoadSuccess(bitmap);
                        }

                    } else {
                        if (callBack != null) {
                            callBack.onDownLoadFailed();

                        }
                    }
                }
            }
        }.start();

    }

    private RequestManager getRequestManager(View view) {
        return Glide.with(view);

    }

    private RequestBuilder getRequestBuilder(ImageLoaderOptions options) {
        RequestBuilder builder = null;
        if (options.isAsGif()) {
            builder = getRequestManager(options.getViewContainer()).asGif();
        } else {
            builder = getRequestManager(options.getViewContainer()).asBitmap();
        }

        if (!TextUtils.isEmpty(options.getUrl())) {
            builder.load(options.getUrl());
        } else {
            builder.load(options.getResource());
        }
        return builder;

    }


}
