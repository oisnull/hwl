//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.view.View;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserSettingBinding;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.UITransfer;
//
//public class ActivityUserSetting extends BaseActivity {
//
//    Activity activity;
//    ActivityUserSettingBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_setting);
//
//        initView();
//    }
//
//    private void initView() {
//        binding.tbTitle.setTitle("个人设置")
//                .setImageRightHide()
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//        binding.rlPrivacySetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UITransfer.toUserPrivacySettingActivity(activity);
//            }
//        });
//        binding.rlMessageSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UITransfer.toUserMessageSettingActivity(activity);
//            }
//        });
//        binding.rlPasswordSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UITransfer.toUserPasswordResetActivity(activity);
//            }
//        });
//    }
//}
