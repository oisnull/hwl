package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.widget.Toast;

import com.hwl.beta.HWLApp;

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
        onError(e.getMessage());
    }

    @Override
    public void onComplete() {
        onSuccess();
    }

    protected void onSuccess() {
    }

    protected void onError(String message) {
        if (isToast) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}
