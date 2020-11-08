package com.hwl.beta.ui.chat.adp;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ChatReceivedVoiceItemBinding;
import com.hwl.beta.databinding.ChatReceivedImageItemBinding;
import com.hwl.beta.databinding.ChatReceivedMessageItemBinding;
import com.hwl.beta.databinding.ChatReceivedVideoItemBinding;
import com.hwl.beta.databinding.ChatReceivedWelcomeTipBinding;
import com.hwl.beta.databinding.ChatSendImageItemBinding;
import com.hwl.beta.databinding.ChatSendMessageItemBinding;
import com.hwl.beta.databinding.ChatSendVideoItemBinding;
import com.hwl.beta.databinding.ChatSendVoiceItemBinding;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.ui.chat.holder.ChatMessageReceivedVoiceViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageReceivedImageViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageReceivedMessageViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageReceivedVideoViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageReceivedWelcomeTipViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageSendImageViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageSendMessageViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageSendVideoViewHolder;
import com.hwl.beta.ui.chat.holder.ChatMessageSendVoiceViewHolder;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.utils.DateUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/2/10.
 */

public class ChatGroupMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final Context context;
    final LayoutInflater inflater;
    List<ChatGroupMessage> messages;
    IChatMessageItemListener itemListener;
    long myUserId = 0;

    public ChatGroupMessageAdapter(Context context, List<ChatGroupMessage> messages,
                                   IChatMessageItemListener itemListener) {
        this.context = context;
        this.messages = messages;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 100:
                return new ChatMessageSendMessageViewHolder((ChatSendMessageItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_send_message_item,
                                parent, false));
            case 101:
                return new ChatMessageSendImageViewHolder((ChatSendImageItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_send_image_item, parent,
                                false));
            case 102:
                return new ChatMessageSendVoiceViewHolder((ChatSendVoiceItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_send_voice_item, parent,
                                false));
            case 103:
                return new ChatMessageSendVideoViewHolder((ChatSendVideoItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_send_video_item, parent,
                                false));
            case 200:
                return new ChatMessageReceivedMessageViewHolder((ChatReceivedMessageItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_received_message_item,
                                parent, false));
            case 201:
                return new ChatMessageReceivedImageViewHolder((ChatReceivedImageItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_received_image_item,
                                parent, false));
            case 202:
                return new ChatMessageReceivedVoiceViewHolder((ChatReceivedVoiceItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_received_voice_item,
                                parent, false));
            case 203:
                return new ChatMessageReceivedVideoViewHolder((ChatReceivedVideoItemBinding)
                        DataBindingUtil.inflate(inflater, R.layout.chat_received_video_item,
                                parent, false));
            case 300:
                return new ChatMessageReceivedWelcomeTipViewHolder(
                        (ChatReceivedWelcomeTipBinding) DataBindingUtil.inflate(inflater, R
                                .layout.chat_received_welcome_tip, parent, false));
        }
        return null;
    }

    private void setSendStatus(int sendStatus, ProgressBar pbMessageStatus, ImageView
            ivMessageStatusFail) {
        switch (sendStatus) {
            case IMConstant.CHAT_SEND_SENDING:
                pbMessageStatus.setVisibility(View.VISIBLE);
                ivMessageStatusFail.setVisibility(View.GONE);
                break;
            case IMConstant.CHAT_SEND_FAILD:
                pbMessageStatus.setVisibility(View.GONE);
                ivMessageStatusFail.setVisibility(View.VISIBLE);
                break;
            default:
            case IMConstant.CHAT_SEND_SUCCESS:
                pbMessageStatus.setVisibility(View.GONE);
                ivMessageStatusFail.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ChatGroupMessage message = messages.get(position);
        if (holder instanceof ChatMessageSendMessageViewHolder) {
            ChatMessageSendMessageViewHolder viewHolder = (ChatMessageSendMessageViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage()), position, message.getFromUserName(), message
                    .getContent(), message.getStatusDesc());
            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus,
                    viewHolder.getItemBinding().ivMessageStatusFail);
        } else if (holder instanceof ChatMessageSendImageViewHolder) {
            ChatMessageSendImageViewHolder viewHolder = (ChatMessageSendImageViewHolder) holder;
            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message
                    .getPreviewUrl(), message.getOriginalUrl());
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage(), showUrl), null, position, message.getStatusDesc());
            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus,
                    viewHolder.getItemBinding().ivMessageStatusFail);
        } else if (holder instanceof ChatMessageSendVoiceViewHolder) {
            ChatMessageSendVoiceViewHolder viewHolder = (ChatMessageSendVoiceViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage()), position, null, message.getPlayTime(), message
                    .getStatusDesc());
            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus,
                    viewHolder.getItemBinding().ivMessageStatusFail);
        } else if (holder instanceof ChatMessageSendVideoViewHolder) {
            ChatMessageSendVideoViewHolder viewHolder = (ChatMessageSendVideoViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage(), message.getPreviewUrl()), null, position, message
                    .getStatusDesc());
            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus,
                    viewHolder.getItemBinding().ivMessageStatusFail);
        } else if (holder instanceof ChatMessageReceivedMessageViewHolder) {
            ChatMessageReceivedMessageViewHolder viewHolder =
                    (ChatMessageReceivedMessageViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage()), position, message.getContent(), message
                    .getFromUserName(), DateUtils.getChatShowTime(message.getSendTime()));
        } else if (holder instanceof ChatMessageReceivedImageViewHolder) {
            ChatMessageReceivedImageViewHolder viewHolder = (ChatMessageReceivedImageViewHolder)
                    holder;
            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message
                    .getPreviewUrl(), message.getOriginalUrl());
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                            .getFromUserHeadImage(), showUrl), position, message.getFromUserName(),
                    DateUtils.getChatShowTime(message.getSendTime()));
        } else if (holder instanceof ChatMessageReceivedVoiceViewHolder) {
            ChatMessageReceivedVoiceViewHolder viewHolder = (ChatMessageReceivedVoiceViewHolder)
                    holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage()), position, message.getPlayTime(), message
                    .getFromUserName(), DateUtils.getChatShowTime(message.getSendTime()));
        } else if (holder instanceof ChatMessageReceivedVideoViewHolder) {
            ChatMessageReceivedVideoViewHolder viewHolder = (ChatMessageReceivedVideoViewHolder)
                    holder;
            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message
                    .getFromUserHeadImage(), message.getPreviewUrl()), position, message
                    .getFromUserName(), DateUtils.getChatShowTime(message.getSendTime()));
        } else if (holder instanceof ChatMessageReceivedWelcomeTipViewHolder) {
            ChatMessageReceivedWelcomeTipViewHolder viewHolder =
                    (ChatMessageReceivedWelcomeTipViewHolder) holder;
            viewHolder.setItemBinding(message.getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatGroupMessage message = messages.get(position);
        switch (message.getContentType()) {
            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT:
                if (message.getFromUserId() == myUserId) {
                    return 100;
                } else {
                    return 200;
                }
            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
                if (message.getFromUserId() == myUserId) {
                    return 101;
                } else {
                    return 201;
                }
            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE:
                if (message.getFromUserId() == myUserId) {
                    return 102;
                } else {
                    return 202;
                }
            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
                if (message.getFromUserId() == myUserId) {
                    return 103;
                } else {
                    return 203;
                }
            default:
            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_SYSTEM:
                return 300;
        }
    }

	public int getCurrentImageIndex(int position,List<String> images){
		int imageIndex = 0;
        int imagePosition = 0;
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

		return imagePosition;
	}

    public void updateMessage(ChatGroupMessage msg) {
        if (msg == null) return;
        boolean isExists = false;
        int position = messages.indexOf(msg);
        if (position == -1) {
            messages.add(msg);
            notifyItemInserted(messages.size() - 1);
        } else {
            messages.remove(position);
            messages.add(position, msg);
            notifyItemChanged(position);
        }
    }

	public void deleteMessage(int position){
        if (position<0) return;
        messages.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, messages.size() - position);
	}

    public void addMessages(List<ChatGroupMessage> msgs) {
        if (msgs == null || msgs.size() <= 0) return;
        messages.addAll(0, msgs);
//        notifyItemRangeInserted(0, messages.size()-1);
    }

    public long getMinMessageId() {
        if (messages != null && messages.size() > 0) {
            return messages.get(0).getMsgId();
        }
        return 0;
    }

    public void updateUserName(long userId, String name) {
        if (userId <= 0 || StringUtils.isBlank(name)) return;
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getFromUserId() == userId) {
                messages.get(i).setFromUserName(name);
                notifyItemChanged(i);
            }
        }
    }

    public ChatGroupMessage getChatGroupMessage(int position) {
        return messages.get(position);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
