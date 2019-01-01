package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatFragmentRecordBinding;
import com.hwl.beta.db.entity.ChatRecordMessage;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment_record, container,
                false);

        initView();

        return binding.getRoot();
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

//        recordAdapter = new RecordAdapter(activity, records, new RecordAdapter.IAdapterListener
// () {
//            @Override
//            public void onLoadComplete(int messageTotalCount) {
////                if (currMessageTotalCount == messageTotalCount) return;
////                currMessageTotalCount = messageTotalCount;
////                MessageCountSP.setChatMessageCount(messageTotalCount);
////                EventBus.getDefault().post(EventBusConstant.EB_TYPE_CHAT_MESSAGE_UPDATE);
//            }
//
//            @Override
//            public void onItemClick(int position) {
////                ChatRecordMessage record = records.get(position);
////                if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_USER) {
////                    if (record.getFromUserId() == UserSP.getUserId()) {
////                        UITransfer.toChatUserActivity(activity, record.getToUserId(), record
// .getToUserName(), record.getToUserHeadImage(), record.getRecordId());
////                    } else {
////                        UITransfer.toChatUserActivity(activity, record.getFromUserId(),
// record.getFromUserName(), record.getFromUserHeadImage(), record.getRecordId());
////                    }
////                } else if (record.getRecordType() == MQConstant.CHAT_RECORD_TYPE_GROUP) {
////                    UITransfer.toChatGroupActivity(activity, record.getGruopGuid());
////                }
//            }
//
//            @Override
//            public void onItemLongClick(View view, final int position) {
//                PopupMenu popup = new PopupMenu(activity, view);
//                popup.getMenuInflater().inflate(R.menu.popup_record_menu, popup.getMenu());
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        final ChatRecordMessage record = records.get(position);
//                        switch (item.getItemId()) {
//                            case R.id.pop_set_top:
//                                break;
//                            case R.id.pop_delete_record:
////                                if (DaoUtils.getChatRecordMessageManagerInstance()
// .deleteRecord(record)) {
////                                    records.remove(record);
////                                    recordAdapter.notifyDataSetChanged();
////                                }
//                                break;
//                            case R.id.pop_delete_messages:
//                                new AlertDialog.Builder(activity)
//                                        .setMessage("聊天数据清空后,不能恢复,确认清空 ?")
//                                        .setPositiveButton("确定", new DialogInterface
// .OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int
// which) {
////                                                if (DaoUtils
// .getChatRecordMessageManagerInstance().deleteRecord(record)) {
////                                                    boolean succ = false;
////                                                    if (record.getRecordType() == MQConstant
// .CHAT_RECORD_TYPE_GROUP) {
////                                                        succ = DaoUtils
// .getChatGroupMessageManagerInstance().deleteMessages(record.getGruopGuid());
////                                                    } else if (record.getRecordType() ==
// MQConstant.CHAT_RECORD_TYPE_USER) {
////                                                        succ = DaoUtils
// .getChatUserMessageManagerInstance().deleteUserMessages(record.getFromUserId(), record
// .getToUserId());
////                                                    }
////                                                    if (succ) {
////                                                        records.remove(record);
////                                                        recordAdapter.notifyDataSetChanged();
////                                                    }
////                                                } else {
////                                                    records.remove(record);
////                                                    recordAdapter.notifyDataSetChanged();
////                                                }
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .setNegativeButton("取消", null)
//                                        .show();
//                                break;
//                        }
//                        return true;
//                    }
//                });
//                popup.show();
//            }
//        });

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
    protected void receiveStickyEventMessage(EventMessageModel messageModel) {
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
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void deleteRecord(EventActionChatRecord actionChatRecord) {
//        if (actionChatRecord == null || actionChatRecord.getRecord() == null) return;
//        int position = records.indexOf(actionChatRecord.getRecord());
//        if (position == -1) return;
//        if (actionChatRecord.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE) {
//            records.remove(position);
//            recordAdapter.notifyItemRemoved(position);
//            recordAdapter.notifyItemRangeChanged(position, records.size() - position);
//        } else if (actionChatRecord.getActionType() == EventBusConstant
// .EB_TYPE_CHAT_RECORD_UPDATE_SHIELD) {
//            records.get(position).setIsShield(actionChatRecord.getRecord().getIsShield());
//            recordAdapter.notifyItemChanged(position);
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateGroupImage(EventActionGroup actionGroup) {
//        if (actionGroup == null || actionGroup.getGroupInfo() == null)
//            return;
//        if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_GROUP_IMAGE_UPDATE) {
//            recordAdapter.updateGroupImage(actionGroup.getGroupInfo().getGroupGuid(),
// actionGroup.getGroupInfo().getUserImages());
//        }
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0)
//            return;
//        for (int i = 0; i < records.size(); i++) {
//            if (records.get(i).getFromUserId() == remark.getFriendId() && records.get(i)
// .getRecordType() == MQConstant.CHAT_RECORD_TYPE_USER) {
//                ChatRecordMessage record = records.get(i);
//                if (!record.getFromUserName().equals(remark.getFriendRemark())) {
//                    record.setFromUserName(remark.getFriendRemark());
//                    record.setTitle(remark.getFriendRemark());
//                    recordAdapter.notifyItemChanged(i);
//                }
//            }
//        }
//    }

}
