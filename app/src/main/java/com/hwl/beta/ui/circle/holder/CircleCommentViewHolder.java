package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleCommentItemBinding;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.ui.circle.action.ICircleCommentItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;

/**
 * Created by Administrator on 2018/3/17.
 */

public class CircleCommentViewHolder extends RecyclerView.ViewHolder {

    private CircleCommentItemBinding itemBinding;

    public CircleCommentViewHolder(CircleCommentItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleCommentItemListener itemListener, CircleComment comment) {
        this.itemBinding.setImage(new ImageViewBean(comment.getCommentUserImage()));
        this.itemBinding.setComment(comment);
        this.itemBinding.setAction(itemListener);
    }

    public CircleCommentItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
