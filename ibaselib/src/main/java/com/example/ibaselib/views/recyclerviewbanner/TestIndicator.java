package com.example.ibaselib.views.recyclerviewbanner;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * time   :  2018/10/11
 * author :  Z
 * des    :
 */
public class TestIndicator extends RecyclerView.ItemDecoration {


    private int colorActive = 0xFFFFFFFF;
    private int colorInactive = /*0x66FFFFFF*/  0x0255255;                  // 透明色;


    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    /**
     * 指示器在底部占空间的高度
     */
    private final int mIndicatorHeight = (int) (DP * 16);

    /**
     * Indicator stroke width.  应该是 指示器 本身  高度;
     */
    private final float mIndicatorStrokeWidth = DP * 2;

    /**
     * 指示器的宽度;
     */
    private final float mIndicatorItemLength = DP * 16;
    /**
     * 指示器之间的间距;
     */
    private final float mIndicatorItemPadding = DP * 4;

    /**
     * 使用插值器添加过渡的动画
     */
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    public TestIndicator() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        // center horizontally, calculate width and subtract half from center
        // 水平居中
        float totalLength = mIndicatorItemLength * itemCount;

        float totalVerticalLength = parent.getHeight();     //
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;

        // 计算开始位置;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F;

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


        // 活跃的页面，指示器突出显示;
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
    }

    /**
     * 画 指示器;
     *
     * @param c
     * @param indicatorStartX
     * @param indicatorPosY
     * @param itemCount
     */
    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(colorInactive);

        //  指示器长度 + 间距;
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            //  在 每个item 上面画线
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY, int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);

        // 指示器宽度 + padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        if (progress == 0F) {
            // 没有滑动 , 一个正常的指示器;
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            c.drawLine(highlightStart, indicatorPosY, highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

        } else {

            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            // 计算部分突出
            float partialLength = mIndicatorItemLength * progress;

            // 画出切断突出
            c.drawLine(highlightStart + partialLength, indicatorPosY, highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);

            // 画出突出重叠的下一个项目
            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth;
                c.drawLine(highlightStart, indicatorPosY, highlightStart + partialLength, indicatorPosY, mPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }


}
