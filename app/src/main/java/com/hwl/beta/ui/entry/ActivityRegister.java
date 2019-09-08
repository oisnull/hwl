package com.hwl.beta.ui.entry;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.EntryActivityRegisterBinding;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.entry.action.IRegisterListener;
import com.hwl.beta.ui.entry.bean.RegisterBean;
import com.hwl.beta.ui.entry.logic.RegisterLogic;
import com.hwl.beta.ui.entry.standard.RegisterStandard;
import com.hwl.beta.ui.widget.TimeCount;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/1/13.
 */

public class ActivityRegister extends BaseActivity {
    FragmentActivity activity;
    EntryActivityRegisterBinding binding;
    RegisterStandard registerStandard;
    RegisterBean registerBean;

    TimeCount timeCount = null;
    static final int CODETIMESECONDS = 60 * 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.entry_activity_register);
        activity = this;
        registerStandard = new RegisterLogic();
        registerBean = registerStandard.getRegisterBean();
        binding = DataBindingUtil.setContentView(this, R.layout.entry_activity_register);
        binding.setRegisterBean(registerBean);
        binding.setAction(new RegisterListener());

        initView();
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

    private void initView() {
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

        binding.etAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etAccount.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardAction.getLocalSoftInputHeight(activity);
                    }
                }, 800L);
            }
        });

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
        boolean isRunning = false;

        @Override
        public void onRegisterClick() {
            if (isRunning) {
                return;
            }
            isRunning = true;
            binding.btnRegister.setText("注 册 中...");

            if (!registerBean.checkParams()) {
                isRunning = false;
                binding.btnRegister.setText("注   册");
                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
                return;
            }

            registerStandard.userRegister(registerBean)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RXDefaultObserver<Boolean>() {
                        @Override
                        protected void onSuccess(Boolean success) {
                            if (success) {
                                Toast.makeText(activity, "注册成功 ！", Toast.LENGTH_LONG).show();
                                UITransfer.toLoginActivity(activity);
                                finish();
                            } else {
                                onError("注册失败 ！");
                            }
                        }

                        @Override
                        protected void onError(String message) {
                            super.onError(message);
                            isRunning = false;
                            binding.btnRegister.setText("注   册");
                        }
                    });

//            registerStandard.userRegister(registerBean, new DefaultAction() {
//                @Override
//                public void run() {
//                    Toast.makeText(activity, "注册成功 ！", Toast.LENGTH_LONG).show();
//                    UITransfer.toLoginActivity(activity);
//                    finish();
//                }
//            }, new DefaultConsumer<String>() {
//                @Override
//                public void accept(String s) {
//                    isRunning = false;
//                    binding.btnRegister.setText("注   册");
//                }
//            });
        }

        @Override
        public void onCheckCodeClick() {
            if (!registerBean.checkAccountAndCode()) {
                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
                return;
            }

            setSendViewStatus(NetConstant.SEND_STATUS_PROGRESSING);
            registerStandard.sendCode(registerBean)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RXDefaultObserver<Boolean>() {
                        @Override
                        protected void onSuccess(Boolean success) {
                            if (success)
                                setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
                            else
                                onError("发送失败！");
                        }

                        @Override
                        protected void onError(String message) {
                            super.onError(message);
                            setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
                        }
                    });
//            registerStandard.sendCode(registerBean, new DefaultAction() {
//                @Override
//                public void run() {
//                    setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
//                }
//            }, new DefaultConsumer<String>() {
//                @Override
//                public void accept(String s) {
//                    setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
//                }
//            });
        }
    }
}
