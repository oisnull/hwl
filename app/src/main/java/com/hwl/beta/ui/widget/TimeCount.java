package com.hwl.beta.ui.widget;

/**
 * Created by Administrator on 2018/1/14.
 */

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    private TextView tvChecking;
    private TimeListener timeListener;

    public TimeCount(long millisInFuture, long countDownInterval, TextView tvChecking, TimeListener timeListener) {
        super(millisInFuture, countDownInterval);
        this.tvChecking = tvChecking;
        this.timeListener = timeListener;
    }

    @Override
    public void onFinish() {
        if (timeListener != null) {
            if (tvChecking != null)
                timeListener.onFinishViewChange(tvChecking.getId());
            else
                timeListener.onFinishViewChange(0);
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (timeListener != null) {
            if (tvChecking != null)
                timeListener.onTickViewChange(millisUntilFinished, tvChecking.getId());
            else
                timeListener.onTickViewChange(millisUntilFinished, 0);
        }
    }

    public interface TimeListener {
        void onFinishViewChange(int resId);

        void onTickViewChange(long millisUntilFinished, int resId);
    }
}