package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserLoginResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.rxext.DefaultAction;
import com.hwl.beta.ui.common.rxext.DefaultConsumer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.entry.bean.LoginBean;

import com.hwl.beta.ui.entry.standard.LoginStandard;

public class LoginHandle implements LoginStandard {

    @Override
    public LoginBean getLoginBean() {
        LoginBean loginBean = new LoginBean();
        loginBean.setAccount(UserSP.getAccount());
        loginBean.setPassword("123456");
        return loginBean;
    }

    @Override
    public void userLogin(LoginBean loginBean, final DefaultAction succCallback, final DefaultConsumer<String> errorCallback) {
        if (loginBean == null) {
            errorCallback.accept("login param is empty");
            return;
        }

        String email = "";
        String mobile = "";
        if (loginBean.getIsEmail()) {
            email = loginBean.getAccount();
        } else {
            mobile = loginBean.getAccount();
        }

        UserService.userLogin(email, mobile, loginBean.getMd5Password())
                .subscribe(new NetDefaultObserver<UserLoginResponse>() {
                    @Override
                    protected void onSuccess(UserLoginResponse response) {
                        UserSP.setUserInfo(response.getUserInfo());
                        succCallback.run();
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        errorCallback.accept(resultMessage);
                    }
                });
    }

}