package com.example.ibaselib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by EDZ on 2018/4/16.
 * 用于ScrollView 中嵌套ListView
 */

public class ForScrollListView extends ListView {
    public ForScrollListView(Context context) {
        super(context);
    }

    public ForScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
