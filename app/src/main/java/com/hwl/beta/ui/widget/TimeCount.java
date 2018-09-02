package com.hwl.beta.ui.widget;

/**
 * Created by Administrator on 2018/1/14.
 */

import android.os.CountDownTimer;
import android.widget.TextView;

public class TimeCount extends CountDownTimer {
    private TextView tvChecking;
    private TimeCountInterface timeCountInterface;

    public TimeCount(long millisInFuture, long countDownInterval, TextView tvChecking, TimeCountInterface timeCountInterface) {
        super(millisInFuture, countDownInterval);
        this.tvChecking = tvChecking;
        this.timeCountInterface = timeCountInterface;
    }

    @Override
    public void onFinish() {
        if (timeCountInterface != null) {
            if (tvChecking != null)
                timeCountInterface.onFinishViewChange(tvChecking.getId());
            else
                timeCountInterface.onFinishViewChange(0);
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (timeCountInterface != null) {
            if (tvChecking != null)
                timeCountInterface.onTickViewChange(millisUntilFinished, tvChecking.getId());
            else
                timeCountInterface.onTickViewChange(millisUntilFinished, 0);
        }
    }

    public interface TimeCountInterface {
        void onFinishViewChange(int resId);

        void onTickViewChange(long millisUntilFinished, int resId);
    }
}