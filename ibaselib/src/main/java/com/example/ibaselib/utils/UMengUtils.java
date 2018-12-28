package com.example.ibaselib.utils;

import android.app.Activity;
import android.content.Context;
import android.net.sip.SipSession;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.ibaselib.model.Test;
import com.example.ibaselib.model.UMengModel;
import com.example.ibaselib.utils.eventbus.Event;
import com.example.ibaselib.utils.eventbus.EventBusUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Map;

/**
 * Author
 * DATE
 * Des	      ${友盟分享工具类}
 * 使用 ：     UMengUtils.shareWeb(this, Defaultcontent.url, Defaultcontent.title
 * ,                                      Defaultcontent.text, Defaultcontent.imageurl, R.drawable.icon_logo_share, SHARE_MEDIA.QQ);
 */
public class UMengUtils {

    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(WebUrl);//连接地址
        web.setTitle(title);//标题
        web.setDescription(description);//描述
        if (TextUtils.isEmpty(imageUrl)) {
            web.setThumb(new UMImage(activity, imageID));  //本地缩略图
        } else {
            web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
        }

        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media.name().equals("WEIXIN_FAVORITE")) {
                                    Toast.makeText(activity, share_media + " 收藏成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity, share_media + " 分享成功", Toast.LENGTH_SHORT).show();
                                    // TODO  需要调用 分享赚积分;

                                }

                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, share_media + " 分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();

        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/
    }


    /**
     * 友盟三方登录;
     *
     * @param activity
     * @param share_media
     * @param listener
     */
    public static void authorization(Activity activity, SHARE_MEDIA share_media, final AuthorizationListener listener) {
        UMShareAPI.get(activity).getPlatformInfo(activity, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("授权 = ", "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.e("授权 = ", "onComplete " + "授权完成");

                //sdk是6.4.4的,但是获取值的时候用的是6.2以前的(access_token)才能获取到值,未知原因
                String uid = map.get("uid");
                String openid = map.get("openid");//微博没有
                String unionid = map.get("unionid");//微博没有
                String access_token = map.get("access_token");
                String refresh_token = map.get("refresh_token");//微信,qq,微博都没有获取到
                String expires_in = map.get("expires_in");
                String name = map.get("name");
                String gender = map.get("gender");
                String iconurl = map.get("iconurl");

//                Toast.makeText(mContext(), "name=" + name + ",gender=" + gender, Toast.LENGTH_SHORT).show();

                //拿到信息去请求登录接口。。。
                UMengModel model = new UMengModel.Builder()
                        .uid(uid)
                        .openid(openid)
                        .unionid(unionid)
                        .access_token(access_token)
                        .refresh_token(refresh_token)
                        .expires_in(expires_in)
                        .name(name)
                        .gender(gender)
                        .iconurl(iconurl)
                        .build();

                listener.authorComplete(model);

            }


            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.e("授权 = ", "onError " + "授权失败");

                listener.authorError(throwable);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.e("授权 = ", "onCancel " + "授权取消");
            }
        });
    }


    public interface AuthorizationListener {

        void authorComplete(UMengModel model);

        void authorError(Throwable throwable);
    }


    // 分享
    private void shareInteract() {

    }

}
