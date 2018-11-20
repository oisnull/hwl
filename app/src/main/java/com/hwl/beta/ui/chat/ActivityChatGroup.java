package com.hwl.beta.ui.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatActivityGroupBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.sp.AppInstallStatus;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.adp.ChatGroupMessageAdapter;
import com.hwl.beta.ui.chat.imp.ChatGroupEmotionPannelListener;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.ClipboardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.widget.TitleBar;
import com.hwl.beta.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/2/10.
 */

public class ActivityChatGroup extends BaseActivity {

    FragmentActivity activity;
    ChatActivityGroupBinding binding;

    SmartRefreshLayout refreshLayout;
    GroupInfo groupInfo;
    ChatRecordMessage record;
    RecyclerView rvMessageContainer;
    List<ChatGroupMessage> messages = null;
    ChatGroupMessageAdapter messageAdapter;
    ChatGroupEmotionPannelListener emotionPanelListener;
    TitleBar tbTitle;
    int pageSize = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        groupInfo = DaoUtils.getGroupInfoManagerInstance().get(getIntent().getStringExtra("groupguid"));
        if (groupInfo == null) {
            Toast.makeText(activity, "群组不存在或者已经被解散了", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (groupInfo.getIsDismiss()) {
            Toast.makeText(activity, "已经被解散群组不能发送消息", Toast.LENGTH_SHORT).show();
        }

        record = DaoUtils.getChatRecordMessageManagerInstance().getGroupRecord(groupInfo.getGroupGuid());
        if (record == null) {
            record = new ChatRecordMessage();
        }

        messages = DaoUtils.getChatGroupMessageManagerInstance().getGroupMessages(groupInfo.getGroupGuid(), 0, pageSize);
        sortMessages(messages);
        if (messages == null) {
            messages = new ArrayList<>();
        }

        binding = DataBindingUtil.setContentView(this, R.layout.chat_activity_user);

        initView();
    }

    private void initView() {
        tbTitle = findViewById(R.id.tb_title);
        tbTitle.setTitle(groupInfo.getGroupName())
                .setImageRightResource(R.drawable.ic_setting)
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        UITransfer.toChatGroupSettingActivity(activity, groupInfo.getGroupGuid());
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        emotionPanelListener = new ChatGroupEmotionPannelListener(activity, groupInfo);
        binding.ecpEmotion.setLocalSoftInputHeight(AppInstallStatus.getSoftInputHeight())
                .setContentContainerView(binding.refreshLayout)
                .setEmotionPanelListener(emotionPanelListener)
                .setOnPanelHeightChanged(new Runnable() {
                    @Override
                    public void run() {
                        binding.rvMessageContainer.scrollToPosition(messageAdapter.getItemCount()
                                - 1);
                    }
                });

        messageAdapter = new ChatGroupMessageAdapter(activity, messages, new ChatMessageItemListener());
        rvMessageContainer = findViewById(R.id.rv_message_container);
        rvMessageContainer.setAdapter(messageAdapter);
        rvMessageContainer.setLayoutManager(new LinearLayoutManager(this));
        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
        rvMessageContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        binding.ecpEmotion.hideEmotionPanel();
                        break;
                }
                return false;
            }
        });

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
//        refreshLayout.setEnableScrollContentWhenLoaded(false);
//        refreshLayout.setEnableScrollContentWhenRefreshed(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadMessages();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void loadMessages() {
        long msgId = messageAdapter.getMinMessageId();
        if (msgId <= 0) return;
        Observable.just(msgId)
                .map(new Function<Long, List<ChatGroupMessage>>() {
                    @Override
                    public List<ChatGroupMessage> apply(Long msgId) throws Exception {
                        return DaoUtils.getChatGroupMessageManagerInstance().getGroupMessages(groupInfo.getGroupGuid(), msgId, pageSize);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChatGroupMessage>>() {
                    @Override
                    public void accept(List<ChatGroupMessage> msgs) throws Exception {
                        if (msgs != null && msgs.size() > 0) {
                            sortMessages(msgs);
                            messageAdapter.addMessages(msgs);
                        } else {
                            refreshLayout.setEnableRefresh(false);
                        }
                        refreshLayout.finishRefresh(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh(false);
                    }
                });
    }

    private void sortMessages(List<ChatGroupMessage> messageList) {
        if (messageList == null || messageList.size() <= 0) return;
        Collections.sort(messageList, new Comparator<ChatGroupMessage>() {
            public int compare(ChatGroupMessage arg0, ChatGroupMessage arg1) {
                return arg0.getMsgId().compareTo(arg1.getMsgId());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    ArrayList<ImageBean> list = data.getExtras().getParcelableArrayList("selectimages");
                    //Toast.makeText(activity, list.size() + " 张图片！", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < list.size(); i++) {
                        emotionPanelListener.sendChatGroupImageMessage(list.get(i).getPath());
                    }
                    break;
                case 2:
                    emotionPanelListener.sendChatGroupImageMessage();
                    break;
                case 3:
                    emotionPanelListener.sendChatGroupVideoMessage(data.getStringExtra("videopath"));
                    break;
            }
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateGroupInfo(GroupInfo group) {
//        if (group == null || !group.getGroupGuid().equals(groupInfo.getGroupGuid())) return;
//        if (!group.getGroupName().equals(tbTitle.getTitle())) {
//            tbTitle.setTitle(group.getGroupName());
//        }
//        groupInfo = group;
//        emotionPannelListener.setGroupInfo(groupInfo);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void clearGroupMessage(EventActionGroup actionGroup) {
//        if (actionGroup == null || actionGroup.getGroupInfo() == null || !actionGroup.getGroupInfo().getGroupGuid().equals(groupInfo.getGroupGuid()))
//            return;
//        if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_CLEAR) {
//            messages.clear();
//            messageAdapter.notifyDataSetChanged();
//        } else if (actionGroup.getActionType() == EventBusConstant.EB_TYPE_ACTINO_EXIT) {
//            finish();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateGroupMessage(ChatGroupMessage message) {
//        if (message == null) return;
//        if (!message.getGroupGuid().equals(groupInfo.getGroupGuid())) return;
//        messageAdapter.addMessage(message);
//        rvMessageContainer.scrollToPosition(messageAdapter.getItemCount() - 1);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateRemark(EventUpdateFriendRemark remark) {
//        if (remark == null || remark.getFriendId() <= 0)
//            return;
//
//        messageAdapter.updateUserName(remark.getFriendId(), remark.getFriendRemark());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MediaManager.release();
//        if (record != null && record.getRecordId() != null && record.getRecordId() > 0 && record.getUnreadCount() > 0) {
//            ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance().clearUnreadCount(record.getRecordId());
//            if (recordMessage != null) {
//                EventBus.getDefault().post(recordMessage);
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    public class ChatMessageItemListener implements IChatMessageItemListener {
        @Override
        public void onHeadImageClick(int position) {
            ChatGroupMessage message = messages.get(position);
            UITransfer.toUserIndexActivity(activity, message.getFromUserId(), message.getFromUserName(), message.getFromUserHeadImage());
        }

        @Override
        public boolean onChatItemLongClick(View view, final int position) {
            PopupMenu popup = new PopupMenu(activity, view);
            popup.getMenuInflater().inflate(R.menu.popup_message_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    ChatGroupMessage message = messages.get(position);
                    switch (item.getItemId()) {
                        case R.id.pop_copy:
                            ClipboardAction.copy(activity, message.getContent());
                            break;
                        case R.id.pop_send_friend:
                            break;
                        case R.id.pop_delete:
                            if (DaoUtils.getChatGroupMessageManagerInstance().deleteMessage(message)) {
                                messages.remove(position);
                                messageAdapter.notifyItemRemoved(position);
                                messageAdapter.notifyItemRangeChanged(position, messages.size() - position);
                            }
                            break;
                        case R.id.pop_collection:
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
            int imageIndex = 0;
            int imagePosition = 0;
            List<String> images = new ArrayList<>();
            for (int i = 0; i < messages.size(); i++) {
                if (messages.get(i).getContentType() == NetConstant.CIRCLE_CONTENT_IMAGE) {
                    if (StringUtils.isNotBlank(messages.get(i).getLocalUrl())) {
                        images.add(messages.get(i).getLocalUrl());
                    } else if (StringUtils.isNotBlank(messages.get(i).getOriginalUrl())) {
                        images.add(messages.get(i).getOriginalUrl());
                    } else {
                        images.add(messages.get(i).getPreviewUrl());
                    }
                    if (i == position) {
                        imagePosition = imageIndex;
                    }
                    imageIndex++;
                }
            }
            //Toast.makeText(activity, "查看图片功能稍后开放", Toast.LENGTH_SHORT).show();
            UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, imagePosition, images);
        }

        @Override
        public void onVideoItemClick(int position) {
//            ChatGroupMessage message = messages.get(position);
//            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), null, message.getOriginalUrl());
//            UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_VIEW, showUrl);
        }

        @Override
        public void onAudioItemClick(View view, int position) {
//            ChatGroupMessage message = messages.get(position);
//            emotionPannelListener.playAudio((ImageView) view.findViewById(R.id.iv_audio), message);
        }

        @Override
        public void onFaildStatusClick(final View view, final int position) {
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
        }
    }
}
