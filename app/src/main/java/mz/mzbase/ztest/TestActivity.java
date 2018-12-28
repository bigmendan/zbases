package mz.mzbase.ztest;

import android.util.Log;
import android.view.View;

import com.example.ibaselib.network.retrofitclient.ApiRetrofit;
import com.example.ibaselib.baseapp.BaseActivity;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import mz.mzbase.R;

public class TestActivity extends BaseActivity {
    private static String TAG = "=== TestActivity";
//    Map<String, String> maps;

    @Override
    protected int getResId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initDatas() {
//        maps = new HashMap<>();
//        maps.put("", "");
    }


    public void onClick(View view) {

        test();

    }


    private void test() {
//        ApiRetrofit.getInstance(ApiConstant.BASE_URL)
//                .create(ApiService.class)
//                .getBanner2()       // 使用的是 Observable ，区别与 Flowable, 不支持 被压
//                .compose(this.setThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.e(TAG, "onSubscribe: ");
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//                        Log.e(TAG, "onNext: " + o.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG, "onError: " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e(TAG, "onComplete: ");
//                    }
//                });

    }
}
