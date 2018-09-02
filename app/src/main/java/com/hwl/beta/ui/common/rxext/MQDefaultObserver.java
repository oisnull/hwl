package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.StringUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/31.
 */

public abstract class MQDefaultObserver implements Observer<Boolean> {
    private static final String TAG = "MQDefaultObserver";
    private Context mContext;
    private boolean isToast = true;

    protected MQDefaultObserver() {
        this.mContext = HWLApp.getContext();
    }

    protected MQDefaultObserver(boolean isToast) {
        this();
        this.isToast = isToast;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Boolean succ) {
        if (succ) {
            onSuccess();
        } else {
            onError("消息发送失败");
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "MQDefaultObserver-onError:" + e.getMessage());
        if (e instanceof SocketTimeoutException) {
            onError("发送消息网络请求超时");
        } else {
            onError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "MQDefaultObserver-onComplete");
    }

    protected abstract void onSuccess();

    protected void onError(String resultMessage) {
        if (isToast) {
            if(StringUtils.isBlank(resultMessage))
                resultMessage="消息发送失败";
            Toast.makeText(mContext, resultMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
