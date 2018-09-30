package com.hwl.beta.ui.common.rxext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.hwl.beta.HWLApp;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.ResponseHead;
import com.hwl.beta.utils.StringUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/3/29.
 */

public abstract class NetDefaultObserver<T> implements Observer<ResponseBase<T>> {

    private static final String TAG = "NetDefaultObserver";
    private Context mContext;
    private boolean isToast = true;

    protected NetDefaultObserver() {
        this.mContext = HWLApp.getContext();
    }

    protected NetDefaultObserver(boolean isToast) {
        this();
        this.isToast = isToast;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(ResponseBase<T> response) {
        if (response != null) {
            ResponseHead head = response.getResponseHead();
            if (head == null) {
                onError("response head is null !");
            } else {
                if (head.getResultCode().equals(NetConstant.RESPONSE_SUCCESS)) {
                    T t = response.getResponseBody();
                    onSuccess(t);
                } else if (head.getResultCode().equals(NetConstant.RESPONSE_RELOGIN)) {
                    onRelogin();
                } else {
                    onError(head.getResultMessage());
                }
            }
        } else {
            onError("response stream is null !");
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "NetDefaultObserver-onError:" + e.getMessage());
        if (e instanceof SocketTimeoutException) {
            onError("网络请求超时");
        } else {
            //onError("网络请求异常，请稍后再试");
            onError("网络请求异常:" + e.getMessage());
        }
    }

    protected void onRelogin() {
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "NetDefaultObserver-onComplete");
    }


    protected abstract void onSuccess(T response);

    protected void onError(String resultMessage) {
        if (isToast) {
            if (StringUtils.isBlank(resultMessage))
                resultMessage = "数据请求失败";
            Toast.makeText(mContext, resultMessage, Toast.LENGTH_SHORT).show();
        }
    }
}
