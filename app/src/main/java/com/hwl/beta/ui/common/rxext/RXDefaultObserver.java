package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.widget.Toast;

import com.hwl.beta.HWLApp;

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
        if (e instanceof NetException&&((NetException)e).getCode()==NetExceptionCode.TokenInvalid) {
			onRelogin();
		}else{
			onError(e.getMessage());
		}
    }

    @Override
    public void onComplete() {
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
