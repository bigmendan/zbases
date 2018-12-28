package com.example.ibaselib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ibaselib.R;

import java.util.List;

/**
 * Created by 麻子舅舅 on 2018/6/15.
 * 极简 可实现自动轮播的 Banner;
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;

    //网络图片地址
    private List<String> imageUrls;

    private int[] localImgs;

    //指示点的容器
    private LinearLayout pointLayout;

    // Banner 描述 的容器
    private LinearLayout desLayout;

    //当前页面位置
    private int currentItem;

    //自动播放时间
    private int autoPlayTime = 0;
    //是否自动播放
    private boolean isAutoPlay;

    // 添加一个图片来源的标记
    private int TYPE = 0;      //  网络图片 = 1 ;  本地图片 = 0 ;

    //是否是一张图片
    private boolean isOneImage;

    private boolean showDes;

    //监听事件
    private OnBannerItemClick onBannerItemClick;

    //这里利用handler实现循环播放
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            currentItem++;
            int length = 0;
            if (TYPE == 1) {
                length = imageUrls.size();
            } else {
                length = localImgs.length;
            }
            currentItem = currentItem % (length + 2);
            viewPager.setCurrentItem(currentItem);
            handler.sendEmptyMessageDelayed(0, autoPlayTime);
            return false;
        }
    });


    public BannerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);
        //默认自动播放
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerView_isAutoPlay, true);

        autoPlayTime = typedArray.getInteger(R.styleable.BannerView_duration, 2000);

        typedArray.recycle();

        viewPager = new ViewPager(getContext());
        pointLayout = new LinearLayout(getContext());
        desLayout = new LinearLayout(getContext());
        //添加监听事件
        viewPager.addOnPageChangeListener(this);
        //利用布局属性将指示器容器放置底部并居中
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.bottomMargin = 60;   //  可以修改 指示器的位置

        addView(viewPager);
        addView(pointLayout, params);
        addView(desLayout, params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (!isOneImage) {
            switchToPoint(toRealPosition(position));
        }


    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //根据滑动松开后的状态，去判断当前的current 并跳转到指定current
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            int current = viewPager.getCurrentItem();
            int lastReal = viewPager.getAdapter().getCount() - 2;
            if (current == 0) {
                viewPager.setCurrentItem(lastReal, false);
            } else if (current == lastReal + 1) {
                viewPager.setCurrentItem(1, false);
            }
        }
    }

    //配置viewpager适配器
    private class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if (TYPE == 1) {
                return imageUrls.size() + 2;
            } else {
                return localImgs.length + 2;
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClick != null) {
                        onBannerItemClick.onItemClick(toRealPosition(position));
                    }
                }
            });
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (TYPE == 1) {  // type =1   网络图片
                Glide.with(getContext()).load(imageUrls.get(toRealPosition(position))).into(imageView);
            } else {
                Glide.with(getContext()).load(localImgs[toRealPosition(position)]).into(imageView);
            }

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 添加网络图片
     */
    public void setImageUrls(List<String> imageUrls) {
        TYPE = 1;
        this.imageUrls = imageUrls;
        if (imageUrls.size() <= 1) {
            isOneImage = true;
        } else {
            isOneImage = false;
        }


        initViewPager();
    }

    /**
     * 添加 本地图片
     *
     * @param localUrls
     */
    public void setImageLocal(int[] localUrls) {
        this.localImgs = localUrls;
        TYPE = 0;

        if (localUrls.length <= 1) {
            isOneImage = true;
        } else {
            isOneImage = false;
        }

        initViewPager();
    }

    //加载viewPager
    private void initViewPager() {
        if (!isOneImage) {
            //添加指示点
            addPoints();
        }

//        if (showDes) {
//            addDes();
//        }


        BannerAdapter adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
        //默认当前图片
        viewPager.setCurrentItem(1);
        //判断是否自动播放和是否是一张图片的情况
        if (isAutoPlay && !isOneImage) {
            handler.sendEmptyMessageDelayed(0, autoPlayTime);
        }
    }


    // 设置是否显示 描述
    public void setShowDes(boolean showDes) {
        this.showDes = showDes;
    }


    //添加指示点
    private void addPoints() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(30, 30);
        lp.setMargins(10, 10, 10, 10);
        ImageView imageView;
        int length;
        if (TYPE == 1) {
            length = imageUrls.size();
        } else {
            length = localImgs.length;
        }
        for (int i = 0; i < length; i++) {
            imageView = new ImageView(getContext());

            imageView.setLayoutParams(lp);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.selector_indictor));
            pointLayout.addView(imageView);
        }
        switchToPoint(0);
    }


    // 隐藏指示点
    public void hidePoints() {
        pointLayout.setVisibility(GONE);
    }

    //切换指示器
    private void switchToPoint(int currentPoint) {
        for (int i = 0; i < pointLayout.getChildCount(); i++) {
            pointLayout.getChildAt(i).setEnabled(false);
        }
        pointLayout.getChildAt(currentPoint).setEnabled(true);
    }


    // 如何设置 Banner回复初始值
    public void reset() {

        // 释放图片资源
        if (imageUrls != null) {
            for (int i = 0; i < imageUrls.size(); i++) {
                imageUrls.remove(imageUrls.get(i));
            }
            // 释放小点
            pointLayout.removeAllViews();
        }

        // 自动播放时间
        autoPlayTime = 2000;

    }

    /**
     * 返回真实的 位置
     *
     * @param position
     * @return
     */
    private int toRealPosition(int position) {
        int realPosition;

        int length = 0;
        if (TYPE == 1) {
            length = imageUrls.size();
        } else {
            length = localImgs.length;
        }
        if (length > 0) {
            realPosition = (position - 1) % length;
            if (realPosition < 0)
                realPosition += length;
        } else {
            realPosition = 0;
        }
        return realPosition;
    }

    /**
     * 设置是否自动轮播
     *
     * @param autoPlay
     */
    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    /**
     * 设置轮播时间
     *
     * @param i
     */
    public void setAutoPlayTime(Integer i) {
        autoPlayTime = i;
    }

    /**
     * 轮播图点击的监听事件
     *
     * @param onBannerItemClick
     */
    public void setOnBannerItemClick(OnBannerItemClick onBannerItemClick) {
        this.onBannerItemClick = onBannerItemClick;
    }

    //添加监听事件回调
    public interface OnBannerItemClick {
        void onItemClick(int position);
    }

}
