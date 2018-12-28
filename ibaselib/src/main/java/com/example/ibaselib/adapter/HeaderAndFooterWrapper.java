package com.example.ibaselib.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * time   : 2018/7/26
 * author :  M
 * des    :      使用装饰着模式 添加头尾布局
 * 使用 方法  ：
 *                  RecyclerView .setLayoutManager(new LinearLayoutManager(context));
 *                  RecyclerView.Adapter adapter = new RecyclerView.Adapter(context);
                    HeaderAndFooterWrapper wrapper = new HeaderAndFooterWrapper(adapter);
                    recyclerView.setAdapter(wrapper);
                    wrapper.addHeaderView(view);
                    wrapper.addFooterView(view);
 */
public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 定义两个静态变量，主要作用为设置header和footer的viewType
    private static final int BASE_HEAD_ITEM_VIEW = 100;
    private static final int BASE_FOOTER_ITEM_VIEW = 200;


    private SparseArray<View> mHeaderViewSparseArray = new SparseArray<>();
    private SparseArray<View> mFooterViewSparseArray = new SparseArray<>();

    private RecyclerView.Adapter mInnerAdapter;

    /**
     * 装饰者模式的装饰者必定持有被装饰者的引用，装饰者实例化的时候通过构造方法将该引用传递过来
     *
     * @param adapter：被装饰者，被装饰者的引用
     */
    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 根据viewType返回相应的viewHolder

        // 这里的viewType对于header来说就是mHeaderViewSparseArray的键元素
        // 对于footer来说就是mFooterViewSparseArray的键元素

        if (mHeaderViewSparseArray.get(viewType) != null) {  // 创建view，将view与viewHolder绑定，返回viewHolder
            // 返回header的viewHolder
            // 根据viewType拿到headerView
            View headerView = mHeaderViewSparseArray.get(viewType);

            return new HeaderViewHodler(headerView);
        } else if (mFooterViewSparseArray.get(viewType) != null) {
            // 返回footer的viewHolder
            View footerView = mFooterViewSparseArray.get(viewType);
            return new FooterViewHolder(footerView);
        }
        return mInnerAdapter.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) return;
        if (isFooterView(position)) return;
        mInnerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    /**
     * 因为增加了header和footer，所以需要重写该方法
     * 这里的position是该类中的getItemCount()返回的item数目
     *
     * @param position
     * @return 该方法需要给每个position返回一个itemViewType
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {  // 如果该position上的view为header，那么返回一个独特的itemViewType
            // 这里以headerView的键元素作为itemViewType
            return mHeaderViewSparseArray.keyAt(position);
        } else if (isFooterView(position)) {
            // 这里以footerView的键元素作为itemViewType
            // 这里返回的position与与mFooterViewSparseArray中的position有一定差距
            return mFooterViewSparseArray.keyAt(position - getRealItemCount() - getHeaderCount());
        }
        // 默认返回原来Adapter的itemViewType
        // 默认为0
        return mInnerAdapter.getItemViewType(position - getHeaderCount());
    }

    /**
     * 因为增加了header和footer，所以需要重写该方法
     *
     * @return header数量+原来item数量+footer数量
     */
    @Override
    public int getItemCount() {
        return getHeaderCount() + getRealItemCount() + getFooterCount();
    }

    private int getHeaderCount() {
        return mHeaderViewSparseArray.size();
    }

    private int getFooterCount() {
        return mFooterViewSparseArray.size();
    }

    /**
     * 被装饰者的item的数目
     */
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    /**
     * 添加headerView，将其添加到SparseArray中
     *
     * @param view
     */
    public void addHeaderView(View view) {
        mHeaderViewSparseArray.put(mHeaderViewSparseArray.size() + BASE_HEAD_ITEM_VIEW, view);

    }


    public void removeHeaderView(View view) {

    }

    /**
     * 添加footerView，将其添加到SparseArray中
     *
     * @param view
     */
    public void addFooterView(View view) {
        mFooterViewSparseArray.put(mFooterViewSparseArray.size() + BASE_FOOTER_ITEM_VIEW, view);
    }

    private boolean isHeaderView(int position) {
        // 根据position判断是不是header
        // header所占据的position应该小于header的数目
        if (position < getHeaderCount()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFooterView(int position) {
        // 根据position判断是不是footer
        // footer所占据的position大于等于header数目+real数目
        if (position >= getHeaderCount() + getRealItemCount()) return true;
        else return false;
    }

    class HeaderViewHodler extends RecyclerView.ViewHolder {

        public HeaderViewHodler(View itemView) {
            super(itemView);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 解决不适配GridLayoutManager
     * spanSize：表示一个item的跨度
     * spanCount：表示表示列数
     */
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);

        // 拿到recyclerView设置的LayoutManager
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            // 拿到SpanSizeLookup这个类
            //GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                // 主要思想，根据position设置每条item的跨度
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);  // 根据position拿到viewType
                    if (mHeaderViewSparseArray.get(viewType) != null) {  // 不为null，表明该位置的头布局
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterViewSparseArray.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        }
    }


    /**
     * 解决瀑布模型不适配的问题
     */
    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams1 = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                layoutParams1.setFullSpan(true);
            }
        }
    }
}
