package com.hwl.beta.ui.group.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.GroupUserItemHBinding;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class GroupUserItemHViewHolder extends RecyclerView.ViewHolder {
    GroupUserItemHBinding itemBinding;

    public GroupUserItemHViewHolder(GroupUserItemHBinding itemBinding) {
        super(itemBinding.getRoot());

        this.itemBinding = itemBinding;
    }

    public void setItemBinding(View.OnClickListener itemListener,
                               String userImage,
                               String userName,
                               String distance) {
        this.itemBinding.getRoot().setOnClickListener(itemListener);
        ImageViewBean.loadImage(this.itemBinding.ivHeader, userImage);
        this.itemBinding.tvName.setText(userName);
        this.itemBinding.tvDistance.setText(distance);
    }

    public GroupUserItemHBinding getItemBinding() {
        return itemBinding;
    }
}
