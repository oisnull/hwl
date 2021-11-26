package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.ChatSendImageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendImageViewHolder extends RecyclerView.ViewHolder {

    private ChatSendImageItemBinding itemBinding;

    public ChatMessageSendImageViewHolder(ChatSendImageItemBinding itemBinding) {
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

    public ChatSendImageItemBinding getItemBinding() {
        return itemBinding;
    }
}
