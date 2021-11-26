package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.ChatReceivedWelcomeTipBinding;

/**
 * Created by Administrator on 2018/1/6.
 */
public class ChatMessageReceivedWelcomeTipViewHolder extends RecyclerView.ViewHolder {

    private ChatReceivedWelcomeTipBinding itemBinding;

    public ChatMessageReceivedWelcomeTipViewHolder(ChatReceivedWelcomeTipBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(String content) {
//        this.itemBinding.setContent(content);
    }

    public ChatReceivedWelcomeTipBinding getItemBinding() {
        return itemBinding;
    }
}
