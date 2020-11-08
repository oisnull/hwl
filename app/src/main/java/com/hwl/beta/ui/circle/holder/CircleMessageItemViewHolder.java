package com.hwl.beta.ui.circle.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.CircleMessageItemBinding;
import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.utils.StringUtils;

public class CircleMessageItemViewHolder extends RecyclerView.ViewHolder {

    private CircleMessageItemBinding itemBinding;

    public CircleMessageItemViewHolder(CircleMessageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public CircleMessageItemBinding getItemBinding() {
        return this.itemBinding;
    }

    public void setItemBinding(CircleMessage message) {
        this.itemBinding.setMessage(message);
        this.itemBinding.setImage(new ImageViewBean(message.getUserImage()));
        if (message.getStatus() == DBConstant.STAUTS_DELETE) {
            this.itemBinding.ivLike.setVisibility(View.GONE);
            this.itemBinding.tvComment.setVisibility(View.VISIBLE);
            this.itemBinding.llReplyContainer.setVisibility(View.GONE);
            this.itemBinding.tvComment.setText("评论已删除");
            message.setComment("评论已删除");
        } else {
            if (message.getType() == DBConstant.CIRCLE_TYPE_LIKE) {
                this.itemBinding.ivLike.setVisibility(View.VISIBLE);
                this.itemBinding.tvComment.setVisibility(View.GONE);
                this.itemBinding.llReplyContainer.setVisibility(View.GONE);
            } else {
                this.itemBinding.ivLike.setVisibility(View.GONE);
                if (message.getReplyUserId() > 0) {
                    this.itemBinding.tvComment.setVisibility(View.GONE);
                    this.itemBinding.llReplyContainer.setVisibility(View.VISIBLE);
                } else {
                    this.itemBinding.tvComment.setVisibility(View.VISIBLE);
                    this.itemBinding.llReplyContainer.setVisibility(View.GONE);
                }
            }
        }

        if (StringUtils.isBlank(message.getContent())) {
            this.itemBinding.tvContent.setVisibility(View.GONE);
        } else {
            this.itemBinding.tvContent.setVisibility(View.VISIBLE);
            this.itemBinding.tvContent.setText(message.getContent());
        }
    }
}