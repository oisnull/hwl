package com.hwl.beta.ui.near.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.NearCommentItemBinding;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;

/**
 * Created by Administrator on 2018/3/17.
 */

public class NearCommentViewHolder extends RecyclerView.ViewHolder {

    private NearCommentItemBinding itemBinding;

    public NearCommentViewHolder(NearCommentItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(INearCircleCommentItemListener itemListener,NearCircleComment comment) {
        this.itemBinding.setImage(new ImageViewBean(comment.getCommentUserImage()));
        this.itemBinding.setComment(comment);
        this.itemBinding.setAction(itemListener);
    }

    public NearCommentItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
