//package com.hwl.beta.ui.chat;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.databinding.ActivityChatGroupSettingEditBinding;
//import com.hwl.beta.db.DaoUtils;
//import com.hwl.beta.mq.send.GroupActionMessageSend;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.net.group.GroupService;
//import com.hwl.beta.net.group.body.SetGroupNameResponse;
//import com.hwl.beta.net.group.body.SetGroupNoteResponse;
//import com.hwl.beta.sp.UserPosSP;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
//import com.hwl.beta.ui.dialog.LoadingDialog;
//import com.hwl.beta.utils.StringUtils;
//
//public class ActivityChatGroupSettingEdit extends BaseActivity {
//    Activity activity;
//    ActivityChatGroupSettingEditBinding binding;
//    String groupGuid;
//    int editType;
//    String content;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = this;
//
//        binding = DataBindingUtil.setContentView(activity, R.layout.activity_chat_group_setting_edit);
//
//        initView();
//    }
//
//    private void initView() {
//        binding.tbTitle
//                .setTitleRightShow()
//                .setTitleRightText("提交")
//                .setImageRightHide()
//                .setTitleRightBackground(R.drawable.bg_top)
//                .setTitleRightClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        switch (editType) {
//                            case 1:
//                                setGroupNote();
//                                break;
//                            case 2:
//                                setGroupName();
//                                break;
//                            case 3:
//                                setGrouMyName();
//                                break;
//                        }
//                    }
//                })
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//        editType = getIntent().getIntExtra("edittype", 0);
//        groupGuid = getIntent().getStringExtra("groupguid");
//        content = getIntent().getStringExtra("content");
//
//        switch (editType) {
//            case 1:
//                binding.tbTitle.setTitle("设置群组公告");
//                binding.etGroupNote.setText(content);
//                binding.etGroupNote.setVisibility(View.VISIBLE);
//                binding.etContent.setVisibility(View.GONE);
//                break;
//            case 2:
//                binding.tbTitle.setTitle("设置群组名称");
//                binding.etContent.setText(content);
//                binding.etGroupNote.setVisibility(View.GONE);
//                binding.etContent.setVisibility(View.VISIBLE);
//                break;
//            case 3:
//                binding.tbTitle.setTitle("设置我在群的昵称");
//                binding.etContent.setText(content);
//                binding.etGroupNote.setVisibility(View.GONE);
//                binding.etContent.setVisibility(View.VISIBLE);
//                break;
//        }
//    }
//
//    private void setGrouMyName() {
//        if (content == null) {
//            content = "";
//        }
//        if (content.equals(binding.etContent.getText()+ "")) {
//            finish();
//            return;
//        }
//        content = binding.etContent.getText() + "";
//        DaoUtils.getGroupInfoManagerInstance().setGroupMyName(groupGuid, content);
//        DaoUtils.getGroupUserInfoManagerInstance().setUserName(groupGuid, UserSP.getUserId(), content);
//        Intent intent = new Intent();
//        intent.putExtra("content", content);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }
//
//    private void setGroupNote() {
//        if (content == null) {
//            content = "";
//        }
//        if (content.equals(binding.etGroupNote.getText()+ "")) {
//            finish();
//            return;
//        }
//        content = binding.etGroupNote.getText() + "";
//        if (UserPosSP.getGroupGuid().equals(groupGuid)) {
//            sendEditGroupNoteMessage();
//            return;
//        }
//        LoadingDialog.show(activity);
//        //调用server api
//        //成功后保存到本地
//        //发送MQ通知群组人
//        GroupService.setGroupNote(groupGuid, content)
//                .subscribe(new NetDefaultObserver<SetGroupNoteResponse>() {
//                    @Override
//                    protected void onSuccess(SetGroupNoteResponse response) {
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            sendEditGroupNoteMessage();
//                        } else {
//                            onError("修改群组公告失败");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        LoadingDialog.hide();
//                    }
//                });
//    }
//
//    private void sendEditGroupNoteMessage() {
//        DaoUtils.getGroupInfoManagerInstance().setGroupNote(groupGuid, content);
//        GroupActionMessageSend.sendEditGroupNoteMessage(groupGuid, content, UserSP.getUserName() + " 修改了群公告").subscribe();
//        Intent intent = new Intent();
//        intent.putExtra("content", content);
//        setResult(Activity.RESULT_OK, intent);
//        finish();
//    }
//
//    private void setGroupName() {
//        if (content == null) {
//            content = "";
//        }
//        if (content.equals(binding.etContent.getText()+ "")) {
//            finish();
//            return;
//        }
//        content = binding.etContent.getText() + "";
//        if (StringUtils.isBlank(content)) {
//            Toast.makeText(activity, "群组名称不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //如果修改的是系统群，则不用户调用api
//        if (UserPosSP.getGroupGuid().equals(groupGuid)) {
//            Toast.makeText(activity, "系统群组名称不能修改", Toast.LENGTH_SHORT).show();
//            //sendEditGroupNameMessage();
//            return;
//        }
//        LoadingDialog.show(activity);
//        //调用server api
//        //成功后保存到本地
//        //发送MQ通知群组人
//        GroupService.setGroupName(groupGuid, content)
//                .subscribe(new NetDefaultObserver<SetGroupNameResponse>() {
//                    @Override
//                    protected void onSuccess(SetGroupNameResponse response) {
//                        LoadingDialog.hide();
//                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
//                            DaoUtils.getGroupInfoManagerInstance().setGroupName(groupGuid, content);
//                            GroupActionMessageSend.sendEditGroupNameMessage(groupGuid, content, UserSP.getUserName() + " 修改了群名称").subscribe();
//                            Intent intent = new Intent();
//                            intent.putExtra("content", content);
//                            setResult(Activity.RESULT_OK, intent);
//                            finish();
//                        } else {
//                            onError("修改群名称失败");
//                        }
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        LoadingDialog.hide();
//                    }
//                });
//    }
//}
