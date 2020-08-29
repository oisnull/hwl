package com.hwl.beta.ui.chat;

import android.content.DialogInterface;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityGroupSettingBinding;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.action.IChatGroupSettingListener;
import com.hwl.beta.ui.chat.adp.ChatGroupUserAdapter;
import com.hwl.beta.ui.chat.logic.ChatGroupSettingLogic;
import com.hwl.beta.ui.chat.standard.ChatGroupSettingStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventChatGroupSetting;
import com.hwl.beta.ui.group.ActivityGroupAdd;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class ActivityChatGroupSetting extends BaseActivity {

    FragmentActivity activity;
    ChatActivityGroupSettingBinding binding;
    ChatGroupSettingStandard settingStandard;
    ChatGroupUserAdapter userAdapter;
    GroupInfo group;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        settingStandard = new ChatGroupSettingLogic();

        group = settingStandard.getGroupInfo(getIntent().getStringExtra("groupguid"));
        if (group == null) {
            Toast.makeText(activity, "群组不存在或者已经被解散了", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.chat_activity_group_setting);
        binding.setAction(new ChatGroupSettingListener());
        binding.setSetting(group);

        initView();
    }

    private void initView() {
        binding.tbTitle.setTitle("群组设置")
                .setImageRightHide()
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
        userAdapter = new ChatGroupUserAdapter(activity,
                settingStandard.getGroupUsers(group.getGroupGuid(),
                        group.getIsDismiss()));
        binding.gvUserContainer.setAdapter(userAdapter);
        binding.gvUserContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == -1) {
                    UITransfer.toGroupAddActivity(activity, ActivityGroupAdd.TYPE_ADD, group
                            .getGroupGuid());
                } else {
                    GroupUserInfo user = userAdapter.getItem(position);
                    if (user != null) {
                        UITransfer.toUserIndexActivity(activity, user.getUserId(), user
                                .getUserName(), user.getUserImage());
                    }
                }
            }
        });

        if (group.getBuildUserId() == UserSP.getUserId()) {
            binding.btnDismiss.setVisibility(View.VISIBLE);
        } else {
            binding.btnDismiss.setVisibility(View.GONE);
        }
        if (group.getGroupGuid().equals(UserPosSP.getGroupGuid())) {
            binding.btnExit.setVisibility(View.GONE);
        } else {
            binding.btnExit.setVisibility(View.VISIBLE);
        }

        this.loadUsersFromServer();
    }

    private void loadUsersFromServer() {
        if (userAdapter.getUserCount() > 0 && group.getIsLoadUser())
            return;
        settingStandard.loadGroupUsersFromServer(group.getGroupGuid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<GroupUserInfo>>() {
                    @Override
                    public void accept(List<GroupUserInfo> users) {
                        userAdapter.addUsers(users);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        if (messageModel.getMessageType() == EventBusConstant.EB_TYPE_GROUP_USERS_ADD) {
            userAdapter.addUsers((List<GroupUserInfo>) messageModel.getMessageModel());
            return;
        }

        EventChatGroupSetting groupSetting = (EventChatGroupSetting) messageModel.getMessageModel();
        if (!groupSetting.getGroupGuid().equals(group.getGroupGuid())) return;

        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_CHAT_GROUP_NOTE_SETTING:
                group.setGroupNote(groupSetting.getGroupNote());
                break;
            case EventBusConstant.EB_TYPE_CHAT_GROUP_NAME_SETTING:
                group.setGroupName(groupSetting.getGroupName());
                break;
            case EventBusConstant.EB_TYPE_CHAT_GROUP_USER_REMARK_SETTING:
                group.setMyUserName(groupSetting.getGroupUserRemark());
                break;
        }
    }

    public class ChatGroupSettingListener implements IChatGroupSettingListener {

        @Override
        public void onGroupNoteClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 1, group
                    .getGroupNote());
        }

        @Override
        public void onGroupNameClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 2, group
                    .getGroupName());
        }

        @Override
        public void onMyNameClick() {
            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 3, group
                    .getMyUserName());
        }

        @Override
        public void onShieldCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            group.setIsShield(isChecked);
            settingStandard.setShieldMessage(group);
        }

        @Override
        public void onSearchClick() {
            Toast.makeText(activity, "搜索功能稍后开放...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClearClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            settingStandard.clearMessage(group.getGroupGuid());
                            Toast.makeText(activity, "聊天记录已经清空", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        @Override
        public void onExitClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("退出群组后,会帮你清空掉所有聊天信息,确认退出 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            LoadingDialog.show(activity, "正在退出,请稍后...");
                            settingStandard.exitGroup(group.getGroupGuid(), new
                                    DefaultCallback<Boolean, String>() {
                                        @Override
                                        public void success(Boolean successMessage) {
                                            LoadingDialog.hide();
                                            finish();
                                        }

                                        @Override
                                        public void error(String errorMessage) {
                                            LoadingDialog.hide();
                                            Toast.makeText(activity, errorMessage, Toast
                                                    .LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        @Override
        public void onDismissClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("解散群组后,会删除群组相关的信息,确定要解散 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            LoadingDialog.show(activity, "群组解散中,请稍后...");
                            settingStandard.dismissGroup(group.getGroupGuid(), new
                                    DefaultCallback<Boolean, String>() {
                                        @Override
                                        public void success(Boolean successMessage) {
                                            LoadingDialog.hide();
                                            finish();
                                        }

                                        @Override
                                        public void error(String errorMessage) {
                                            LoadingDialog.hide();
                                            Toast.makeText(activity, errorMessage, Toast
                                                    .LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
