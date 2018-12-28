package com.example.ibaselib.test;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.ibaselib.R;
import com.example.ibaselib.views.loadmorerecyclerview.LoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 这个 Activity 是要演示 LoadRecyclerView 的使用;
 */
public class DemoActivity extends AppCompatActivity {
// Adapter 没有变化 不影响;
//    private static String TAG = "== LoadMoreRecyActivity =";
//
//
//    @BindView(R.id.swipe)
//    SwipeRefreshLayout swipe;
//
//    @BindView(R.id.recyclerView)
//    LoadRecyclerView recyclerView;
//
//    private MoreAdapter adapter;
//
//    private int page = 0;
//    private int totalPage = 0;
//    private int pageSize = 0;
//
//    List<WenZhang.DataBean.DatasBean> dataList = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_load_more_recy);
//
//        ButterKnife.bind(this);
//
//        initRecyclerView();
//        swipe.setOnRefreshListener(this);
//
//        getWenZhang(true);
//    }
//
//
//    private void initRecyclerView() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        adapter = new MoreAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setFootViewText("加载中...", "加载完成");
//
//        recyclerView.setLoadingListener(new LoadRecyclerView.LoadingListener() {
//            @Override
//            public void onLoadMore() {
//                page++;
//                if (page > totalPage) {
//                    recyclerView.setNoMore(true);
//                } else {
//                    getWenZhang(false);
//                }
//            }
//        });
//
//    }
//
//    // 获取到文章列表
//    private void getWenZhang(final boolean isRefresh) {
//        ApiRetrofit.getApiService().listWenZhang(page)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<WenZhang>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(WenZhang wenZhang) {
//                        // 获取到总的页数
//                        recyclerView.setLoadingMoreEnabled(true);
//                        swipe.setRefreshing(false);
//                        totalPage = wenZhang.getData().getPageCount();
//
//                        pageSize = wenZhang.getData().getSize();
//
//                        List<WenZhang.DataBean.DatasBean> datas = wenZhang.getData().getDatas();
//                        if (datas != null && datas.size() > 0) {
//
//                            // 需要判断一下有没有刷新
//                            setData(isRefresh, datas);
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.showShortToast(TAG, "数据请求错误" + e.getMessage());
//                        recyclerView.setLoadingMoreEnabled(true);
//                        swipe.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 加载数据完成以后必须添加这个
//                        recyclerView.loadMoreComplete();
//
//                    }
//                });
//    }
//
//
//    private void setData(final boolean isRefresh, List<WenZhang.DataBean.DatasBean> datas) {
//
//        int size = datas == null ? 0 : datas.size();
//
//
//        if (isRefresh) {
//            adapter.setDatas(datas);
//        } else {
//            if (size > 0) {             //  这里有个小Bug,就是没有加上第一页的数据，先放着吧;
//                dataList.addAll(datas);
//                adapter.setDatas(dataList);
//            }
//        }
//
//
//        if (size < pageSize) {
//            //第一页如果不够一页就不显示没有更多数据布局
//            recyclerView.setNoMore(true);
//        } else {
//
//            recyclerView.loadMoreComplete();
//
//        }
//
//    }
//
//    @Override
//    public void onRefresh() {
//        page = 0;
//        recyclerView.setLoadingMoreEnabled(false);
//        getWenZhang(true);
//    }
}
