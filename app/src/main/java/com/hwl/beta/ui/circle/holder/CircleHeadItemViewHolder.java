package com.hwl.beta.ui.circle.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.CircleHeadItemBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class CircleHeadItemViewHolder extends RecyclerView.ViewHolder {

    private CircleHeadItemBinding itemBinding;

    public CircleHeadItemViewHolder(CircleHeadItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleItemListener itemListener,String userName, ImageViewBean imageBean) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setImage(imageBean);
        this.itemBinding.setUserName(userName);
    }

    public CircleHeadItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
