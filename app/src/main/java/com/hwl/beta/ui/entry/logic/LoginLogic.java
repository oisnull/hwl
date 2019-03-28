package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserLoginResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.entry.bean.LoginBean;

import com.hwl.beta.ui.entry.standard.LoginStandard;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class LoginLogic implements LoginStandard {

    @Override
    public LoginBean getLoginBean() {
        LoginBean loginBean = new LoginBean();
        loginBean.setAccount(UserSP.getAccount());
        loginBean.setPassword("123456");
        return loginBean;
    }

    @Override
    public Observable userLogin(LoginBean loginBean) {
        String email = "";
        String mobile = "";
        if (loginBean.getIsEmail()) {
            email = loginBean.getAccount();
        } else {
            mobile = loginBean.getAccount();
        }

        return UserService.userLogin(email, mobile, loginBean.getMd5Password())
                .doOnNext(new Consumer<UserLoginResponse>() {
                    @Override
                    public void accept(UserLoginResponse response) throws Exception {
                        UserSP.setUserInfo(response.getUserInfo());
                    }
                });
    }

}