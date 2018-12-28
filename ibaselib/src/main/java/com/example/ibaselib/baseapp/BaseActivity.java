package com.example.ibaselib.baseapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.ibaselib.R;
import com.example.ibaselib.utils.eventbus.Event;
import com.example.ibaselib.utils.eventbus.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseActivity extends AppCompatActivity {
    private static String TAG;
    private long mPreTime;
    protected Activity mCurrentActivity;
    private ArrayList<Activity> activities;

    Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getResId());

        TAG = "==" + getClass().getSimpleName();

        if (activities == null) {
            activities = new ArrayList<>();
        }
        activities.add(this);

        // 绑定EventBus
        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }

        initDatas();

    }

    /**
     * ButterKnife 必须 写在 setContentView() 函数后面
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCurrentActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCurrentActivity = null;
    }


    protected abstract int getResId();

    protected abstract void initDatas();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        activities.remove(this);

        // 解绑EventBus
        if (isRegisterEventBus()) {
            EventBusUtil.unregister(this);
        }
    }

    /**
     * 接收时间分发
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(Event event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(Event event) {

    }

    /**
     * 是否注册事件分发
     *
     * @return true绑定EventBus事件分发，默认不绑定，子类需要绑定的话复写此方法返回true.
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    /**
     * 统一退出控制
     */
    @Override
    public void onBackPressed() {
//        if (mCurrentActivity instanceof BaseActivity ) {
//            //如果是主页面
//            if (System.currentTimeMillis() - mPreTime > 2000) {// 两次点击间隔大于2秒
//
//
//                Toast.makeText(mCurrentActivity, "在按一次退出", Toast.LENGTH_SHORT).show();
//                mPreTime = System.currentTimeMillis();
//                return;
//            }
//        }
        super.onBackPressed();      // finish()
    }

    /**
     * 判断 当是HomeActivity 时 退出app
     *
     * @param clazz
     */
    protected void exitMainActivity(Class clazz) {

        onBackPressed();
    }

    /**
     * 最普通的标题
     *
     * @param title
     * @return
     */
    protected android.support.v7.widget.Toolbar setToolBar(String title) {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setToolBar(title, toolbar);
        return toolbar;

    }

    /**
     * 右边 如果是文字的 ToolBar
     *
     * @param title
     * @param rightText
     * @param listener
     * @return
     */
    protected android.support.v7.widget.Toolbar setToolBarRightText(String title, String rightText, final RightListener listener) {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setToolBar(title, toolbar);

        LinearLayout l_right = findViewById(R.id.right);
        TextView right = findViewById(R.id.t_right);
        right.setText(rightText);


        l_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRightClick(v);
            }
        });

        return toolbar;
    }

    /**
     * 右边如果是图片
     *
     * @param title
     * @param srcImg
     * @param listener
     * @return
     */
    protected android.support.v7.widget.Toolbar setToolBarRightImg(String title, int srcImg, final RightListener listener) {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setToolBar(title, toolbar);

        LinearLayout right = findViewById(R.id.right);
        ImageView i_right = findViewById(R.id.i_right);
        i_right.setImageResource(srcImg);


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRightClick(v);
            }
        });
        return toolbar;

    }

    /**
     * 自定义一个沉浸式状态栏
     */
    protected void setImmStatusBar() {

    }

    // 基本使用的
    private void setToolBar(String title, android.support.v7.widget.Toolbar toolbar) {
        LinearLayout back = findViewById(R.id.back);
        TextView tvTitle = findViewById(R.id.title);

        tvTitle.setText(title);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsRelative(10, 0);   // 这个是啥意思  不知道

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public interface RightListener {
        void onRightClick(View view);
    }

    /**
     * 将 Retrofit + RxJava   中的线程切换 封装
     *
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> setThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
