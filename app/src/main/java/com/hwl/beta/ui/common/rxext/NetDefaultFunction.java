//package com.hwl.beta.ui.common.rxext;
//
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.ResponseBase;
//import com.hwl.beta.net.ResponseHead;
//import com.hwl.beta.utils.StringUtils;
//
//import io.reactivex.ObservableSource;
//import io.reactivex.functions.Function;
//
///**
// * Created by Administrator on 2018/4/14.
// */
//
//public abstract class NetDefaultFunction<T, T2> implements Function<ResponseBase<T>, ObservableSource<T2>> {
//    @Override
//    public ObservableSource<T2> apply(ResponseBase<T> response) throws Exception {
//        if (response != null) {
//            ResponseHead head = response.getResponseHead();
//            if (head == null) {
//                onError("response head is null !");
//            } else {
//                if (head.getResultCode().equals(NetConstant.RESPONSE_SUCCESS)) {
//                    T t = response.getResponseBody();
//                    return onSuccess(t);
//                } else if (head.getResultCode().equals(NetConstant.RESPONSE_RELOGIN)) {
//                    onError(NetConstant.RESPONSE_RELOGIN);
//                } else {
//                    onError(head.getResultMessage());
//                }
//            }
//        } else {
//            onError("response stream is null !");
//        }
//        return null;
//    }
//
//    protected abstract ObservableSource<T2> onSuccess(T response);
//
//    protected void onError(String resultMessage) throws Exception {
//        if (StringUtils.isBlank(resultMessage))
//            resultMessage = "数据请求失败";
//        throw new Exception(resultMessage);
//    }
//}
