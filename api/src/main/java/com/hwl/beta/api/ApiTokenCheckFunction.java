package com.hwl.beta.api;

import android.content.Context;

import com.hwl.beta.api.dialog.TokenInvalidDialog;
import com.hwl.beta.api.mode.ApiResponse;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2019/3/26.
 */
public class ApiTokenCheckFunction<T> implements Function<ApiResponse<T>, T> {

    Context context;

    public ApiTokenCheckFunction(Context context) {
        this.context = context;
    }

    @Override
    public T apply(ApiResponse<T> response) throws Exception {
        if (response == null)
            throw new Exception("Response stream is empty");

        if (response.Head == null)
            throw new Exception("Response head is empty");

        if (response.Head.Status == 200)
            return response.Body;
        else if (response.Head.Status == 410) {
            TokenInvalidDialog.getInstance().show(context);
            return null;
        } else
            throw new Exception(response.Head.Output);
    }
}
