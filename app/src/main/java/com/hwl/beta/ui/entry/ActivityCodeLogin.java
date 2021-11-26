package com.hwl.beta.ui.entry;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.widget.Toast;

import com.hwl.beta.BuildConfig;
import com.hwl.beta.R;
import com.hwl.beta.api.account.AccountService;
import com.hwl.beta.databinding.EntryActivityCodeLoginBinding;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserLoginAndRegisterResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.widget.TimeCount;
import com.hwl.beta.utils.StringUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityCodeLogin extends FragmentActivity {

    EntryActivityCodeLoginBinding binding;
    TimeCount timer = null;
    boolean isRunning = false;
    boolean isEmail = false;
    int TOTAL_TIME_SECONDS = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EntryActivityCodeLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle(BuildConfig.AppName)
                .setImageLeftHide()
                .setImageRightHide();

        binding.etAccount.setText(UserSP.getAccount());
        binding.etCode.setText(BuildConfig.LoginCode);
        binding.btnCodeSend.setOnClickListener(v -> sendCode());
        binding.btnLogin.setOnClickListener(v -> {
            KeyBoardAction.hideSoftInput(this);
            login();
        });
        binding.getRoot().setOnClickListener(v -> KeyBoardAction.hideSoftInput(ActivityCodeLogin.this));

        timer = new TimeCount(TOTAL_TIME_SECONDS * 1000, 1000, binding.btnCodeSend, new TimeCount.TimeListener() {
            @Override
            public void onFinishViewChange(int resId) {
                setSendViewStatus(0);
            }

            @Override
            public void onTickViewChange(long millisUntilFinished, int resId) {
                binding.btnCodeSend.setText(millisUntilFinished / 1000 + " 秒");
            }
        });
    }

    private void setSendViewStatus(int status) {
        switch (status) {
            case 0:
                binding.btnCodeSend.setClickable(true);
                binding.btnCodeSend.setText("获取验证码");
                break;
            case 1:
                binding.btnCodeSend.setClickable(false);
                binding.btnCodeSend.setText("发送中...");
                break;
            case 2:
                timer.start();
                binding.btnCodeSend.setClickable(false);
                break;
        }
    }

    private void sendCode() {
        if (isRunning) return;
        isRunning = true;

        if (!validateAccount()) {
            isRunning = false;
            return;
        }

        setSendViewStatus(1);

        String account = binding.etAccount.getText().toString();
        Observable<Boolean> observable = null;
        if (isEmail) {
            observable = AccountService.sendEmail(account);
        } else {
//            observable = AccountService.sendSMS(account);
        }


        observable.subscribe(s -> {

        }, throwable -> {

        });

        observable.subscribe(new RXDefaultObserver<Boolean>() {
            @Override
            protected void onSuccess(Boolean success) {
                isRunning = false;
                if (success) {
                    setSendViewStatus(2);
                } else {
                    onError("发送失败，可以再发一次试试看!");
                }
            }

            @Override
            protected void onError(String message) {
                super.onError(message);
                setSendViewStatus(0);
                isRunning = false;
            }
        });
    }

    private void login() {
        if (isRunning) return;
        isRunning = true;

        if (!validateAccount()) {
            isRunning = false;
            return;
        }

        if (!validateCode()) {
            isRunning = false;
            return;
        }

        binding.btnLogin.setText("登 录 中...");

        String account = binding.etAccount.getText().toString();
        String code = binding.etCode.getText().toString();
        Observable<UserLoginAndRegisterResponse> observable = null;
        if (isEmail) {
            observable = AccountService.sendEmail(account);
        } else {
//            observable = UserService.userLoginAndReg(null, account, code);
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<UserLoginAndRegisterResponse>(true) {
                    @Override
                    protected void onSuccess(UserLoginAndRegisterResponse response) {
                        UserSP.setUserInfo(response.getUserInfo());
                        UITransfer.toWelcomeActivity(ActivityCodeLogin.this);
                        finish();
                    }

                    @Override
                    protected void onError(String message) {
                        super.onError(message);
                        isRunning = false;
                        binding.btnLogin.setText("登   录");
                    }
                });
    }

    private boolean validateAccount() {
        String account = binding.etAccount.getText().toString();
        boolean isEmail = StringUtils.isEmail(account);
        if (isEmail) {
            UserSP.setEmail(account);
        }
        boolean isPhone = StringUtils.isPhone(account);
        if (isPhone) {
            UserSP.setPhone(account);
        }
        if (!isEmail && !isPhone) {
            binding.etAccount.setFocusable(true);
            binding.etAccount.setFocusableInTouchMode(true);
            binding.etAccount.requestFocus();
            KeyBoardAction.showSoftInput(this);
            Toast.makeText(this, "请输入手机号或邮箱", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateCode() {
        String code = binding.etCode.getText().toString();
        if (StringUtils.isBlank(code)) {
            binding.etCode.setFocusable(true);
            binding.etCode.setFocusableInTouchMode(true);
            binding.etCode.requestFocus();
            KeyBoardAction.showSoftInput(this);
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
