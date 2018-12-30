package com.hwl.beta.ui.chat;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityUserSettingBinding;
import com.hwl.beta.db.entity.ChatUserSetting;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.chat.logic.ChatUserSettingLogic;
import com.hwl.beta.ui.chat.standard.ChatUserSettingStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class ActivityChatUserSetting extends BaseActivity {

    ChatActivityUserSettingBinding binding;
    FragmentActivity activity;
    ChatUserSettingStandard settingStandard;
    Friend user;
    ChatUserSetting userSetting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        settingStandard = new ChatUserSettingLogic();

        user = settingStandard.getUserInfo(getIntent().getLongExtra("userid", 0), getIntent()
                .getStringExtra("username"), getIntent().getStringExtra("userimage"));

        if (user.getId() <= 0) {
            Toast.makeText(activity, "用户参数错误", Toast.LENGTH_SHORT).show();
            finish();
        }

        userSetting = settingStandard.getChatUserSetting(user.getId());
        binding = DataBindingUtil.setContentView(activity, R.layout.chat_activity_user_setting);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("聊天设置")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        ImageViewBean.loadImage(binding.ivHeader, user.getHeadImage());
        binding.tvName.setText(user.getShowName());
        binding.switchShield.setChecked(userSetting.getIsShield());
        binding.switchShield.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userSetting.setIsShield(isChecked);
                settingStandard.setShieldMessage(userSetting);
            }
        });
        binding.rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "搜索功能稍后开放...", Toast.LENGTH_SHORT).show();
            }
        });
        binding.rlClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                settingStandard.clearMessage(user.getId());
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        binding.ivHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toUserIndexActivity(activity, user.getId(), user.getShowName(), user
                        .getHeadImage());
            }
        });
        binding.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toUserIndexActivity(activity, user.getId(), user.getShowName(), user
                        .getHeadImage());
            }
        });
    }
}
