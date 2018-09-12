//package com.hwl.beta.ui.chat;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.CompoundButton;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityChatGroupSettingBinding;
//import com.hwl.beta.db.DaoUtils;
//import com.hwl.beta.db.entity.ChatRecordMessage;
//import com.hwl.beta.db.entity.GroupInfo;
//import com.hwl.beta.db.entity.GroupUserInfo;
//import com.hwl.beta.mq.send.GroupActionMessageSend;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.group.GroupService;
//import com.hwl.beta.net.group.body.DeleteGroupResponse;
//import com.hwl.beta.net.group.body.DeleteGroupUserResponse;
//import com.hwl.beta.net.group.body.GroupUsersResponse;
//import com.hwl.beta.sp.UserPosSP;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.busbean.EventActionChatRecord;
//import com.hwl.beta.ui.busbean.EventActionGroup;
//import com.hwl.beta.ui.busbean.EventBusConstant;
//import com.hwl.beta.ui.busbean.EventUpdateFriendRemark;
//import com.hwl.beta.ui.chat.action.IChatGroupSettingListener;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.common.rxext.MQDefaultObserver;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.convert.DBGroupAction;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.ui.group.ActivityGroupAdd;
//import com.hwl.beta.ui.group.adp.GroupUserAdapter;
//import com.hwl.beta.utils.StringUtils;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//
//public class ActivityChatGroupSetting extends BaseActivity {
//
//    Activity activity;
//    ActivityChatGroupSettingBinding binding;
//    List<GroupUserInfo> users;
//    GroupUserAdapter userAdapter;
//    GroupInfo group;
//    long myUserId;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//
//        group = DaoUtils.getGroupInfoManagerInstance().get(getIntent().getStringExtra("groupguid"));
//        if (group == null) {
//            Toast.makeText(activity, "群组不存在或者已经被解散了", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//        if (StringUtils.isBlank(group.getMyUserName())) {
//            group.setMyUserName(UserSP.getUserName());
//        }
//
//        myUserId = UserSP.getUserId();
//        users = DaoUtils.getGroupUserInfoManagerInstance().getUsers(group.getGroupGuid());
//        if (users == null) {
//            users = new ArrayList<>();
//        }
//        addUserItem();
//
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_chat_group_setting);
//        binding.setAction(new ChatGroupSettingListener());
//        binding.setSetting(group);
//        initView();
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateGroupUser(List<GroupUserInfo> userInfos) {
//        if (userInfos == null || userInfos.size() <= 0) return;
//
//        int addCount = 0;
//        for (int i = 0; i < userInfos.size(); i++) {
//            if (!users.contains(userInfos.get(i))) {
//                users.add(userInfos.get(i));
//                addCount++;
//            }
//        }
//        if (addCount > 0) {
//            removeUserItem();
//            addUserItem();
//            userAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0)
//            return;
//        for (int i = 0; i < users.size(); i++) {
//            if (remark.getFriendId() == users.get(i).getId()) {
//                users.get(i).setUserName(remark.getFriendRemark());
//                userAdapter.notifyDataSetChanged();
//                break;
//            }
//        }
//    }
//
//    private void addUserItem() {
//        if (group.getIsDismiss() || group.getGroupGuid().equals(UserPosSP.getGroupGuid())) return;
//        GroupUserInfo groupUserInfo = new GroupUserInfo();
//        groupUserInfo.setId((long) -1);
//        users.add(users.size(), groupUserInfo);
//    }
//
//    private void removeUserItem() {
//        if (group.getIsDismiss() || group.getGroupGuid().equals(UserPosSP.getGroupGuid())) return;
//        for (int i = 0; i < users.size(); i++) {
//            if (users.get(i).getId() == -1) {
//                users.remove(i);
//                break;
//            }
//        }
//    }
//
//    private void initView() {
//        binding.tbTitle.setTitle("群组设置")
//                .setImageRightHide()
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                        finish();
//                    }
//                });
//        userAdapter = new GroupUserAdapter(activity, users);
//        binding.gvUserContainer.setAdapter(userAdapter);
//        binding.gvUserContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (id == -1) {
//                    UITransfer.toGroupAddActivity(activity, ActivityGroupAdd.TYPE_ADD, group.getGroupGuid());
//                } else {
//                    GroupUserInfo user = users.get(position);
//                    if (user != null) {
//                        UITransfer.toUserIndexActivity(activity, user.getUserId(), user.getUserName(), user.getUserHeadImage());
//                    }
//                }
//            }
//        });
//
//        if (group.getBuildUserId() == myUserId) {
//            binding.btnDismiss.setVisibility(View.VISIBLE);
//        } else {
//            binding.btnDismiss.setVisibility(View.GONE);
//        }
//        if (group.getGroupGuid().equals(UserPosSP.getGroupGuid())) {
//            binding.btnExit.setVisibility(View.GONE);
//        } else {
//            binding.btnExit.setVisibility(View.VISIBLE);
//        }
//
//        loadGroupUserFromServer();
//    }
//
//    private void loadGroupUserFromServer() {
//        if (users.size() > 0) return;
//        GroupService.groupUsers(group.getGroupGuid())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new NetDefaultObserver<GroupUsersResponse>() {
//                    @Override
//                    protected void onSuccess(GroupUsersResponse response) {
//                        if (response.getGroupUserInfos() != null) {
//                            removeUserItem();
//                            List<GroupUserInfo> userInfos = DBGroupAction.convertToGroupUserInfos(response.getGroupUserInfos());
//                            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfos);
//                            users.addAll(userInfos);
//                            addUserItem();
//                            userAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    group.setGroupNote(data.getStringExtra("content"));
//                    EventBus.getDefault().post(group);
//                    break;
//                case 2:
//                    group.setGroupName(data.getStringExtra("content"));
//                    EventBus.getDefault().post(group);
//                    break;
//                case 3:
//                    String myName = data.getStringExtra("content");
//                    if (group.getMyUserName().equals(myName))
//                        return;
//                    group.setMyUserName(myName);
//                    for (int i = 0; i < users.size(); i++) {
//                        if (users.get(i).getUserId() == myUserId) {
//                            users.get(i).setUserName(group.getMyUserName());
//                            userAdapter.notifyDataSetChanged();
//                            EventBus.getDefault().post(group);
//                            break;
//                        }
//                    }
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    public class ChatGroupSettingListener implements IChatGroupSettingListener {
//
//        @Override
//        public void onGroupNoteClick() {
//            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 1, group.getGroupNote());
//        }
//
//        @Override
//        public void onGroupNameClick() {
//            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 2, group.getGroupName());
//        }
//
//        @Override
//        public void onMyNameClick() {
//            UITransfer.toChatGroupSettingEditActivity(activity, group.getGroupGuid(), 3, group.getMyUserName());
//        }
//
//        @Override
//        public void onShieldCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            group.setIsShield(isChecked);
//            DaoUtils.getGroupInfoManagerInstance().add(group);
//            ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().getGroupRecord(group.getGroupGuid());
//            if (recordMessage != null) {
//                recordMessage.setIsShield(group.getIsShield());
//                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_CHAT_RECORD_UPDATE_SHIELD, recordMessage));
//            }
//        }
//
//        @Override
//        public void onSearchClick() {
//            Toast.makeText(activity, "搜索功能稍后开放...", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onClearClick() {
//            new AlertDialog.Builder(activity)
//                    .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(group.getGroupGuid());
//                            EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_CLEAR, group));
//                            Toast.makeText(activity, "聊天记录已经清空", Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//
//        @Override
//        public void onExitClick() {
//            new AlertDialog.Builder(activity)
//                    .setMessage("退出群组后,会帮你清空掉所有聊天信息,确认退出 ?")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//
//                            LoadingDialog.show(activity, "正在退出,请稍后...");
//                            GroupService.deleteGroupUser(group.getGroupGuid())
//                                    .subscribe(new NetDefaultObserver<DeleteGroupUserResponse>() {
//                                        @Override
//                                        protected void onSuccess(DeleteGroupUserResponse response) {
//                                            LoadingDialog.hide();
//                                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                                                GroupActionMessageSend.sendExitGroupUserMessage(group.getGroupGuid(), myUserId, group.getMyUserName() + " 退出群组").subscribe();
//
//                                                DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(group);
//                                                DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(group.getGroupGuid());
//                                                DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(group.getGroupGuid());
//                                                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().deleteGroupRecord(group.getGroupGuid());
//
//                                                EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_EXIT, group));
//                                                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_ACTINO_REMOVE, recordMessage));
//                                                finish();
//                                            } else {
//                                                onError("退出失败");
//                                            }
//                                        }
//
//                                        @Override
//                                        protected void onError(String resultMessage) {
//                                            super.onError(resultMessage);
//                                            LoadingDialog.hide();
//                                        }
//                                    });
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//
//        @Override
//        public void onDismissClick() {
//            new AlertDialog.Builder(activity)
//                    .setMessage("确定要解散群组 ?")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//
//                            LoadingDialog.show(activity, "群组解散中,请稍后...");
//                            GroupActionMessageSend.sendDismissGroupUserMessage(group.getGroupGuid(), myUserId, group.getMyUserName() + " 解散了群组")
//                                    .subscribe(new MQDefaultObserver() {
//                                        @Override
//                                        protected void onSuccess() {
//                                            deleteGroupFromServer();
//                                        }
//
//                                        @Override
//                                        protected void onError(String resultMessage) {
//                                            super.onError(resultMessage);
//                                            LoadingDialog.hide();
//                                        }
//                                    });
//
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//
//        private void deleteGroupFromServer() {
//            GroupService.deleteGroup(group.getGroupGuid())
//                    .subscribe(new NetDefaultObserver<DeleteGroupResponse>() {
//                        @Override
//                        protected void onSuccess(DeleteGroupResponse response) {
//                            LoadingDialog.hide();
//                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//
//                                DaoUtils.getGroupInfoManagerInstance().deleteGroupInfo(group);
//                                DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(group.getGroupGuid());
//                                DaoUtils.getChatGroupMessageManagerInstance().deleteMessages(group.getGroupGuid());
//                                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().deleteGroupRecord(group.getGroupGuid());
//
//                                EventBus.getDefault().post(new EventActionGroup(EventBusConstant.EB_TYPE_ACTINO_EXIT, group));
//                                EventBus.getDefault().post(new EventActionChatRecord(EventBusConstant.EB_TYPE_ACTINO_REMOVE, recordMessage));
//                                finish();
//                            } else {
//                                onError("解散失败");
//                            }
//                        }
//
//                        @Override
//                        protected void onError(String resultMessage) {
//                            super.onError(resultMessage);
//                            LoadingDialog.hide();
//                        }
//                    });
//        }
//    }
//}
