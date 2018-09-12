//package com.hwl.beta.ui.chat.holder;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.hwl.beta.databinding.ReceivedMessageItemBinding;
//import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
//import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
//import com.hwl.beta.utils.StringUtils;
//
///**
// * Created by Administrator on 2018/1/6.
// */
//
//public class ChatMessageReceivedMessageViewHolder extends RecyclerView.ViewHolder {
//
//    private ReceivedMessageItemBinding itemBinding;
//
//    public ChatMessageReceivedMessageViewHolder(ReceivedMessageItemBinding itemBinding) {
//        super(itemBinding.getRoot());
//        this.itemBinding = itemBinding;
//    }
//
//    public void setItemBinding(IChatMessageItemListener itemListener,
//                               ChatImageViewBean image,
//                               int position,
//                               String content,
//                               String userName,
//                               String showTime) {
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
//    }
//
//    public ReceivedMessageItemBinding getItemBinding() {
//        return itemBinding;
//    }
//}
