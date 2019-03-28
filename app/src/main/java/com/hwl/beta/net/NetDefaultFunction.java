package com.hwl.beta.net;

import com.hwl.beta.utils.StringUtils;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2019/3/26.
 */
public class NetDefaultFunction<T> implements Function<ResponseBase<T>, T> {

    @Override
    public T apply(ResponseBase<T> response) throws Exception {
        if (response == null)
            throw new Exception("Response stream is empty");

        if (response.getResponseHead() == null)
            throw new Exception("Response head is empty");

        if (response.getResponseHead().getResultCode().equals(NetConstant.RESPONSE_SUCCESS))
            return response.getResponseBody();
        else if (response.getResponseHead().getResultCode().equals(NetConstant.RESPONSE_RELOGIN))
            throw new NetException(NetExceptionCode.TokenInvalid);
        else
            throw new Exception(getFailedMessage(response));
    }

    private String getFailedMessage(ResponseBase<T> response) {
        if (StringUtils.isBlank(response.getResponseHead().getResultMessage())) {
            return "Network error,request data failed";
        }

        return response.getResponseHead().getResultMessage();
    }
}
