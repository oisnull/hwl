package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.ChatSendMessageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendMessageViewHolder extends RecyclerView.ViewHolder {

    private ChatSendMessageItemBinding itemBinding;

    public ChatMessageSendMessageViewHolder(ChatSendMessageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               int position,
                               String userName,
                               String content,
                               String statusDesc) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setImage(image);
//        this.itemBinding.setPosition(position);
//        this.itemBinding.setContent(content);
//        if (StringUtils.isBlank(userName)) {
//            this.itemBinding.tvUsername.setVisibility(View.GONE);
//        } else {
//            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
//            this.itemBinding.setUserName(userName);
//        }
//        this.itemBinding.setStatusDesc(statusDesc);
    }

    public ChatSendMessageItemBinding getItemBinding() {
        return itemBinding;
    }
}
