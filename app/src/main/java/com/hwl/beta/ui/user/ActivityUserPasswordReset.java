//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserPasswordResetBinding;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.user.UserService;
//import com.hwl.beta.net.user.body.ResetUserPasswordResponse;
//import com.hwl.beta.sp.UserPosSP;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.utils.MD5;
//import com.hwl.beta.utils.StringUtils;
//
//public class ActivityUserPasswordReset extends BaseActivity {
//    Activity activity;
//    ActivityUserPasswordResetBinding binding;
//    boolean isRuning = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_password_reset);
//
//        initView();
//    }
//
//    private void initView() {
//        binding.tbTitle.setTitle("密码重置")
//                .setTitleRightShow()
//                .setTitleRightText("提交")
//                .setImageRightHide()
//                .setTitleRightBackground(R.drawable.bg_top)
//                .setTitleRightClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (checkParams()) {
//                            resetPassword();
//                        }
//                    }
//                })
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//    }
//
//    public boolean checkParams() {
//
//        if (StringUtils.isBlank(binding.etOldPwd.getText() + "")) {
//            Toast.makeText(activity, "旧密码不能为空", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (StringUtils.isBlank(binding.etNewPwd.getText() + "")) {
//            Toast.makeText(activity, "新密码不能为空", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        if (StringUtils.isBlank(binding.etNewPwd2.getText() + "")) {
//            Toast.makeText(activity, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (!binding.etNewPwd.getText().toString().equals(binding.etNewPwd2.getText().toString())) {
//            Toast.makeText(activity, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        return true;
//    }
//
//    private void resetPassword() {
//        if (isRuning) return;
//        isRuning = true;
//        LoadingDialog.show(activity, "密码重置中...");
//        UserService.resetUserPassword(
//                MD5.encode(binding.etOldPwd.getText() + ""),
//                MD5.encode(binding.etNewPwd.getText() + ""),
//                MD5.encode(binding.etNewPwd2.getText() + ""))
//                .subscribe(new NetDefaultObserver<ResetUserPasswordResponse>() {
//                    @Override
//                    protected void onSuccess(ResetUserPasswordResponse response) {
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            new AlertDialog.Builder(activity)
//                                    .setMessage("密码重新设置成功,你需要重新登录.")
//                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                            UserSP.clearUserInfo();
//                                            UserPosSP.clearPosInfo();
//
//                                            UITransfer.toWelcomeActivity(activity);
//                                            activity.finish();
//                                        }
//                                    })
//                                    .show();
//                        } else {
//                            onError("密码重置失败!");
//                        }
//                        isRuning = false;
//                        LoadingDialog.hide();
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        isRuning = false;
//                        LoadingDialog.hide();
//                        super.onError(resultMessage);
//                    }
//
//                    @Override
//                    protected void onRelogin() {
//                        LoadingDialog.hide();
//                        isRuning = false;
//                        UITransfer.toReloginDialog((FragmentActivity) activity);
//                    }
//                });
//
//    }
//}
