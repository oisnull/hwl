//package com.hwl.beta.ui.entry;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityRegisterBinding;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.general.GeneralService;
//import com.hwl.beta.net.general.body.SendEmailResponse;
//import com.hwl.beta.net.general.body.SendSMSResponse;
//import com.hwl.beta.net.user.UserService;
//import com.hwl.beta.net.user.body.UserRegisterResponse;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.entry.action.IRegisterListener;
//import com.hwl.beta.ui.entry.bean.RegisterBean;
//import com.hwl.beta.ui.widget.TimeCount;
//
///**
// * Created by Administrator on 2018/1/13.
// */
//
//public class ActivityRegister extends BaseActivity {
//    Activity activity;
//    ActivityRegisterBinding binding;
//    RegisterBean registerBean;
//    TimeCount timeCount = null;
//    static final int CODETIMESECONDS = 60 * 2;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        activity = this;
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
//        registerBean = new RegisterBean();
////        registerBean.setAccount("253621965@qq.com");
//        registerBean.setCheckCode("888888");
////        registerBean.setPassword("123456");
////        registerBean.setPasswordOK("123456");
//        binding.setRegisterBean(registerBean);
//        binding.setAction(new RegisterListener());
//
//        binding.tbTitle
//                .setTitle("HWL注册")
//                .setImageRightHide()
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//        initTime();
//    }
//
//    private void setSendViewStatus(int status) {
//        switch (status) {
//            case NetConstant.SEND_STATUS_PROGRESSING:
//                binding.btnCodeSend.setClickable(false);
//                binding.btnCodeSend.setText("正在发送...");
//                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
//                        .color_b2b2b2));
//                break;
//            case NetConstant.SEND_STATUS_SUCCESS:
//                timeCount.start();
//                break;
//            case NetConstant.SEND_STATUS_COMPLETE:
//            case NetConstant.SEND_STATUS_FAILED:
//                binding.btnCodeSend.setClickable(true);
//                binding.btnCodeSend.setText("获取验证码");
//                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
//                        .main));
//                break;
//        }
//    }
//
//    private void initTime() {
//        timeCount = new TimeCount(CODETIMESECONDS * 1000, 1000, binding.btnCodeSend, new
//                TimeCount.TimeCountInterface() {
//            @Override
//            public void onFinishViewChange(int resId) {
//                setSendViewStatus(NetConstant.SEND_STATUS_COMPLETE);
//                binding.btnCodeSend.setText("获取验证码");
//            }
//
//            @Override
//            public void onTickViewChange(long millisUntilFinished, int resId) {
//                binding.btnCodeSend.setClickable(false);
//                binding.btnCodeSend.setBackgroundColor(activity.getResources().getColor(R.color
//                        .main));
//                binding.btnCodeSend.setText(millisUntilFinished / 1000 + "秒后再试");
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        this.finish();
//    }
//
//    private class RegisterListener implements IRegisterListener {
//        boolean isRuning = false;
//
//        @Override
//        public void onRegisterClick() {
//            register();
//        }
//
//        private void register() {
//            if (isRuning) {
//                return;
//            }
//            isRuning = true;
//            binding.btnRegister.setText("注 册 中...");
//
//            if (!registerBean.checkParams()) {
//                isRuning = false;
//                binding.btnRegister.setText("注   册");
//                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String email = "";
//            String mobile = "";
//            if (registerBean.getIsEmail()) {
//                email = registerBean.getAccount();
//            } else {
//                mobile = registerBean.getAccount();
//            }
//
//            UserService.userRegister(email,
//                    mobile,
//                    registerBean.getMd5Password(),
//                    registerBean.getMd5PasswordOK(),
//                    registerBean.getCheckCode())
//                    .subscribe(new NetDefaultObserver<UserRegisterResponse>() {
//                        @Override
//                        protected void onSuccess(UserRegisterResponse response) {
//                            Toast.makeText(activity, "注册成功 ！", Toast.LENGTH_LONG).show();
//                            UITransfer.toLoginActivity(activity);
//                            finish();
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            isRuning = false;
//                            binding.btnRegister.setText("注   册");
//                        }
//                    });
//        }
//
//        @Override
//        public void onCheckCodeClick() {
//            if (!registerBean.checkAccountAndCode()) {
//                Toast.makeText(activity, registerBean.getMessageCode(), Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            setSendViewStatus(NetConstant.SEND_STATUS_PROGRESSING);
//            if (registerBean.getIsEmail()) {
//                //发送邮箱验证码
//                sendEmailCode();
//            } else {
//                //发送短信验证码
//                sendSMSCode();
//            }
//        }
//
//        private void sendEmailCode() {
//            GeneralService.sendEmail(registerBean.getAccount()).subscribe(new NetDefaultObserver<SendEmailResponse>() {
//                @Override
//                protected void onSuccess(SendEmailResponse response) {
//                    if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                        Toast.makeText(activity, "发送成功", Toast.LENGTH_SHORT).show();
//                        setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
//                    } else {
//                        setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
//                    }
//                }
//
//                @Override
//                protected void onError(String resultMessage) {
//                    super.onError(resultMessage);
//                    setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
//                }
//            });
//        }
//
//        private void sendSMSCode() {
//            GeneralService.sendSMS(registerBean.getAccount()).subscribe(new NetDefaultObserver<SendSMSResponse>() {
//                @Override
//                protected void onSuccess(SendSMSResponse response) {
//                    if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                        Toast.makeText(activity, "发送成功", Toast.LENGTH_SHORT).show();
//                        setSendViewStatus(NetConstant.SEND_STATUS_SUCCESS);
//                    } else {
//                        setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
//                    }
//                }
//
//                @Override
//                protected void onError(String resultMessage) {
//                    super.onError(resultMessage);
//                    setSendViewStatus(NetConstant.SEND_STATUS_FAILED);
//                }
//            });
//        }
//    }
//}
