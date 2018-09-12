package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.ReceivedWelcomeTipBinding;

/**
 * Created by Administrator on 2018/1/6.
 */
public class ChatMessageReceivedWelcomeTipViewHolder extends RecyclerView.ViewHolder {

    private ReceivedWelcomeTipBinding itemBinding;

    public ChatMessageReceivedWelcomeTipViewHolder(ReceivedWelcomeTipBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(String content) {
        this.itemBinding.setContent(content);
    }

    public ReceivedWelcomeTipBinding getItemBinding() {
        return itemBinding;
    }
}
