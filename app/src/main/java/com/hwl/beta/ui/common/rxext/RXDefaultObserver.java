package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hwl.beta.HWLApp;
import com.hwl.beta.net.NetExceptionCode;
import com.hwl.beta.utils.StringUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RXDefaultObserver<T> implements Observer<T> {
    private Context mContext;
    private boolean isToast = true;
    private T t;

    protected RXDefaultObserver() {
        this.mContext = HWLApp.getContext();
    }

    protected RXDefaultObserver(boolean isToast) {
        this();
        this.isToast = isToast;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        this.t = t;
    }

    @Override
    public void onError(Throwable e) {
        if (NetExceptionCode.isTokenInvalid(e)) {
            onRelogin();
        } else {
            onError(StringUtils.isBlank(e.getMessage()) ? "获取API数据失败" : e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        if (t != null)
            Log.d("RXDefaultObserver", t.toString());
        onSuccess(t);
    }

    protected void onSuccess(T t) {
    }

    protected void onRelogin() {
    }

    protected void onError(String message) {
        if (isToast) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}
