package com.hwl.beta.ui.circle.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.CircleCommentReplyItemBinding;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;

/**
 * Created by Administrator on 2018/3/17.
 */

public class CircleCommentReplyViewHolder extends RecyclerView.ViewHolder {
    private CircleCommentReplyItemBinding itemBinding;

    public CircleCommentReplyViewHolder(CircleCommentReplyItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleCommentItemListener itemListener, CircleComment comment) {
//        this.itemBinding.setImage(new ImageViewBean(comment.getCommentUserImage()));
//        this.itemBinding.setComment(comment);
//        this.itemBinding.setAction(itemListener);
    }

    public CircleCommentReplyItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
