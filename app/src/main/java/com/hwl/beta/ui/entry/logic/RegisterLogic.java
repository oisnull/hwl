package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.general.body.SendEmailResponse;
import com.hwl.beta.net.general.body.SendSMSResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserRegisterResponse;
import com.hwl.beta.ui.entry.bean.RegisterBean;
import com.hwl.beta.ui.entry.standard.RegisterStandard;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * RegisterLogic
 */
public class RegisterLogic implements RegisterStandard {


    @Override
    public RegisterBean getRegisterBean() {
        RegisterBean registerBean = new RegisterBean();
        registerBean.setAccount("253621965@qq.com");
        registerBean.setCheckCode("888888");
        registerBean.setPassword("123456");
        registerBean.setPasswordOK("123456");
        return registerBean;
    }

    @Override
    public Observable<Boolean> userRegister(RegisterBean registerBean) {
//        if (registerBean == null) {
//            errorCallback.accept("register parameter is empty");
//            return;
//        }
        String email = "";
        String mobile = "";
        if (registerBean.getIsEmail()) {
            email = registerBean.getAccount();
        } else {
            mobile = registerBean.getAccount();
        }

        return UserService.userRegister(email,
                mobile,
                registerBean.getMd5Password(),
                registerBean.getMd5PasswordOK(),
                registerBean.getCheckCode())
                .map(new Function<UserRegisterResponse, Boolean>() {
                    @Override
                    public Boolean apply(UserRegisterResponse response) {
                        return response.getStatus() == NetConstant.RESULT_SUCCESS;
                    }
                });
//                .subscribe(new NetDefaultObserver<UserRegisterResponse>() {
//                    @Override
//                    protected void onSuccess(UserRegisterResponse response) {
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            succCallback.run();
//                        } else {
//                            onError("注册失败");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        errorCallback.accept(resultMessage);
//                    }
//                });
    }

    @Override
    public Observable<Boolean> sendCode(RegisterBean registerBean) {
        if (registerBean.getIsEmail()) {
            return GeneralService.sendEmail(registerBean.getAccount())
                    .map(new Function<SendEmailResponse, Boolean>() {
                        @Override
                        public Boolean apply(SendEmailResponse response) {
                            return response.getStatus() == NetConstant.RESULT_SUCCESS;
                        }
                    });
        } else {
            return GeneralService.sendSMS(registerBean.getAccount())
                    .map(new Function<SendSMSResponse, Boolean>() {
                        @Override
                        public Boolean apply(SendSMSResponse response) {
                            return response.getStatus() == NetConstant.RESULT_SUCCESS;
                        }
                    });
        }

    }
}