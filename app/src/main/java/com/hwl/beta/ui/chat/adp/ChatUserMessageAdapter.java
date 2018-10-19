// package com.hwl.beta.ui.chat.adp;

// import android.content.Context;
// import android.databinding.DataBindingUtil;
// import android.support.v7.widget.RecyclerView;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;
// import android.widget.ImageView;
// import android.widget.ProgressBar;

// import com.hwl.beta.R;
// import com.hwl.beta.databinding.ReceivedAudioItemBinding;
// import com.hwl.beta.databinding.ReceivedImageItemBinding;
// import com.hwl.beta.databinding.ReceivedMessageItemBinding;
// import com.hwl.beta.databinding.ReceivedVideoItemBinding;
// import com.hwl.beta.databinding.ReceivedWelcomeTipBinding;
// import com.hwl.beta.databinding.SendAudioItemBinding;
// import com.hwl.beta.databinding.SendImageItemBinding;
// import com.hwl.beta.databinding.SendMessageItemBinding;
// import com.hwl.beta.databinding.SendVideoItemBinding;
// import com.hwl.beta.db.entity.ChatUserMessage;
// import com.hwl.beta.mq.MQConstant;
// import com.hwl.beta.sp.UserSP;
// import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
// import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
// import com.hwl.beta.ui.chat.holder.ChatMessageReceivedAudioViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageReceivedImageViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageReceivedMessageViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageReceivedVideoViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageReceivedWelcomeTipViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageSendAudioViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageSendImageViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageSendMessageViewHolder;
// import com.hwl.beta.ui.chat.holder.ChatMessageSendVideoViewHolder;
// import com.hwl.beta.utils.DateUtils;

// import java.util.List;

// /**
// * Created by Administrator on 2018/2/10.
// */

// public class ChatUserMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    Context context;
//    LayoutInflater inflater;
//    List<ChatUserMessage> messages;
//    IChatMessageItemListener itemListener;
//    long myUserId = 0;

//    public ChatUserMessageAdapter(Context context, List<ChatUserMessage> messages, IChatMessageItemListener itemListener) {
//        this.context = context;
//        this.messages = messages;
//        this.itemListener = itemListener;
//        inflater = LayoutInflater.from(context);
//        myUserId = UserSP.getUserId();
//    }

//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 100:
//                return new ChatMessageSendMessageViewHolder((SendMessageItemBinding) DataBindingUtil.inflate(inflater, R.layout.send_message_item, parent, false));
//            case 101:
//                return new ChatMessageSendImageViewHolder((SendImageItemBinding) DataBindingUtil.inflate(inflater, R.layout.send_image_item, parent, false));
//            case 102:
//                return new ChatMessageSendAudioViewHolder((SendAudioItemBinding) DataBindingUtil.inflate(inflater, R.layout.send_audio_item, parent, false));
//            case 103:
//                return new ChatMessageSendVideoViewHolder((SendVideoItemBinding) DataBindingUtil.inflate(inflater, R.layout.send_video_item, parent, false));
//            case 200:
//                return new ChatMessageReceivedMessageViewHolder((ReceivedMessageItemBinding) DataBindingUtil.inflate(inflater, R.layout.received_message_item, parent, false));
//            case 201:
//                return new ChatMessageReceivedImageViewHolder((ReceivedImageItemBinding) DataBindingUtil.inflate(inflater, R.layout.received_image_item, parent, false));
//            case 202:
//                return new ChatMessageReceivedAudioViewHolder((ReceivedAudioItemBinding) DataBindingUtil.inflate(inflater, R.layout.received_audio_item, parent, false));
//            case 203:
//                return new ChatMessageReceivedVideoViewHolder((ReceivedVideoItemBinding) DataBindingUtil.inflate(inflater, R.layout.received_video_item, parent, false));
//            case 300:
//                return new ChatMessageReceivedWelcomeTipViewHolder((ReceivedWelcomeTipBinding) DataBindingUtil.inflate(inflater, R.layout.received_welcome_tip, parent, false));
//        }
//        return null;
//    }

//    private void setSendStatus(int sendStatus, ProgressBar pbMessageStatus, ImageView ivMessageStatusFail) {
//        switch (sendStatus) {
//            case MQConstant.CHAT_SEND_SENDING:
//                pbMessageStatus.setVisibility(View.VISIBLE);
//                ivMessageStatusFail.setVisibility(View.GONE);
//                break;
//            case MQConstant.CHAT_SEND_FAILD:
//                pbMessageStatus.setVisibility(View.GONE);
//                ivMessageStatusFail.setVisibility(View.VISIBLE);
//                break;
//            default:
//            case MQConstant.CHAT_SEND_SUCCESS:
//                pbMessageStatus.setVisibility(View.GONE);
//                ivMessageStatusFail.setVisibility(View.GONE);
//                break;
//        }
//    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        ChatUserMessage message = messages.get(position);
//        if (holder instanceof ChatMessageSendMessageViewHolder) {
//            ChatMessageSendMessageViewHolder viewHolder = (ChatMessageSendMessageViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage()), position, null, message.getContent());
//            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus, viewHolder.getItemBinding().ivMessageStatusFail);
//        } else if (holder instanceof ChatMessageSendImageViewHolder) {
//            ChatMessageSendImageViewHolder viewHolder = (ChatMessageSendImageViewHolder) holder;
//            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message.getPreviewUrl(), message.getOriginalUrl());
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage(), showUrl), null, position);
//            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus, viewHolder.getItemBinding().ivMessageStatusFail);
//        } else if (holder instanceof ChatMessageSendAudioViewHolder) {
//            ChatMessageSendAudioViewHolder viewHolder = (ChatMessageSendAudioViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage()), position, null, message.getPlayTime());
//            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus, viewHolder.getItemBinding().ivMessageStatusFail);
//        } else if (holder instanceof ChatMessageSendVideoViewHolder) {
//            ChatMessageSendVideoViewHolder viewHolder = (ChatMessageSendVideoViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage(), message.getPreviewUrl()), null, position);
//            setSendStatus(message.getSendStatus(), viewHolder.getItemBinding().pbMessageStatus, viewHolder.getItemBinding().ivMessageStatusFail);
//        } else if (holder instanceof ChatMessageReceivedMessageViewHolder) {
//            ChatMessageReceivedMessageViewHolder viewHolder = (ChatMessageReceivedMessageViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage()), position, message.getContent(), null, DateUtils.getChatShowTime(message.getSendTime()));
//        } else if (holder instanceof ChatMessageReceivedImageViewHolder) {
//            ChatMessageReceivedImageViewHolder viewHolder = (ChatMessageReceivedImageViewHolder) holder;
//            String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message.getPreviewUrl(), message.getOriginalUrl());
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage(), showUrl), position, null, DateUtils.getChatShowTime(message.getSendTime()));
//        } else if (holder instanceof ChatMessageReceivedAudioViewHolder) {
//            ChatMessageReceivedAudioViewHolder viewHolder = (ChatMessageReceivedAudioViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage()), position, message.getPlayTime(), null, DateUtils.getChatShowTime(message.getSendTime()));
//        } else if (holder instanceof ChatMessageReceivedVideoViewHolder) {
//            ChatMessageReceivedVideoViewHolder viewHolder = (ChatMessageReceivedVideoViewHolder) holder;
//            viewHolder.setItemBinding(itemListener, new ChatImageViewBean(message.getFromUserHeadImage(), message.getPreviewUrl()), position, null, DateUtils.getChatShowTime(message.getSendTime()));
//        } else if (holder instanceof ChatMessageReceivedWelcomeTipViewHolder) {
//            ChatMessageReceivedWelcomeTipViewHolder viewHolder = (ChatMessageReceivedWelcomeTipViewHolder) holder;
//            viewHolder.setItemBinding(message.getContent());
//        }
//    }

//    @Override
//    public int getItemViewType(int position) {
//        ChatUserMessage message = messages.get(position);
//        switch (message.getContentType()) {
//            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD:
//                if (message.getFromUserId() == myUserId) {
//                    return 100;
//                } else {
//                    return 200;
//                }
//            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
//                if (message.getFromUserId() == myUserId) {
//                    return 101;
//                } else {
//                    return 201;
//                }
//            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_SOUND:
//                if (message.getFromUserId() == myUserId) {
//                    return 102;
//                } else {
//                    return 202;
//                }
//            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
//                if (message.getFromUserId() == myUserId) {
//                    return 103;
//                } else {
//                    return 203;
//                }
//            default:
//            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_REJECT:
//                return 300;
//        }
//    }

//    public void addMessage(ChatUserMessage msg) {
//        if (msg == null) return;
//        int position = messages.indexOf(msg);
//        if (position == -1) {
//            messages.add(msg);
//            notifyItemInserted(messages.size() - 1);
//        } else {
//            messages.remove(position);
//            messages.add(position, msg);
//            notifyItemChanged(position);
//        }
//    }

//    public void addMessages(List<ChatUserMessage> msgs) {
//        if (msgs == null || msgs.size() <= 0) return;
//        messages.addAll(0, msgs);
// //        notifyItemRangeInserted(0, messages.size()-1);
//    }

//    public long getMinMessageId() {
//        if (messages != null && messages.size() > 0) {
//            return messages.get(0).getMsgId();
//        }
//        return 0;
//    }

//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
// }
