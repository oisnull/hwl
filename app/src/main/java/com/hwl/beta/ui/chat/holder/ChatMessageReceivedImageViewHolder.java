package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.ChatReceivedImageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageReceivedImageViewHolder extends RecyclerView.ViewHolder {

    private ChatReceivedImageItemBinding itemBinding;

    public ChatMessageReceivedImageViewHolder(ChatReceivedImageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               int position,
                               String userName,
                               String showTime) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setPosition(position);
//        this.itemBinding.setImage(image);
//        this.itemBinding.setShowTime(showTime);
//        if (StringUtils.isBlank(userName)) {
//            this.itemBinding.tvUsername.setVisibility(View.GONE);
//        } else {
//            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
//            this.itemBinding.setUserName(userName);
//        }
    }

    public ChatReceivedImageItemBinding getItemBinding() {
        return itemBinding;
    }
}
