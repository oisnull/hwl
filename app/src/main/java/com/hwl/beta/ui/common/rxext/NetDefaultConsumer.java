//package com.hwl.beta.ui.common.rxext;
//
//import android.content.Context;
//
//import com.hwl.beta.HWLApp;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.ResponseBase;
//import com.hwl.beta.net.ResponseHead;
//import com.hwl.beta.utils.StringUtils;
//
//import io.reactivex.functions.Consumer;
//
///**
// * Created by Administrator on 2018/3/29.
// */
//
//public abstract class NetDefaultConsumer<T> implements Consumer<ResponseBase<T>> {
//
//    private static final String TAG = "NetDefaultConsumer";
//    private Context mContext;
//
//    protected NetDefaultConsumer() {
//        this.mContext = HWLApp.getContext();
//    }
//
//    @Override
//    public void accept(ResponseBase<T> response) throws Exception {
//        if (response != null) {
//            ResponseHead head = response.getResponseHead();
//            if (head == null) {
//                onError("response head is null !");
//            } else {
//                if (head.getResultCode().equals(NetConstant.RESPONSE_SUCCESS)) {
//                    T t = response.getResponseBody();
//                    onSuccess(t);
//                } else if (head.getResultCode().equals(NetConstant.RESPONSE_RELOGIN)) {
//                    onRelogin();
//                } else {
//                    onError(head.getResultMessage());
//                }
//            }
//        } else {
//            onError("response stream is null !");
//        }
//    }
//
//    protected abstract void onSuccess(T response);
//
//    protected void onRelogin() {
//    }
//
//    protected void onError(String resultMessage) {
//        if (StringUtils.isBlank(resultMessage))
//            resultMessage = "数据请求失败";
//    }
//}
