package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.widget.Toast;

import com.hwl.beta.HWLApp;
import com.hwl.beta.net.NetException;
import com.hwl.beta.net.NetExceptionCode;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RXDefaultObserverEmpty implements Observer {
    private Context mContext;
    private boolean isToast = true;

    protected RXDefaultObserverEmpty() {
        this.mContext = HWLApp.getContext();
    }

    protected RXDefaultObserverEmpty(boolean isToast) {
        this();
        this.isToast = isToast;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Object o) {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof NetException&&((NetException)e).getCode()== NetExceptionCode.TokenInvalid) {
			onRelogin();
		}else{
			onError(e.getMessage());
		}
    }

    @Override
    public void onComplete() {
        onSuccess();
    }

    protected void onSuccess() {
    }

    protected void onRelogin() {
    }

    protected void onError(String message) {
        if (isToast) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}
