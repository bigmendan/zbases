package com.example.ibaselib.utils.imgloader.loaderbase;

import android.graphics.Bitmap;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;

/**
 * 图片框架的相关配置
 */

public class ImageLoaderOptions {
    private View viewContainer;                   // 图片容器
    private String url;                          // 图片地址
    private int resource;                        // 图片地址
    private int holderDrawable;                   // 设置占位图
    private ImageSize imageSize;                 //设置图片的大小
    private int errorDrawable;                   //是否展示加载错误的图片
    private boolean asGif = false;               //是否作为gif展示
    private boolean isCrossFade = true;         //是否渐变平滑的显示图片,默认为true
    private boolean isSkipMemoryCache = false; //是否跳过内存缓存

    private String fileName = "";               // 图片下载存储路径
    private ImageDownLoadCallBack imageDownLoadCallBack;            // 返回图片下载结果

    private DiskCacheStrategy mDiskCacheStrategy = DiskCacheStrategy.DEFAULT; //磁盘缓存策略
    private boolean blurImage = false;           //是否使用高斯模糊
    private LoaderResultCallBack loaderResultCallBack;   // 返回图片加载结果

    private int blurValue;                       // 高斯模糊参数，越大越模糊
    private int imageRadius = 0;
    private boolean isCircle = false;
    // 还有图片缩放......待开发


    private ImageLoaderOptions(Builder builder) {
        this.asGif = builder.asGif;
        this.errorDrawable = builder.errorDrawable;
        this.holderDrawable = builder.holderDrawable;
        this.imageSize = builder.mImageSize;
        this.isCrossFade = builder.isCrossFade;
        this.isSkipMemoryCache = builder.isSkipMemoryCache;
        this.mDiskCacheStrategy = builder.mDiskCacheStrategy;
        this.url = builder.url;
        this.resource = builder.resource;
        this.viewContainer = builder.mViewContainer;
        this.blurImage = builder.blurImage;
        this.loaderResultCallBack = builder.loaderResultCallBack;

        this.imageDownLoadCallBack = builder.imageDownLoadCallBack;
        this.isCircle = builder.isCircle;
        this.blurValue = builder.blurValue;
        this.imageRadius = builder.imageRadius;
        this.fileName = builder.fileName;
    }

    public LoaderResultCallBack getLoaderResultCallBack() {
        return loaderResultCallBack;
    }

    /**
     * 下载图片的监听
     */
    public ImageDownLoadCallBack getImageDownLoadCallBack() {
        return imageDownLoadCallBack;
    }


    public int getBlurValue() {
        return blurValue;
    }

    public boolean needImageRadius() {
        return imageRadius > 0;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public int getResource() {
        return resource;
    }

    public boolean isBlurImage() {
        return blurImage;
    }

    public View getViewContainer() {
        return viewContainer;
    }

    public String getUrl() {
        return url;
    }

    public boolean isCircle() {
        return isCircle;
    }

    //  占位
    public int getHolderDrawable() {
        return holderDrawable;
    }


    // 图片下载 存储位置
    public String getFileName() {
        return fileName;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    // 加载错误
    public int getErrorDrawable() {
        return errorDrawable;
    }


    // 显示Gif
    public boolean isAsGif() {
        return asGif;
    }


    // 是否渐变平滑显示图片
    public boolean isCrossFade() {
        return isCrossFade;
    }


    public boolean isSkipMemoryCache() {
        return isSkipMemoryCache;
    }


    public DiskCacheStrategy getDiskCacheStrategy() {
        return mDiskCacheStrategy;
    }


    public final static class Builder {

        private int holderDrawable = -1;                // 设置占位图
        private View mViewContainer;                // 图片容器
        private String url;                         // 图片地址
        private int resource = -1;                  // 图片地址
        private ImageSize mImageSize;                //设置图片的大小
        private int errorDrawable = -1;             //是否展示加载错误的图片
        private boolean asGif = false;               //是否作为gif展示
        private boolean isCrossFade = false;        //是否渐变平滑的显示图片
        private boolean isSkipMemoryCache = false; //是否跳过内存缓存
        private boolean blurImage = false;          //是否使用高斯模糊
        private DiskCacheStrategy mDiskCacheStrategy = DiskCacheStrategy.DEFAULT;   //磁盘缓存策略
        private LoaderResultCallBack loaderResultCallBack;                           // 返回图片加载结果

        private ImageDownLoadCallBack imageDownLoadCallBack;            // 返回图片下载结果

        private int blurValue = 15;                                              // 高斯模糊参数，越大越模糊
        private int imageRadius = 0;
        private boolean isCircle = false;
        private String fileName = "";


        /**
         * 通过 url 加载图片
         */
        public Builder(@NonNull View v, @NonNull String url) {
            this.url = url;
            this.mViewContainer = v;
        }

        /**
         * 通过本地资源加载图片
         */
        public Builder(@NonNull View v, @NonNull int resource) {
            this.resource = resource;
            this.mViewContainer = v;
        }

        public Builder placeholder(@DrawableRes int holderDrawable) {
            this.holderDrawable = holderDrawable;
            return this;
        }

        /**
         * 加载图片渐变平滑显示效果
         */
        public Builder isCrossFade(boolean isCrossFade) {
            this.isCrossFade = isCrossFade;
            return this;
        }

        /**
         * 是否使用高斯模糊
         */
        public Builder blurImage(boolean blurImage) {
            this.blurImage = blurImage;
            return this;
        }

        /**
         * 是否 为圆
         */
        public Builder isCircle() {
            this.isCircle = true;
            return this;
        }

        /**
         * 设置图片圆角   单位是 PX
         * 上下文 对象 通过 ImageView .getContext().getApplication();
         */
        public Builder imageRadiusPx(@Dimension(unit = Dimension.PX) int rdius) {
            this.imageRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, rdius, mViewContainer.getContext().getApplicationContext().getResources().getDisplayMetrics());

            return this;
        }

        /**
         * 设置图片圆角   单位是 DP
         */
        public Builder imageRadiusDp(@Dimension(unit = Dimension.DP) int rdius) {
            this.imageRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rdius, mViewContainer.getContext().getApplicationContext().getResources().getDisplayMetrics());
            return this;
        }

        /**
         * 设置高斯模糊参数 ， 数值越大，越模糊
         */
        public Builder blurValue(@IntRange(from = 0) int blurvalue) {
            this.blurValue = blurvalue;
            return this;
        }

        /**
         * 是否跳过内存缓存
         */
        public Builder isSkipMemoryCache(boolean isSkipMemoryCache) {
            this.isSkipMemoryCache = isSkipMemoryCache;
            return this;

        }

        /**
         * 设置图片大小
         */
        public Builder override(int width, int height) {
            this.mImageSize = new ImageSize(width, height);
            return this;
        }

        /**
         * 是否显示 Gif
         */
        public Builder asGif(boolean asGif) {
            this.asGif = asGif;
            return this;
        }

        /**
         * 显示错误图片 ， 直接加载 错误图片资源
         */
        public Builder error(@DrawableRes int errorDrawable) {
            this.errorDrawable = errorDrawable;
            return this;
        }


        /**
         * 显示错误图片 ，调用接口形式
         */
        public Builder error(LoaderResultCallBack resultCallBack) {
            this.loaderResultCallBack = resultCallBack;
            return this;
        }

        public Builder downloadError(ImageDownLoadCallBack downLoadCallBack) {
            this.imageDownLoadCallBack = downLoadCallBack;
            return this;
        }

        /**
         * 磁盘缓存策略
         */
        public Builder diskCacheStrategy(DiskCacheStrategy mDiskCacheStrategy) {
            this.mDiskCacheStrategy = mDiskCacheStrategy;
            return this;

        }

        /**
         * 返回
         */
        public ImageLoaderOptions build() {
            return new ImageLoaderOptions(this);
        }


    }

    //对应重写图片size
    public final static class ImageSize {
        private int width = 0;
        private int height = 0;

        public ImageSize(int width, int heigh) {
            this.width = width;
            this.height = heigh;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    //对应磁盘缓存策略
    public enum DiskCacheStrategy {
        All, NONE, SOURCE, RESULT, DEFAULT
    }

    /**
     * 响应图片请求返回结果的接口
     */
    public interface LoaderResultCallBack {
        void onSuccess();

        void onFail();
    }


    /**
     * 响应 图片下载时 返回结果的接口
     */

    public interface ImageDownLoadCallBack {
        void onDownLoadSuccess(Bitmap bitmap);

        void onDownLoadFailed();
    }


}
