package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.general.body.SendEmailResponse;
import com.hwl.beta.net.general.body.SendSMSResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserRegisterResponse;
import com.hwl.im.common.DefaultAction;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.entry.bean.RegisterBean;
import com.hwl.beta.ui.entry.standard.RegisterStandard;

/**
 * RegisterHandle
 */
public class RegisterHandle implements RegisterStandard {


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
    public void userRegister(RegisterBean registerBean, final DefaultAction succCallback, final
    DefaultConsumer<String> errorCallback) {
        if (registerBean == null) {
            errorCallback.accept("register parameter is empty");
            return;
        }
        String email = "";
        String mobile = "";
        if (registerBean.getIsEmail()) {
            email = registerBean.getAccount();
        } else {
            mobile = registerBean.getAccount();
        }

        UserService.userRegister(email,
                mobile,
                registerBean.getMd5Password(),
                registerBean.getMd5PasswordOK(),
                registerBean.getCheckCode())
                .subscribe(new NetDefaultObserver<UserRegisterResponse>() {
                    @Override
                    protected void onSuccess(UserRegisterResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            succCallback.run();
                        } else {
                            onError("注册失败");
                        }
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        errorCallback.accept(resultMessage);
                    }
                });
    }

    @Override
    public void sendCode(RegisterBean registerBean, final DefaultAction succCallback, final
    DefaultConsumer
            <String> errorCallback) {
        if (registerBean.getIsEmail()) {
            GeneralService.sendEmail(registerBean.getAccount()).subscribe(new NetDefaultObserver<SendEmailResponse>() {
                @Override
                protected void onSuccess(SendEmailResponse response) {
                    if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                        succCallback.run();
                    } else {
                        onError("发送失败");
                    }
                }

                @Override
                protected void onError(String resultMessage) {
                    super.onError(resultMessage);
                    errorCallback.accept(resultMessage);
                }
            });
        } else {
            GeneralService.sendSMS(registerBean.getAccount()).subscribe(new NetDefaultObserver<SendSMSResponse>() {
                @Override
                protected void onSuccess(SendSMSResponse response) {
                    if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                        succCallback.run();
                    } else {
                        onError("发送失败");
                    }
                }

                @Override
                protected void onError(String resultMessage) {
                    super.onError(resultMessage);
                    errorCallback.accept(resultMessage);
                }
            });
        }

    }
}