package com.hwl.beta.ui.group;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.GroupActivityAddBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.FriendComparator;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.group.adp.GroupAddAdapter;
import com.hwl.beta.ui.group.logic.GroupAddLogic;
import com.hwl.beta.ui.group.standard.GroupAddStandard;
import com.hwl.beta.ui.widget.SideBar;

import java.util.ArrayList;
import java.util.List;

public class ActivityGroupAdd extends BaseActivity {

    public static final int TYPE_CREATE = 1;
    public static final int TYPE_ADD = 2;

    FragmentActivity activity;
    GroupActivityAddBinding binding;
    GroupAddStandard standard;
    GroupAddAdapter addAdapter;
    List<Friend> selectUsers;
    int groupActionType;
    String groupGuid;
//    boolean isRuning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        standard = new GroupAddLogic();
        groupActionType = getIntent().getIntExtra("actiontype", TYPE_CREATE);

        addAdapter = new GroupAddAdapter(activity, standard.getLocalFriends(), new
                GroupAddAdapter.IGroupAddItemListener() {
                    @Override
                    public void onCheckBoxClick(View v, Friend friend, int position) {
                        CheckBox cb = (CheckBox) v;
                        if (cb.isChecked())
                            selectUsers.add(friend);
                        else {
                            selectUsers.remove(friend);
                        }
                    }
                });

        if (groupActionType == TYPE_ADD) {
            groupGuid = getIntent().getStringExtra("groupguid");
            addAdapter.setGroupUsers(DaoUtils.getGroupUserInfoManagerInstance().getUsers
                    (groupGuid));
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.group_activity_add);
        binding.setFriendAdapter(addAdapter);

        initView();
    }

    private void initView() {
        binding.tbTitle
                .setTitleRightShow()
                .setImageRightHide()
                .setTitleRightBackground(R.drawable.bg_top)
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        if (groupActionType == TYPE_CREATE) {
            binding.tbTitle.setTitle("发起群聊")
                    .setTitleRightText("创建")
                    .setTitleRightClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createGroup();
                        }
                    });
        } else if (groupActionType == TYPE_ADD) {
            binding.tbTitle.setTitle("加入群聊")
                    .setTitleRightText("加入")
                    .setTitleRightClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addUserToGroup();
                        }
                    });
        }

        binding.lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = addAdapter.getItem(position);
                if (addAdapter.setCheckBox(view))
                    selectUsers.add(friend);
                else {
                    selectUsers.remove(friend);
                }
            }
        });
        binding.sidrbarLetter.setTextView(binding.tvLetter);
        binding.sidrbarLetter.setOnTouchingLetterChangedListener(new SideBar
                .OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                // 该字母首次出现的位置
                int position = addAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    binding.lvFriends.setSelection(position);
                }
            }
        });

        selectUsers = new ArrayList<>();
    }

    private void addUserToGroup() {
//        if (selectUsers.size() <= 0) {
//            Toast.makeText(activity, "请选择加入的用户", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        final GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
//        if (groupInfo == null) {
//            Toast.makeText(activity, "加入的群组信息错误", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (isRuning) return;
//        isRuning = true;
//
//        List<Long> userIds = new ArrayList<>(selectUsers.size());
//        List<String> userImages = new ArrayList<>(9);
//        final List<MQGroupUserInfo> userInfos = new ArrayList<>(selectUsers.size());
//        String messageContent = groupInfo.getMyUserName() + " 邀请";
//        for (int i = 0; i < selectUsers.size(); i++) {
//            Friend user = selectUsers.get(i);
//            if (i <= 8) {
//                userImages.add(user.getHeadImage());
//            }
//            userIds.add(user.getId());
//            userInfos.add(new MQGroupUserInfo(user.getId(), user.getName(), user.getHeadImage()));
//            messageContent += " " + user.getName() + ",";
//        }
//        messageContent += " 加入群组 ";
//        LoadingDialog.show(activity);
//        final String finalMessageContent = messageContent;
//        GroupService.addGroupUsers(groupInfo.getGroupGuid(), userIds)
//                .subscribe(new NetDefaultObserver<AddGroupUsersResponse>() {
//                    @Override
//                    protected void onSuccess(AddGroupUsersResponse response) {
//                        LoadingDialog.hide();
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            GroupActionMessageSend.sendAddGroupUsersMessage(groupInfo,
// finalMessageContent, userInfos).subscribe();
//                            finish();
//                        } else {
//                            onError("加入群组失败");
//                        }
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        LoadingDialog.hide();
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onRelogin() {
//                        LoadingDialog.hide();
//                        isRuning = false;
//                        UITransfer.toReloginDialog(activity);
//                    }
//                });
    }

    private void createGroup() {
        LoadingDialog.show(activity);
        standard.createGroup(selectUsers, new DefaultCallback<Boolean, String>() {
            @Override
            public void success(Boolean successMessage) {
                LoadingDialog.hide();
                Toast.makeText(activity, "群组创建成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void error(String errorMessage) {
                LoadingDialog.hide();
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void relogin() {
                super.relogin();
                LoadingDialog.hide();
                UITransfer.toReloginDialog(activity);
            }
        });
//        if (selectUsers.size() <= 0) {
//            Toast.makeText(activity, "请选择群聊的用户", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (isRuning) return;
//        isRuning = true;
//        List<Long> userIds = new ArrayList<>(selectUsers.size());
//        List<String> userImages = new ArrayList<>(9);
//        final List<MQGroupUserInfo> userInfos = new ArrayList<>(selectUsers.size());
//        String groupName = UserSP.getUserName();
//        for (int i = 0; i < selectUsers.size(); i++) {
//            Friend user = selectUsers.get(i);
//            if (i <= 8) {
//                userImages.add(user.getHeadImage());
//            }
//            userIds.add(user.getId());
//            userInfos.add(new MQGroupUserInfo(user.getId(), user.getName(), user.getHeadImage()));
//            groupName += "," + user.getName();
//        }
//        userIds.add(UserSP.getUserId());
//        userImages.add(UserSP.getUserHeadImage());
//        userInfos.add(new MQGroupUserInfo(UserSP.getUserId(), UserSP.getUserName(), UserSP
// .getUserHeadImage()));
//
//        final GroupInfo groupInfo = DBGroupAction.convertToGroupInfo("", groupName, "", UserSP
// .getUserId(), selectUsers.size(), userImages, null);
//        LoadingDialog.show(activity);
//        GroupService.addGroup(groupName, userIds)
//                .subscribe(new NetDefaultObserver<AddGroupResponse>() {
//                    @Override
//                    protected void onSuccess(AddGroupResponse response) {
//                        LoadingDialog.hide();
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            groupInfo.setGroupGuid(response.getGroupGuid());
//                            groupInfo.setBuildTime(response.getBuildTime());
//                            GroupActionMessageSend.sendCreateMessage(groupInfo.getGroupGuid(),
// groupInfo.getGroupName(), groupInfo.getUserImages(), response.getBuildTime(), groupInfo
// .getGroupName() + " 加入聊天群", userInfos).subscribe();
//                            EventBus.getDefault().post(new EventActionGroup(EventBusConstant
// .EB_TYPE_ACTINO_ADD, groupInfo));
//                            finish();
//                        } else {
//                            onError("创建群组失败");
//                        }
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        LoadingDialog.hide();
//                        isRuning = false;
//                    }
//
//                    @Override
//                    protected void onRelogin() {
//                        LoadingDialog.hide();
//                        isRuning = false;
//                        UITransfer.toReloginDialog(activity);
//                    }
//                });
    }
}
