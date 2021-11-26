package com.hwl.beta.api;

import com.hwl.beta.api.mode.ApiResponse;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2019/3/26.
 */
public class ApiDefaultFunction<T> implements Function<ApiResponse<T>, T> {

    @Override
    public T apply(ApiResponse<T> response) throws Exception {
        if (response == null)
            throw new Exception("Response stream is empty");

        if (response.Head == null)
            throw new Exception("Response head is empty");

        if (response.Head.Status == 200)
            return response.Body;
        else if (response.Head.Status == 410) {
            throw new ApiException(ApiExceptionCode.TokenInvalid);
        } else
            throw new Exception(response.Head.Output);
    }
}
