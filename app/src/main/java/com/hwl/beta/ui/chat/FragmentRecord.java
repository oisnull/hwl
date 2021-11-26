package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityUserBinding;
import com.hwl.beta.databinding.ChatFragmentRecordBinding;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.adp.RecordAdapter;
import com.hwl.beta.ui.chat.logic.RecordLogic;
import com.hwl.beta.ui.chat.standard.RecordStandard;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventChatGroupSetting;
import com.hwl.beta.ui.ebus.bean.EventUpdateFriendRemark;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.NetworkUtils;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentRecord extends BaseFragment {
    ChatFragmentRecordBinding binding;
    RecordStandard recordStandard;
    RecordAdapter recordAdapter;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        activity = getActivity();

        recordStandard = new RecordLogic();
        binding = ChatFragmentRecordBinding.inflate(inflater, container, false);

        initView();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initView() {
        recordAdapter = new RecordAdapter(activity, recordStandard.loadRecords(), new
                RecordAdapter.IAdapterListener() {
                    @Override
                    public void onLoadComplete(int messageTotalCount) {

                    }

                    @Override
                    public void onItemClick(int position) {
                        ChatRecordMessage recordMessage = recordAdapter.getRecordMessage(position);
                        switch (recordMessage.getRecordType()) {
                            case IMConstant.CHAT_RECORD_TYPE_USER:
                                if (recordMessage.getFromUserId() == UserSP.getUserId()) {
                                    UITransfer.toChatUserActivity(activity,
                                            recordMessage.getToUserId(),
                                            recordMessage.getToUserName(),
                                            recordMessage.getToUserHeadImage(),
                                            recordMessage.getRecordId());
                                } else {
                                    UITransfer.toChatUserActivity(activity,
                                            recordMessage.getFromUserId(),
                                            recordMessage.getFromUserName(),
                                            recordMessage.getFromUserHeadImage(),
                                            recordMessage.getRecordId());
                                }
                                break;
                            case IMConstant.CHAT_RECORD_TYPE_GROUP:
                                UITransfer.toChatGroupActivity(activity, recordMessage
                                        .getGroupGuid());
                                break;
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        showPopupMenu(view, position);
                    }
                });
        binding.rvRecordContainer.setAdapter(recordAdapter);
        binding.rvRecordContainer.setLayoutManager(new LinearLayoutManager(activity));

        binding.tvCheckNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.openWirelessSettings();
            }
        });

        if (NetworkUtils.isConnected()) {
            binding.llNetworkNone.setVisibility(View.GONE);
        } else {
            binding.llNetworkNone.setVisibility(View.VISIBLE);
        }
    }

    private void showPopupMenu(View view, final int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        popup.getMenuInflater().inflate(R.menu.popup_record_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                final ChatRecordMessage record = recordAdapter.getRecordMessage(position);
                switch (item.getItemId()) {
                    case R.id.pop_set_top:
                        break;
                    case R.id.pop_delete_record:
                        recordStandard.deleteRecord(record, new DefaultCallback<Boolean, String>() {
                            @Override
                            public void success(Boolean successMessage) {
                                recordAdapter.removeRecord(record.getRecordId());
                            }

                            @Override
                            public void error(String errorMessage) {
                                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.pop_delete_messages:
                        new AlertDialog.Builder(activity)
                                .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
                                .setPositiveButton("确定", new DialogInterface
                                        .OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int
                                            which) {
                                        recordStandard.clearMessages(record, new
                                                DefaultCallback<Boolean, String>() {
                                                    @Override
                                                    public void success(Boolean successMessage) {
                                                        recordAdapter.removeRecord(record.getRecordId());
                                                    }

                                                    @Override
                                                    public void error(String errorMessage) {
                                                        Toast.makeText(activity, errorMessage, Toast
                                                                .LENGTH_SHORT).show();
                                                    }
                                                });
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_UPDATE_SORT:
                recordAdapter.updateRecord((ChatRecordMessage) messageModel
                        .getMessageModel());
                break;
            case EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_UPDATE_NOSORT:
                recordAdapter.updateRecord((ChatRecordMessage) messageModel
                                .getMessageModel(),
                        false);
                break;
            case EventBusConstant.EB_TYPE_NETWORK_CONNECT_UPDATE:
                binding.llNetworkNone.setVisibility(View.GONE);
                break;
            case EventBusConstant.EB_TYPE_NETWORK_BREAK_UPDATE:
                binding.llNetworkNone.setVisibility(View.VISIBLE);
                break;
            case EventBusConstant.EB_TYPE_FRIEND_UPDATE_REMARK:
                EventUpdateFriendRemark friendRemark = (EventUpdateFriendRemark)
                        messageModel
                                .getMessageModel();
                recordAdapter.updateFriendRemark(friendRemark.getFriendId(),
                        friendRemark
                                .getFriendRemark());
                break;
            case EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_CLEAR:
                recordAdapter.removeRecord((Long) messageModel.getMessageModel());
                break;
            case EventBusConstant.EB_TYPE_CHAT_GROUP_NAME_SETTING:
                EventChatGroupSetting groupSetting = (EventChatGroupSetting)
                        messageModel
                                .getMessageModel();
                recordAdapter.updateGroupName(groupSetting.getGroupGuid(),
                        groupSetting
                                .getGroupName());
                break;
            case EventBusConstant.EB_TYPE_GROUP_ACTION_DELETE:
                recordAdapter.removeRecord((String) messageModel.getMessageModel());
                break;
            case EventBusConstant.EB_TYPE_CHAT_RECORD_GROUP_LOCATION:
                recordAdapter.updateGroupLocation(UserPosSP.getGroupGuid(), UserPosSP.getNearDesc());
                break;
        }
    }
}
