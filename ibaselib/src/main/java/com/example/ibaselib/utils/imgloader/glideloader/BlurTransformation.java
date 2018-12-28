package com.example.ibaselib.utils.imgloader.glideloader;

import android.graphics.Bitmap;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.example.ibaselib.utils.bitmaputils.BitmapUtil;

import java.security.MessageDigest;


/**
 * 高斯模糊
 *
 * 对 Bitmap 自定义   现在是要做
 * 必须继承 BitmapTransformation
 */

public class BlurTransformation extends BitmapTransformation {
    private static final String ID = BlurTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(Key.CHARSET);

    // 默认圆角吗
    private int defaultRadius = 15;

    public BlurTransformation(@IntRange(from = 0) int radius) {
        defaultRadius = radius;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);

    }

    /**
     * 调用高斯模糊算法
     */
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return BitmapUtil.fastBlur(toTransform, defaultRadius);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BlurTransformation;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}
