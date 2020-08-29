package com.hwl.beta.ui.chat.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.ChatReceivedVoiceItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageReceivedVoiceViewHolder extends RecyclerView.ViewHolder {

    private ChatReceivedVoiceItemBinding itemBinding;

    public ChatMessageReceivedVoiceViewHolder(ChatReceivedVoiceItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               ChatImageViewBean image,
                               int position,
                               long playTime,
                               String userName,
                               String showTime) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setShowTime(showTime);
        this.itemBinding.setPlayTime(playTime);
        this.itemBinding.setImage(image);
        if (StringUtils.isBlank(userName)) {
            this.itemBinding.tvUsername.setVisibility(View.GONE);
        } else {
            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
            this.itemBinding.setUserName(userName);
        }
    }

    public ChatReceivedVoiceItemBinding getItemBinding() {
        return itemBinding;
    }
}
