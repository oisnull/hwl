//package com.hwl.beta.ui.chat;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.Toast;
//
//import com.hwl.beta.R;
//import com.hwl.beta.db.DaoUtils;
//import com.hwl.beta.db.entity.ChatRecordMessage;
//import com.hwl.beta.db.entity.ChatUserMessage;
//import com.hwl.beta.db.entity.Friend;
//import com.hwl.beta.emotion.EmotionControlPannel;
//import com.hwl.beta.emotion.audio.AudioPlay;
//import com.hwl.beta.emotion.audio.MediaManager;
//import com.hwl.beta.net.NetConstant;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.busbean.EventBusConstant;
//import com.hwl.beta.ui.busbean.EventClearUserMessages;
//import com.hwl.beta.ui.busbean.EventUpdateFriendRemark;
//import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
//import com.hwl.beta.ui.chat.adp.ChatUserMessageAdapter;
//import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
//import com.hwl.beta.ui.chat.imp.ChatUserEmotionPannelListener;
//import com.hwl.beta.ui.common.BaseActivity;
//import com.hwl.beta.ui.common.ClipboardAction;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
//import com.hwl.beta.ui.imgselect.bean.ImageBean;
//import com.hwl.beta.ui.video.ActivityVideoPlay;
//import com.hwl.beta.ui.widget.TitleBar;
//import com.hwl.beta.utils.StringUtils;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.schedulers.Schedulers;
//
///**
// * Created by Administrator on 2017/12/31.
// */
//
//public class ActivityChatUser extends BaseActivity {
//
//    Activity activity;
//    TitleBar tbTitle;
//    SmartRefreshLayout refreshLayout;
//    List<ChatUserMessage> messages;
//    ChatUserMessageAdapter messageAdapter;
//    RecyclerView rvMessageContainer;
//    ChatUserEmotionPannelListener emotionPannelListener;
//    Friend user;
//    boolean isFriend = false;
//    int pageSize = 10;
//    long myUserId;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_user);
//        activity = this;
//
//        long userId = getIntent().getLongExtra("userid", 0);
//        String userName = getIntent().getStringExtra("username");
//        String userImage = getIntent().getStringExtra("userimage");
//        if (userId <= 0) {
//            Toast.makeText(activity, "用户不存在", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//        myUserId = UserSP.getUserId();
//        user = DaoUtils.getFriendManagerInstance().get(userId);
//        if (user == null) {
//            user = new Friend();
//            user.setId(userId);
//            user.setName(userName);
//            user.setHeadImage(userImage);
//        } else {
//            isFriend = true;
//        }
//
//        messages = DaoUtils.getChatUserMessageManagerInstance().getFromUserMessages(myUserId, user.getId(), 0, pageSize);
//        sortMessages(messages);
//        if (messages == null) {
//            messages = new ArrayList<>();
//        }
//        initView();
//
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
//    }
//
//    @SuppressLint("CheckResult")
//    private void loadMessages() {
//        long msgId = messageAdapter.getMinMessageId();
//        if (msgId <= 0) return;
//        Observable.just(msgId)
//                .map(new Function<Long, List<ChatUserMessage>>() {
//                    @Override
//                    public List<ChatUserMessage> apply(Long msgId) throws Exception {
//                        return DaoUtils.getChatUserMessageManagerInstance().getFromUserMessages(myUserId, user.getId(), msgId, pageSize);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<ChatUserMessage>>() {
//                    @Override
//                    public void accept(List<ChatUserMessage> msgs) throws Exception {
//                        if (msgs != null && msgs.size() > 0) {
//                            sortMessages(msgs);
//                            messageAdapter.addMessages(msgs);
//                        } else {
//                            refreshLayout.setEnableRefresh(false);
//                        }
//                        refreshLayout.finishRefresh(true);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        refreshLayout.finishRefresh(false);
//                    }
//                });
//    }
//
//    private void sortMessages(List<ChatUserMessage> messageList) {
//        if (messageList == null || messageList.size() <= 0) return;
//        Collections.sort(messageList, new Comparator<ChatUserMessage>() {
//            public int compare(ChatUserMessage arg0, ChatUserMessage arg1) {
//                return arg0.getMsgId().compareTo(arg1.getMsgId());
//            }
//        });
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void initView() {
//        tbTitle = findViewById(R.id.tb_title);
//        tbTitle.setTitle(user.getShowName())
//                .setImageRightResource(R.drawable.ic_setting)
//                .setImageRightClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        UITransfer.toChatUserSettingActivity(activity, user.getId(), user.getShowName(), user.getHeadImage());
//                    }
//                })
//                .setImageLeftClick(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onBackPressed();
//                    }
//                });
//
//        emotionPannelListener = new ChatUserEmotionPannelListener(activity, user);
//        final EmotionControlPannel ecpEmotion = findViewById(R.id.ecp_emotion);
//        ecpEmotion.setEmotionPannelListener(emotionPannelListener);
//
//        messageAdapter = new ChatUserMessageAdapter(activity, messages, new ChatMessageItemListener());
//        rvMessageContainer = findViewById(R.id.rv_message_container);
//        rvMessageContainer.setAdapter(messageAdapter);
//        rvMessageContainer.setLayoutManager(new LinearLayoutManager(activity));
//        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
//        rvMessageContainer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        ecpEmotion.hideEmotionFunction();
//                        break;
//                }
//                return false;
//            }
//        });
//
//        refreshLayout = findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableLoadMore(false);
//        refreshLayout.setEnableScrollContentWhenLoaded(false);
//        refreshLayout.setEnableScrollContentWhenRefreshed(false);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                loadMessages();
//            }
//        });
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MediaManager.release();
//        long currentRecordId = getIntent().getLongExtra("recordid", 0);
//        if (currentRecordId > 0) {
//            ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().clearUnreadCount(currentRecordId);
//            if (recordMessage != null) {
//                EventBus.getDefault().post(recordMessage);
//            }
//        }
//    }
//
////    private void checkFriendInfo(ChatUserMessage message) {
////        if (message == null) return;
////        //检测用户的名称和头像j是否已经更改
////        if (message.getFromUserId() == user.getId()) {
////            if (!message.getFromUserHeadImage().equals(user.getHeadImage()) || !message.getFromUserName().equals(user.getName())) {
////                user.setHeadImage(message.getFromUserHeadImage());
////                user.setName(message.getFromUserName());
////                if (isFriend) {
////                    DaoUtils.getFriendManagerInstance().save(user);
////                }
////            }
////        }
////    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateMessage(ChatUserMessage message) {
//        if (message == null) return;
//        if (message.getFromUserId() != user.getId() && message.getFromUserId() != myUserId) return;
////        checkFriendInfo(message);
//        messageAdapter.addMessage(message);
//        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0 || remark.getFriendId() != user.getId())
//            return;
//
//        user.setRemark(remark.getFriendRemark());
//        tbTitle.setTitle(user.getShowName());
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateMessage(EventClearUserMessages info) {
//        if (info == null || info.getMyUserId() <= 0 || info.getViewUserId() <= 0) return;
//        if (info.getActionType() == EventBusConstant.EB_TYPE_ACTINO_CLEAR && info.getViewUserId() == user.getId()) {
//            messages.clear();
//            messageAdapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case 1:
//                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList("selectimages");
//                    //Toast.makeText(activity, list.size() + " 张图片！", Toast.LENGTH_SHORT).show();
//                    for (int i = 0; i < list.size(); i++) {
//                        emotionPannelListener.sendChatUserImageMessage(list.get(i).getPath());
//                    }
//                    break;
//                case 2:
//                    emotionPannelListener.sendChatUserImageMessage();
//                    break;
//                case 3:
//                    //Toast.makeText(activity, data.getStringExtra("videopath"), Toast.LENGTH_SHORT).show();
//                    emotionPannelListener.sendChatUserVideoMessage(data.getStringExtra("videopath"));
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
//    public class ChatMessageItemListener implements IChatMessageItemListener {
//        AudioPlay audioPlay;
//
//        @Override
//        public void onHeadImageClick(int position) {
//            ChatUserMessage message = messages.get(position);
//            UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message.getFromUserName(), message.getFromUserHeadImage());
//        }
//
//        @Override
//        public boolean onChatItemLongClick(View view, final int position) {
//            final PopupMenu popup = new PopupMenu(activity, view);
//            popup.getMenuInflater().inflate(R.menu.popup_message_menu, popup.getMenu());
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                public boolean onMenuItemClick(MenuItem item) {
//                    ChatUserMessage message = messages.get(position);
//                    switch (item.getItemId()) {
//                        case R.id.pop_copy:
//                            ClipboardAction.copy(activity, message.getContent());
//                            break;
//                        case R.id.pop_send_friend:
//                            break;
//                        case R.id.pop_delete:
//                            if (DaoUtils.getChatUserMessageManagerInstance().deleteMessage(message)) {
//                                messages.remove(message);
//                                messageAdapter.notifyItemRemoved(position);
//                                messageAdapter.notifyItemRangeChanged(position, messages.size() - position);
//                            }
//                            break;
//                        case R.id.pop_collection:
//                            break;
//                    }
//                    return true;
//                }
//            });
//            popup.show();
//            return true;
//        }
//
//        @Override
//        public void onImageItemClick(int position) {
//            int imageIndex = 0;
//            int imagePosition = 0;
//            List<String> images = new ArrayList<>();
//            for (int i = 0; i < messages.size(); i++) {
//                if (messages.get(i).getContentType() == NetConstant.CIRCLE_CONTENT_IMAGE) {
//                    if (StringUtils.isNotBlank(messages.get(i).getLocalUrl())) {
//                        images.add(messages.get(i).getLocalUrl());
//                    } else if (StringUtils.isNotBlank(messages.get(i).getOriginalUrl())) {
//                        images.add(messages.get(i).getOriginalUrl());
//                    } else {
//                        images.add(messages.get(i).getPreviewUrl());
//                    }
//                    if (i == position) {
//                        imagePosition = imageIndex;
//                    }
//                    imageIndex++;
//                }
//            }
//            //Toast.makeText(activity, "查看图片功能稍后开放", Toast.LENGTH_SHORT).show();
//            UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, imagePosition, images);
//        }
//
//        @Override
//        public void onVideoItemClick(int position) {
//            ChatUserMessage message = messages.get(position);
//            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null, message.getOriginalUrl());
//            UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
//        }
//
//        @Override
//        public void onAudioItemClick(View view, int position) {
//            ChatUserMessage message = messages.get(position);
//            emotionPannelListener.playAudio((ImageView) view.findViewById(R.id.iv_audio), message);
//        }
//
//        @Override
//        public void onFaildStatusClick(final View view, final int position) {
//            new AlertDialog.Builder(activity)
//                    .setMessage("重新发送")
//                    .setPositiveButton("发送", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            view.setVisibility(View.GONE);
//                            emotionPannelListener.resendMessage(messages.get(position));
//                            dialog.dismiss();
//                        }
//                    })
//                    .setNegativeButton("取消", null)
//                    .show();
//        }
//    }
//}
