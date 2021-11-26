package com.hwl.beta.api.account;

import com.hwl.beta.api.account.parameters.EmailCodeLoginRequest;
import com.hwl.beta.api.account.parameters.EmailCodeLoginResponse;
import com.hwl.beta.api.account.parameters.SMSCodeLoginRequest;
import com.hwl.beta.api.account.parameters.SMSCodeLoginResponse;
import com.hwl.beta.api.account.parameters.SendEmailRequest;
import com.hwl.beta.api.account.parameters.SendSMSRequest;
import com.hwl.beta.api.mode.ApiResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAccountService {
    @POST("api/EmailCodeLogin")
    Observable<ApiResponse<EmailCodeLoginResponse>> EmailCodeLogin(@Body EmailCodeLoginRequest request);

    @POST("api/SMSCodeLogin")
    Observable<ApiResponse<SMSCodeLoginResponse>> SMSCodeLogin(@Body SMSCodeLoginRequest request);

    @POST("api/SendEmail")
    Observable<ApiResponse<Object>> SendEmail(@Body SendEmailRequest request);

    @POST("api/SendSMS")
    Observable<ApiResponse<Object>> SendSMS(@Body SendSMSRequest request);
}
