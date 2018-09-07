package com.hwl.beta.ui.entry;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import main.java.com.hwl.beta.ui.entry.logic.LoginHandle;

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
    LoginHandle loginHandle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        loginHandle = new LoginHandle();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginBean(loginHandle.getLoginBean());
        binding.setAction(new LoginListener());
        binding.tbTitle.setTitle(R.string.login_activity_title).setImageLeftHide().setImageRightHide();
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

            loginHandle.userLogin(loginBean, () -> {
                UITransfer.toWelcomeActivity(activity);
                finish();
            }, (msg)->{
                isRuning = false;
                binding.btnLogin.setText("登   录");
            });
        }

        @Override
        public void onLoginClick() {
            login();
        }

        @Override
        public void onRegisterClick() {
            // UITransfer.toRegisterActivity(activity);
        }

        @Override
        public void onGetpwdClick() {
            // UITransfer.toGetpwdActivity(activity);
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
            // SinaAction.login();
        }
    }
}
