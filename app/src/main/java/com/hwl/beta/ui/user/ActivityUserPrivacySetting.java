//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserPrivacySettingBinding;
//import com.hwl.beta.sp.UserSettingSP;
//import com.hwl.beta.ui.common.BaseActivity;
//
//public class ActivityUserPrivacySetting extends BaseActivity {
//    Activity activity;
//    ActivityUserPrivacySettingBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_privacy_setting);
//
//        initView();
//    }
//
//    private void initView() {
//        binding.tbTitle.setTitle("隐私设置")
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                })
//                .setImageRightHide();
//
////        binding.switchHideUserheadimage.setChecked(UserSettingSP.getPrivacySettingHideUserheadimage());
//        binding.switchRejectChat.setChecked(UserSettingSP.getPrivacySettingRejectChat());
//
//        binding.rlBlackUsers.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(activity, "黑名单查看功能稍后开放...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        binding.switchHideUserheadimage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                UserSettingSP.setPrivaySettingHideUserheadimage(isChecked);
////            }
////        });
//
//        binding.switchRejectChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                UserSettingSP.setPrivaySettingRejectChat(isChecked);
//            }
//        });
//    }
//}
