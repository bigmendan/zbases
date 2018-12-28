package com.example.ibaselib.utils.imgloader.loaderbase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;


/**
 * 图片框架 实现接口
 */

public interface IImageLoader {
    void showImage(@NonNull ImageLoaderOptions options);

    void hideImage(@NonNull View view, int visible);

    void cleanMemory(Context context);

    void pause(Context context);

    void resume(Context context);

    void download(ImageLoaderOptions options);


}
