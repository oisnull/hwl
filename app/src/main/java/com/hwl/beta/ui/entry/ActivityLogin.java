package com.hwl.beta.ui.entry;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityLoginBinding;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserLoginResponse;
import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.entry.action.ILoginListener;
import com.hwl.beta.ui.entry.bean.LoginBean;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ActivityLogin extends FragmentActivity {

    Activity activity;
    ActivityLoginBinding binding;
    LoginBean loginBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginBean = new LoginBean();
        loginBean.setAccount(UserSP.getAccount());
        loginBean.setPassword("123456");
        binding.setLoginBean(loginBean);
        binding.setAction(new LoginListener());

        binding.tbTitle
                .setTitle("HWL登录")
                .setImageLeftHide()
                .setImageRightHide();
    }

    private class LoginListener implements ILoginListener {
        boolean isRuning = false;

        private void login() {
            if (isRuning) {
                return;
            }
            isRuning = true;
            binding.btnLogin.setText("登 录 中...");

            if (!loginBean.checkParams()) {
                isRuning = false;
                binding.btnLogin.setText("登   录");
                Toast.makeText(activity, loginBean.getMessageCode(), Toast.LENGTH_SHORT).show();
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
//                            UITransfer.toWelcomeActivity(activity);
                            finish();
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                            binding.btnLogin.setText("登   录");
                        }
                    });
        }

        @Override
        public void onLoginClick() {
            login();
        }

        @Override
        public void onRegisterClick() {
            //UITransfer.toRegisterActivity(activity);
        }

        @Override
        public void onGetpwdClick() {
            //UITransfer.toGetpwdActivity(activity);
        }

        @Override
        public void onWechatClick() {
            Toast.makeText(activity, "WeChat登录后期开放...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onQQClick() {
            Toast.makeText(activity, "QQ登录后期开放...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSinaClick() {
            Toast.makeText(activity, "SINA微博登录后期开放...", Toast.LENGTH_SHORT).show();
//            SinaAction.login();
        }
    }
}
