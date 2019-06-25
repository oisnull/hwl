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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityUserBinding;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.adp.ChatUserMessageAdapter;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.ui.chat.imp.ChatUserEmotionPanelListener;
import com.hwl.beta.ui.chat.logic.ChatUserLogic;
import com.hwl.beta.ui.chat.standard.ChatUserStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusConstant;
import com.hwl.beta.ui.ebus.EventMessageModel;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.video.ActivityVideoPlay;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/31.
 */

public class ActivityChatUser extends BaseActivity {

    FragmentActivity activity;
    ChatActivityUserBinding binding;
    ChatUserStandard chatUserStandard;
    ChatUserMessageAdapter messageAdapter;
    ChatUserEmotionPanelListener emotionPanelListener;
    Friend user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN |
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        activity = this;
        chatUserStandard = new ChatUserLogic();

        user = chatUserStandard.getChatUserInfo(getIntent().getLongExtra("userid", 0), getIntent
                ().getStringExtra("username"), getIntent().getStringExtra("userimage"));
        if (user == null) {
            Toast.makeText(activity, "Chat user does not exist", Toast.LENGTH_SHORT).show();
            finish();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.chat_activity_user);

        initView();
        initEmotionPanel();
    }

    private void initView() {
        binding.tbTitle.setTitle(user.getShowName())
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toChatUserSettingActivity(activity, user.getId(), user
                                .getShowName(), user.getHeadImage());
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        messageAdapter = new ChatUserMessageAdapter(activity, chatUserStandard
                .getTopLocalMessages(user.getId()), new ChatMessageItemListener());
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
        emotionPanelListener = new ChatUserEmotionPanelListener(activity, user);
        binding.edpEmotion.setEmotionPanelListener(emotionPanelListener);
        binding.edpEmotion.setOnHeightChanged(new Runnable() {
            @Override
            public void run() {
                binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        });
    }

    private void loadMessages() {
        chatUserStandard.loadLocalMessages(user.getId(), messageAdapter.getMinMessageId(), new
                DefaultCallback<List<ChatUserMessage>, String>() {
                    @Override
                    public void success(List<ChatUserMessage> msgs) {
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
    protected void receiveStickyEventMessage(EventMessageModel messageModel) {
        switch (messageModel.getMessageType()) {
            case EventBusConstant.EB_TYPE_CHAT_USER_MESSAGE_UPDATE:
                messageAdapter.updateMessage((ChatUserMessage) messageModel.getMessageModel());
                binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount()
                        - 1);
                break;
            case EventBusConstant.EB_TYPE_CHAT_RECORD_MESSAGE_CLEAR:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList("selectimages");
                    for (int i = 0; i < list.size(); i++) {
                        emotionPanelListener.sendChatUserImageMessage(list.get(i).getPath());
                    }
                    break;
                case 2:
                    emotionPanelListener.sendChatUserImageMessage();
                    break;
                case 3:
                    emotionPanelListener.sendChatUserVideoMessage(data.getStringExtra
                            ("videopath"));
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        chatUserStandard.clearRecordMessageCount(user.getId());
        emotionPanelListener.stopAudio();
    }

    public class ChatMessageItemListener implements IChatMessageItemListener {

        @Override
        public void onHeadImageClick(int position) {
            ChatUserMessage message = messageAdapter.getChatUserMessage(position);
            UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message
                    .getFromUserName(), message.getFromUserHeadImage());
        }

        @Override
        public boolean onChatItemLongClick(View view, final int position) {
            final PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_message_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
            ChatUserMessage message = messageAdapter.getChatUserMessage(position);
                        switch (item.getItemId()) {
                            case R.id.pop_copy:
                                ClipboardAction.copy(activity, message.getContent());
                                break;
                            case R.id.pop_forward:
								Toast.makeText(activity, "转发功能稍后开放...", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.pop_delete:
                                if (DaoUtils.getChatUserMessageManagerInstance().deleteMessage(message)) {
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
			int imagePosition = messageAdapter.getCurrentImageIndex(position,images);
            UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,imagePosition, images);
        }

        @Override
        public void onVideoItemClick(int position) {
            ChatUserMessage message = messageAdapter.getChatUserMessage(position);
            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null,
                    message.getOriginalUrl());
            UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
        }

        @Override
        public void onAudioItemClick(View view, int position) {
            ChatUserMessage message = messageAdapter.getChatUserMessage(position);
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
                            emotionPanelListener.resendMessage(messageAdapter.getChatUserMessage
                                    (position));
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
