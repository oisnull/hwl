package com.hwl.beta.ui.entry;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.AppConfig;
import com.hwl.beta.R;
import com.hwl.beta.databinding.EntryActivityLoginV2Binding;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.general.GeneralService;
import com.hwl.beta.net.general.body.SendEmailResponse;
import com.hwl.beta.net.general.body.SendSMSResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.UserLoginAndRegisterResponse;
import com.hwl.beta.net.user.body.UserLoginResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.widget.TimeCount;
import com.hwl.beta.utils.StringUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

public class ActivityLoginV2 extends FragmentActivity {

    FragmentActivity activity;
    EntryActivityLoginV2Binding binding;
    boolean isRunning = false;
    TimeCount timer = null;
    final int TOTAL_TIME_SECONDS = 60 * 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(this, R.layout.entry_activity_login_v2);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle(AppConfig.APP_DEFAULT_NAME)
                .setImageLeftHide()
                .setImageRightHide();

//        binding.etAccount.setText(UserSP.getAccount());
        binding.etAccount.setText("253621965@qq.com");
        binding.etCode.setText("888888");
        binding.btnCodeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardAction.hideSoftInput(activity);
                login();
            }
        });
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardAction.hideSoftInput(activity);
            }
        });

        timer = new TimeCount(TOTAL_TIME_SECONDS * 1000, 1000, binding.btnCodeSend, new
                TimeCount.TimeListener() {
                    @Override
                    public void onFinishViewChange(int resId) {
                        setSendViewStatus(NetConstant.SEND_STATUS_COMPLETE);
                        binding.btnCodeSend.setText("获取验证码");
                    }

                    @Override
                    public void onTickViewChange(long millisUntilFinished, int resId) {
                        binding.btnCodeSend.setClickable(false);
                        binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R
                                .color
                                .main));
                        binding.btnCodeSend.setText(millisUntilFinished / 1000 + " 秒");
                    }
                });
    }

    private void setSendViewStatus(int status) {
        switch (status) {
            case NetConstant.SEND_STATUS_PROGRESSING:
                binding.btnCodeSend.setClickable(false);
                binding.btnCodeSend.setText("发送中...");
                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
                        .color_b2b2b2));
                break;
            case NetConstant.SEND_STATUS_SUCCESS:
                timer.start();
                break;
            case NetConstant.SEND_STATUS_COMPLETE:
            case NetConstant.SEND_STATUS_FAILED:
                binding.btnCodeSend.setClickable(true);
                binding.btnCodeSend.setText("获取验证码");
                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
                        .main));
                break;
        }
    }

    private void sendCode() {
        if (isRunning) return;
        isRunning = true;

        String account = binding.etAccount.getText().toString();
        boolean isEmail = StringUtils.isValidEmail(account);
        boolean isPhone = StringUtils.isValidPhone(account);
        if (!isEmail && !isPhone) {
            binding.etAccount.setFocusable(true);
            binding.etAccount.setFocusableInTouchMode(true);
            binding.etAccount.requestFocus();
            KeyBoardAction.showSoftInput(activity);
            Toast.makeText(activity, "请输入手机号或邮箱", Toast.LENGTH_SHORT).show();
            isRunning = false;
            return;
        }

        setSendViewStatus(NetConstant.SEND_STATUS_PROGRESSING);

        Observable<Boolean> observable = null;
        if (isEmail) {
            observable = GeneralService.sendEmail(account)
                    .map(new Function<SendEmailResponse, Boolean>() {
                        @Override
                        public Boolean apply(SendEmailResponse response) {
                            return response.getStatus() == NetConstant.RESULT_SUCCESS;
                        }
                    });
        } else if (isPhone) {
            observable = GeneralService.sendSMS(account)
                    .map(new Function<SendSMSResponse, Boolean>() {
                        @Override
                        public Boolean apply(SendSMSResponse response) {
                            return response.getStatus() == NetConstant.RESULT_SUCCESS;
                        }
                    });
        } else {
            isRunning = false;
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean success) {
                        isRunning = false;
                        if (success) {
                            setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
                        } else {
                            onError("发送失败，可以再试试看");
                        }
                    }

                    @Override
                    protected void onError(String message) {
                        super.onError(message);
                        setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
                        isRunning = false;
                    }
                });
    }

    private void login() {
        if (isRunning) return;
        isRunning = true;

        String account = binding.etAccount.getText().toString();
        String code = binding.etCode.getText().toString();

        boolean isEmail = StringUtils.isValidEmail(account);
        boolean isPhone = StringUtils.isValidPhone(account);
        if (!isEmail && !isPhone) {
            binding.etAccount.setFocusable(true);
            binding.etAccount.setFocusableInTouchMode(true);
            binding.etAccount.requestFocus();
            KeyBoardAction.showSoftInput(activity);
            Toast.makeText(activity, "请输入手机号或邮箱", Toast.LENGTH_SHORT).show();
            isRunning = false;
            return;
        }

        if (StringUtils.isBlank(code)) {
            binding.etCode.setFocusable(true);
            binding.etCode.setFocusableInTouchMode(true);
            binding.etCode.requestFocus();
            KeyBoardAction.showSoftInput(activity);
            Toast.makeText(activity, "请输入验证码", Toast.LENGTH_SHORT).show();
            isRunning = false;
            return;
        }

        binding.btnLogin.setText("登 录 中...");

        Observable<UserLoginAndRegisterResponse> observable = null;
        if (isEmail) {
            observable = UserService.userLoginAndReg(account, null, code);
        } else if (isPhone) {
            observable = UserService.userLoginAndReg(null, account, code);
        } else {
            isRunning = false;
        }

        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RXDefaultObserver<UserLoginAndRegisterResponse>(true) {
                    @Override
                    protected void onSuccess(UserLoginAndRegisterResponse response) {
                        UserSP.setUserInfo(response.getUserInfo());
                        UITransfer.toWelcomeActivity(activity);
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

}
