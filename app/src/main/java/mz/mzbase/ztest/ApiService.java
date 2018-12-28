package mz.mzbase.ztest;


import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by EDZ on 2018/6/29.
 */

public interface ApiService {

    @GET("banner/json")
    Flowable<Object> getBanner();


//    @GET("banner/json")
//    Observable<BaseResponse<BannerBean>> getBanner3();
}
