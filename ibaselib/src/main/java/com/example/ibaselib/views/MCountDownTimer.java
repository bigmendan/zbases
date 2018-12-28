package com.example.ibaselib.views;

import android.os.CountDownTimer;

/**
 * Created by EDZ on 2018/7/2.
 *  使用 方法
 *     当需要使用倒计时时。
 *     MCountDownTimer timer = null;
 *
 *     if（timer == null）{
 *         timer = new MCountDownTimer(millisInFuture, countDownInterval);  // 持续的时间  和  时间间隔；
 *     }
 *     timer.start();
 *
 *
 *     在 Activity  或者 Fragment  的生命周期 的 onDestroy() 时
 *      if (timer != null) {
            timer.cancel();
         }
 */

public class MCountDownTimer extends CountDownTimer {

    private TickInterface tickInterface;
    private FinishInterface finishInterface;


    public void setTickInterface(TickInterface tickInterface) {
        this.tickInterface = tickInterface;
    }

    public void setFinishInterface(FinishInterface finishInterface) {
        this.finishInterface = finishInterface;
    }

    /**
     * @param millisInFuture    总共持续的时间
     * @param countDownInterval 倒计时的时间间隔
     */
    public MCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    /**
     * @param millisUntilFinished 还剩下的时间
     */
    @Override
    public void onTick(long millisUntilFinished) {
//        sendCode.setText(millisUntilFinished / countDownInterval + "s");
        tickInterface.onTicks();
    }

    /**
     * 倒计时结束时候回调
     */
    @Override
    public void onFinish() {
        //倒计时结束让按钮可用
//        sendCode.setEnabled(true);
//        sendCode.setText("获取验证码");

        finishInterface.onFinishs();
    }


    public interface TickInterface {
        void onTicks();
    }


    public interface FinishInterface {
        void onFinishs();
    }

}
