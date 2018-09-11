//package com.hwl.beta.ui.user;
//
//import android.app.Activity;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.CompoundButton;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityUserMessageSettingBinding;
//import com.hwl.beta.sp.UserSettingSP;
//import com.hwl.beta.ui.common.BaseActivity;
//
//public class ActivityUserMessageSetting extends BaseActivity {
//
//    Activity activity;
//    ActivityUserMessageSettingBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_user_message_setting);
//
//        initView();
//    }
//
//    private void initView() {
//        binding.tbTitle.setTitle("消息提示设置")
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                })
//                .setImageRightHide();
//
//        boolean isChecked = UserSettingSP.getMessageNotifySettingCloseAll();
//        binding.switchAllSetting.setChecked(isChecked);
//        binding.switchShakeSetting.setChecked(UserSettingSP.getMessageNotifySettingOpenShake());
//        binding.switchSoundSetting.setChecked(UserSettingSP.getMessageNotifySettingOpenSound());
//        setViewVisibility(isChecked);
//
//        binding.switchAllSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                setViewVisibility(isChecked);
//                UserSettingSP.setMessageNotifySettingCloseAll(isChecked);
//            }
//        });
//
//        binding.switchShakeSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                UserSettingSP.setMessageNotifySettingOpenShake(isChecked);
//                setCloseAll();
//            }
//        });
//
//        binding.switchSoundSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                UserSettingSP.setMessageNotifySettingOpenSound(isChecked);
//                setCloseAll();
//            }
//        });
//    }
//
//    private void setCloseAll() {
//        if (!binding.switchSoundSetting.isChecked() && !binding.switchShakeSetting.isChecked()) {
//            binding.switchAllSetting.setChecked(true);
//        }
//    }
//
//    private void setViewVisibility(boolean isChecked) {
//        if (isChecked) {
//            binding.rlSoundSetting.setVisibility(View.GONE);
//            binding.rlShakeSetting.setVisibility(View.GONE);
//        } else {
//            binding.rlSoundSetting.setVisibility(View.VISIBLE);
//            binding.rlShakeSetting.setVisibility(View.VISIBLE);
//        }
//    }
//}
