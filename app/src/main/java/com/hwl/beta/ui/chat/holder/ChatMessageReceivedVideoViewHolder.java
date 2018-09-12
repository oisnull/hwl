//package com.hwl.beta.ui.chat.holder;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.hwl.beta.databinding.ReceivedVideoItemBinding;
//import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
//import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
//import com.hwl.beta.utils.StringUtils;
//
///**
// * Created by Administrator on 2018/1/6.
// */
//
//public class ChatMessageReceivedVideoViewHolder extends RecyclerView.ViewHolder {
//
//    private ReceivedVideoItemBinding itemBinding;
//
//    public ChatMessageReceivedVideoViewHolder(ReceivedVideoItemBinding itemBinding) {
//        super(itemBinding.getRoot());
//        this.itemBinding = itemBinding;
//    }
//
//    public void setItemBinding(IChatMessageItemListener itemListener,
//                               ChatImageViewBean image,
//                               int position,
//                               String userName,
//                               String showTime) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setPosition(position);
//        this.itemBinding.setShowTime(showTime);
//        this.itemBinding.setImage(image);
//        if (StringUtils.isBlank(userName)) {
//            this.itemBinding.tvUsername.setVisibility(View.GONE);
//        } else {
//            this.itemBinding.tvUsername.setVisibility(View.VISIBLE);
//            this.itemBinding.setUserName(userName);
//        }
//    }
//
//    public ReceivedVideoItemBinding getItemBinding() {
//        return itemBinding;
//    }
//}
