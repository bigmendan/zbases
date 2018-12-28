package com.example.ibaselib.views.marquee;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/**
 * time   :  2018/9/14
 * author :  Z
 * des    :              竖直方向的跑马灯
 */
public abstract class MarqeeModel<T> extends MarqueeFactory<View, T> {

    private static String TAG = "== 跑马灯 = ";
    private LayoutInflater inflater;
    private Context context;

    public MarqeeModel(Context mContext) {
        super(mContext);
        this.context = mContext;
        inflater = LayoutInflater.from(mContext);

    }

    /**
     * 设置跑马灯布局 并设置数据
     *
     * @param data
     * @return
     */
    @Override
    protected abstract View generateMarqueeItemView(T data);


}
