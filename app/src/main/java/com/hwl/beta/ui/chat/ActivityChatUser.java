package com.hwl.beta.ui.chat;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityUserBinding;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.adp.ChatUserMessageAdapter;
import com.hwl.beta.ui.chat.logic.ChatUserLogic;
import com.hwl.beta.ui.chat.standard.ChatUserStandard;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.DefaultCallback;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * Created by Administrator on 2017/12/31.
 */

public class ActivityChatUser extends BaseActivity {

    FragmentActivity activity;
    ChatActivityUserBinding binding;
    ChatUserStandard chatUserStandard;
    ChatUserMessageAdapter messageAdapter;
    //   ChatUserEmotionPannelListener emotionPannelListener;
    Friend user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void initView() {
        chatUserStandard.clearRecordMessageCount(user.getId());

        binding.tbTitle.setTitle(user.getShowName())
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //    UITransfer.toChatUserSettingActivity(activity, user.getId(), user
                        // .getShowName(), user.getHeadImage());
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        //    emotionPannelListener = new ChatUserEmotionPannelListener(activity, user);
        //    final EmotionControlPannel ecpEmotion = findViewById(R.id.ecp_emotion);
        //    ecpEmotion.setEmotionPannelListener(emotionPannelListener);

        messageAdapter = new ChatUserMessageAdapter(activity, chatUserStandard.getTopLocalMessages(user.getId()), new ChatMessageItemListener());
        binding.rvMessageContainer.setAdapter(messageAdapter);
        binding.rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
//        binding.rvMessageContainer.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            ecpEmotion.hideEmotionFunction();
//                            break;
//                    }
//                    return false;
//                }
//            });

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

    private void loadMessages() {
        chatUserStandard.loadLocalMessages(user.getId(), messageAdapter.getMinMessageId(), new DefaultCallback<List<ChatUserMessage>, String>() {
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateMessage(ChatUserMessage message) {
//        if (message == null) return;
//        if (message.getFromUserId() != user.getId() && message.getFromUserId() != myUserId)
// return;
// //        checkFriendInfo(message);
//        messageAdapter.addMessage(message);
//        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0 || remark.getFriendId() != user.getId())
//            return;

//        user.setRemark(remark.getFriendRemark());
//        tbTitle.setTitle(user.getShowName());
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateMessage(EventClearUserMessages info) {
//        if (info == null || info.getMyUserId() <= 0 || info.getViewUserId() <= 0) return;
//        if (info.getActionType() == EventBusConstant.EB_TYPE_ACTINO_CLEAR && info.getViewUserId
// () == user.getId()) {
//            messages.clear();
//            messageAdapter.notifyDataSetChanged();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList
// ("selectimages");
//                    //Toast.makeText(activity, list.size() + " 张图片！", Toast.LENGTH_SHORT).show();
//                    for (int i = 0; i < list.size(); i++) {
//                        emotionPannelListener.sendChatUserImageMessage(list.get(i).getPath());
//                    }
//                    break;
//                case 2:
//                    emotionPannelListener.sendChatUserImageMessage();
//                    break;
//                case 3:
//                    //Toast.makeText(activity, data.getStringExtra("videopath"), Toast
// .LENGTH_SHORT).show();
//                    emotionPannelListener.sendChatUserVideoMessage(data.getStringExtra
// ("videopath"));
//                    break;
//            }
//        }
//    }

    public class ChatMessageItemListener implements IChatMessageItemListener {
//        AudioPlay audioPlay;

        @Override
        public void onHeadImageClick(int position) {
            //    ChatUserMessage message = messages.get(position);
            //    UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message
            // .getFromUserName(), message.getFromUserHeadImage());
        }

        @Override
        public boolean onChatItemLongClick(View view, final int position) {
            //    final PopupMenu popup = new PopupMenu(activity, view);
            //    popup.getMenuInflater().inflate(R.menu.popup_message_menu, popup.getMenu());
            //    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            //        public boolean onMenuItemClick(MenuItem item) {
            //            ChatUserMessage message = messages.get(position);
            //            switch (item.getItemId()) {
            //                case R.id.pop_copy:
            //                    ClipboardAction.copy(activity, message.getContent());
            //                    break;
            //                case R.id.pop_send_friend:
            //                    break;
            //                case R.id.pop_delete:
            //                    if (DaoUtils.getChatUserMessageManagerInstance().deleteMessage
            // (message)) {
            //                        messages.remove(message);
            //                        messageAdapter.notifyItemRemoved(position);
            //                        messageAdapter.notifyItemRangeChanged(position, messages
            // .size() - position);
            //                    }
            //                    break;
            //                case R.id.pop_collection:
            //                    break;
            //            }
            //            return true;
            //        }
            //    });
            //    popup.show();
            return true;
        }

        @Override
        public void onImageItemClick(int position) {
            //    int imageIndex = 0;
            //    int imagePosition = 0;
            //    List<String> images = new ArrayList<>();
            //    for (int i = 0; i < messages.size(); i++) {
            //        if (messages.get(i).getContentType() == NetConstant.CIRCLE_CONTENT_IMAGE) {
            //            if (StringUtils.isNotBlank(messages.get(i).getLocalUrl())) {
            //                images.add(messages.get(i).getLocalUrl());
            //            } else if (StringUtils.isNotBlank(messages.get(i).getOriginalUrl())) {
            //                images.add(messages.get(i).getOriginalUrl());
            //            } else {
            //                images.add(messages.get(i).getPreviewUrl());
            //            }
            //            if (i == position) {
            //                imagePosition = imageIndex;
            //            }
            //            imageIndex++;
            //        }
            //    }
            //    //Toast.makeText(activity, "查看图片功能稍后开放", Toast.LENGTH_SHORT).show();
            //    UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW,
            // imagePosition, images);
        }

        @Override
        public void onVideoItemClick(int position) {
            //    ChatUserMessage message = messages.get(position);
            //    String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null,
            // message.getOriginalUrl());
            //    UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
        }

        @Override
        public void onAudioItemClick(View view, int position) {
            //    ChatUserMessage message = messages.get(position);
            //    emotionPannelListener.playAudio((ImageView) view.findViewById(R.id.iv_audio),
            // message);
        }

        @Override
        public void onFaildStatusClick(final View view, final int position) {
            //    new AlertDialog.Builder(activity)
            //            .setMessage("重新发送")
            //            .setPositiveButton("发送", new DialogInterface.OnClickListener() {
            //                @Override
            //                public void onClick(DialogInterface dialog, int which) {
            //                    view.setVisibility(View.GONE);
            //                    emotionPannelListener.resendMessage(messages.get(position));
            //                    dialog.dismiss();
            //                }
            //            })
            //            .setNegativeButton("取消", null)
            //            .show();
        }
    }
}
