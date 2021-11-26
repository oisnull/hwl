package com.hwl.beta.api.account;

import com.hwl.beta.api.ApiDefaultFunction;
import com.hwl.beta.api.RetrofitUtils;
import com.hwl.beta.api.account.parameters.EmailCodeLoginRequest;
import com.hwl.beta.api.account.parameters.EmailCodeLoginResponse;
import com.hwl.beta.api.account.parameters.SendEmailRequest;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AccountService {
    public static Observable sendEmail(String email) {
        SendEmailRequest request = new SendEmailRequest();
        request.ToEmail = email;
        return RetrofitUtils.createApi(IAccountService.class)
                .SendEmail(request)
                .map(new ApiDefaultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<EmailCodeLoginResponse> emailCodeLogin(String email, String code) {
        EmailCodeLoginRequest request = new EmailCodeLoginRequest();
        request.Email = email;
        request.Code = code;
        return RetrofitUtils.createApi(IAccountService.class)
                .EmailCodeLogin(request)
                .map(new ApiDefaultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}