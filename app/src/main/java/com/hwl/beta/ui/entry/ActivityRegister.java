package com.hwl.beta.ui.entry;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.EntryActivityRegisterBinding;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.im.common.DefaultAction;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.beta.ui.entry.action.IRegisterListener;
import com.hwl.beta.ui.entry.bean.RegisterBean;
import com.hwl.beta.ui.entry.logic.RegisterLogic;
import com.hwl.beta.ui.entry.standard.RegisterStandard;
import com.hwl.beta.ui.widget.TimeCount;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ActivityRegister extends BaseActivity {
    Activity activity;
    EntryActivityRegisterBinding binding;
    RegisterStandard registerStandard;

    TimeCount timeCount = null;
    static final int CODETIMESECONDS = 60 * 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry_activity_register);
        activity = this;
        registerStandard = new RegisterLogic();
        binding = DataBindingUtil.setContentView(this, R.layout.entry_activity_register);
        binding.setRegisterBean(registerStandard.getRegisterBean());
        binding.setAction(new RegisterListener());

        binding.tbTitle
                .setTitle("HWL注册")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                        activity.finish();
                    }
                });

        initTime();
    }

    private void setSendViewStatus(int status) {
        switch (status) {
            case NetConstant.SEND_STATUS_PROGRESSING:
                binding.btnCodeSend.setClickable(false);
                binding.btnCodeSend.setText("正在发送...");
                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
                        .color_b2b2b2));
                break;
            case NetConstant.SEND_STATUS_SUCCESS:
                timeCount.start();
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

    private void initTime() {
        timeCount = new TimeCount(CODETIMESECONDS * 1000, 1000, binding.btnCodeSend, new
                TimeCount.TimeCountInterface() {
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
                        binding.btnCodeSend.setText(millisUntilFinished / 1000 + "秒后再试");
                    }
                });
    }

    private class RegisterListener implements IRegisterListener {
        boolean isRuning = false;
        RegisterBean registerBean = registerStandard.getRegisterBean();

        @Override
        public void onRegisterClick() {
            if (isRuning) {
                return;
            }
            isRuning = true;
            binding.btnRegister.setText("注 册 中...");

            if (!registerBean.checkParams()) {
                isRuning = false;
                binding.btnRegister.setText("注   册");
                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
                return;
            }

            registerStandard.userRegister(registerBean, new DefaultAction() {
                @Override
                public void run() {
                    Toast.makeText(activity, "注册成功 ！", Toast.LENGTH_LONG).show();
                    UITransfer.toLoginActivity(activity);
                    finish();
                }
            }, new DefaultConsumer<String>() {
                @Override
                public void accept(String s) {
                    isRuning = false;
                    binding.btnRegister.setText("注   册");
                }
            });
        }

        @Override
        public void onCheckCodeClick() {
            if (!registerBean.checkAccountAndCode()) {
                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
                return;
            }

            setSendViewStatus(NetConstant.SEND_STATUS_PROGRESSING);
            registerStandard.sendCode(registerBean, new DefaultAction() {
                @Override
                public void run() {
                    setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
                }
            }, new DefaultConsumer<String>() {
                @Override
                public void accept(String s) {
                    setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
                }
            });
        }
    }
}
