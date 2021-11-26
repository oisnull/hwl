package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.ChatReceivedMessageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageReceivedMessageViewHolder extends RecyclerView.ViewHolder {

    private ChatReceivedMessageItemBinding itemBinding;

    public ChatMessageReceivedMessageViewHolder(ChatReceivedMessageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               int position,
                               String content,
                               String userName,
                               String showTime) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setPosition(position);
//        this.itemBinding.setContent(content);
//        this.itemBinding.setImage(image);
//        this.itemBinding.setShowTime(showTime);
//        if (StringUtils.isBlank(userName)) {
//            this.itemBinding.tvUsername.setVisibility(View.GONE);
//        } else {
//            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
//            this.itemBinding.setUserName(userName);
//        }
    }

    public ChatReceivedMessageItemBinding getItemBinding() {
        return itemBinding;
    }
}
