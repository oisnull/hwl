package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.ChatSendVideoItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendVideoViewHolder extends RecyclerView.ViewHolder {

    private ChatSendVideoItemBinding itemBinding;

    public ChatMessageSendVideoViewHolder(ChatSendVideoItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               String userName,
                               int position,
                               String statusDesc) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setImage(image);
//        this.itemBinding.setPosition(position);
//        if (StringUtils.isBlank(userName)) {
//            this.itemBinding.tvUsername.setVisibility(View.GONE);
//        } else {
//            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
//            this.itemBinding.setUserName(userName);
//        }
//        this.itemBinding.setStatusDesc(statusDesc);
    }

    public ChatSendVideoItemBinding getItemBinding() {
        return itemBinding;
    }
}
