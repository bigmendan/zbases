package com.example.ibaselib.views.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.ibaselib.R;


/**
 * 常规使用的 PopupWindow
 */
public class TestPopupWindow extends PopupWindow {


    public TestPopupWindow(Context context, int popLayout) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(popLayout, null, false);
        setContentView(contentView);
    }
}
