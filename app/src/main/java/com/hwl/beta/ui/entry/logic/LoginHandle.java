package main.java.com.hwl.beta.ui.entry.logic;

import java.util.function.Consumer;

import com.hwl.beta.ui.entry.bean.LoginBean;

import main.java.com.hwl.beta.ui.entry.standard.LoginStandard;

public class LoginHandle implements LoginStandard {

    @Override
    public LoginBean getLoginBean() {
        LoginBean loginBean = new LoginBean();
        loginBean.setAccount(UserSP.getAccount());
        loginBean.setPassword("123456");
        return loginBean;
    }

    @Override
    public void userLogin(LoginBean loginBean, Runnable succCallback, Consumer<String> errorCallback) {
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