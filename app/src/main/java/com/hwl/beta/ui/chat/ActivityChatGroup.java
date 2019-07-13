package com.hwl.beta.ui.chat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityGroupBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.adp.ChatGroupMessageAdapter;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.ui.chat.imp.ChatGroupEmotionPanelListener;
import com.hwl.beta.ui.chat.logic.ChatGroupLogic;
import com.hwl.beta.ui.chat.standard.ChatGroupStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.ebus.bean.EventChatGroupSetting;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.video.ActivityVideoPlay;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 */

public class ActivityChatGroup extends BaseActivity {

    FragmentActivity activity;
    ChatActivityGroupBinding binding;
    ChatGroupStandard chatGroupStandard;
    ChatGroupEmotionPanelListener emotionPanelListener;

    GroupInfo groupInfo;
    ChatGroupMessageAdapter messageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        chatGroupStandard = new ChatGroupLogic();

        groupInfo = chatGroupStandard.getChatGroupInfo(getIntent().getStringExtra("groupguid"));
        if (groupInfo == null) {
            Toast.makeText(activity, "群组不存在或者已经被解散了", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (groupInfo.getIsDismiss()) {
            Toast.makeText(activity, "已经被解散群组不能发送消息", Toast.LENGTH_SHORT).show();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.chat_activity_group);

        initView();
        initEmotionPanel();
    }

    private void initView() {
        binding.tbTitle.setTitle(groupInfo.getGroupName())
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toChatGroupSettingActivity(activity, groupInfo.getGroupGuid());
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        messageAdapter = new ChatGroupMessageAdapter(activity, chatGroupStandard
                .getTopLocalMessages(groupInfo.getGroupGuid()), new ChatMessageItemListener());
        binding.rvMessageContainer.setAdapter(messageAdapter);
        binding.rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
        binding.rvMessageContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    binding.edpEmotion.reset();
                }
                return false;
            }
        });


        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setEnableScrollContentWhenLoaded(false);
        binding.refreshLayout.setEnableScrollContentWhenRefreshed(false);
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadMessages();
            }
        });
    }

    private void initEmotionPanel() {
        emotionPanelListener = new ChatGroupEmotionPanelListener(activity, groupInfo);
        binding.edpEmotion.setEmotionPanelListener(emotionPanelListener);
        binding.edpEmotion.setOnHeightChanged(new Runnable() {
            @Override
            public void run() {
                binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

    private void loadMessages() {
        chatGroupStandard.loadLocalMessages(groupInfo.getGroupGuid(), messageAdapter
                .getMinMessageId(), new
                DefaultCallback<List<ChatGroupMessage>, String>() {
                    @Override
                    public void success(List<ChatGroupMessage> msgs) {
                        if (msgs != null && msgs.size() > 0) {
                            messageAdapter.addMessages(msgs);
                        } else {
                            binding.refreshLayout.setEnableRefresh(false);
                        }
                        binding.refreshLayout.finishRefresh(true);
                    }

                    @Override
                    public void error(String errorMessage) {
                        binding.refreshLayout.finishRefresh(false);
                    }
                });
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void receiveEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_CHAT_GROUP_MESSAGE_UPDATE:
                messageAdapter.updateMessage((ChatGroupMessage) messageModel.getMessageModel());
                binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount()
                        - 1);
                break;
            case EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_CLEAR:
            case EventBusConstant.EB_TYPE_GROUP_ACTION_DELETE:
                finish();
                break;
            case EventBusConstant.EB_TYPE_CHAT_GROUP_NAME_SETTING:
                EventChatGroupSetting groupSetting = (EventChatGroupSetting) messageModel
                        .getMessageModel();
                if (!groupSetting.getGroupGuid().equals(groupInfo.getGroupGuid())) return;
                groupInfo.setGroupName(groupSetting.getGroupName());
                binding.tbTitle.setTitle(groupInfo.getGroupName());
                break;
            case EventBusConstant.EB_TYPE_GROUP_ACTION_DISMISS:
                if (messageModel.getMessageModel().equals(groupInfo.getGroupGuid()))
                    groupInfo.setIsDismiss(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList(
                            "selectimages");
                    for (int i = 0; i < list.size(); i++) {
                        emotionPanelListener.sendChatGroupImageMessage(list.get(i).getPath());
                    }
                    break;
                case 2:
                    emotionPanelListener.sendChatGroupImageMessage();
                    break;
                case 3:
                    emotionPanelListener.sendChatGroupVideoMessage(data.getStringExtra("videopath"
                    ));
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatGroupStandard.clearRecordMessageCount(groupInfo.getGroupGuid());
        emotionPanelListener.stopAudio();
    }

    public class ChatMessageItemListener implements IChatMessageItemListener {
        @Override
        public void onHeadImageClick(int position) {
            ChatGroupMessage message = messageAdapter.getChatGroupMessage(position);
            UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message
                    .getFromUserName(), message.getFromUserHeadImage());
        }

        @Override
        public boolean onChatItemLongClick(View view, final int position) {
            PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_chat_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    ChatGroupMessage message = messageAdapter.getChatGroupMessage(position);
                    switch (item.getItemId()) {
                        case R.id.pop_copy:
                            ClipboardAction.copy(activity, message.getContent());
                            break;
                        case R.id.pop_forward:
                            Toast.makeText(activity, "转发功能稍后开放...", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.pop_delete:
                            if (DaoUtils.getChatGroupMessageManagerInstance().deleteMessage(message)) {
                                messageAdapter.deleteMessage(position);
                            }
                            break;
                        case R.id.pop_favourite:
                            Toast.makeText(activity, "收藏功能稍后开放...", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });
            popup.show();
            return true;
        }

        @Override
        public void onImageItemClick(int position) {
            List<String> images = new ArrayList<>();
            int imagePosition = messageAdapter.getCurrentImageIndex(position, images);
            UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
                    imagePosition, images);
        }

        @Override
        public void onVideoItemClick(int position) {
            ChatGroupMessage message = messageAdapter.getChatGroupMessage(position);
            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null, message
                    .getOriginalUrl());
            UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
        }

        @Override
        public void onAudioItemClick(View view, int position) {
            ChatGroupMessage message = messageAdapter.getChatGroupMessage(position);
            emotionPanelListener.playAudio((ImageView) view.findViewById(R.id.iv_audio),
                    message);
        }

        @Override
        public void onFaildStatusClick(final View view, final int position) {
            new AlertDialog.Builder(activity)
                    .setMessage("重新发送")
                    .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            view.setVisibility(View.GONE);
                            emotionPanelListener.resendMessage(messageAdapter.getChatGroupMessage
                                    (position));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
