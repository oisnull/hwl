package com.hwl.beta.ui.near.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.NearCommentReplyItemBinding;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NearCommentReplyViewHolder extends RecyclerView.ViewHolder {
    private NearCommentReplyItemBinding itemBinding;

    public NearCommentReplyViewHolder(NearCommentReplyItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(INearCircleCommentItemListener itemListener, NearCircleComment comment) {
//        this.itemBinding.setImage(new ImageViewBean(comment.getCommentUserImage()));
//        this.itemBinding.setComment(comment);
//        this.itemBinding.setAction(itemListener);
    }

    public NearCommentReplyItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
