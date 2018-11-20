package com.hwl.beta.ui.group.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.GroupHeadItemBinding;

public class GroupHeadItemViewHolder extends RecyclerView.ViewHolder {
    GroupHeadItemBinding itemBinding;

    public GroupHeadItemViewHolder(GroupHeadItemBinding itemBinding) {
        super(itemBinding.getRoot());

        this.itemBinding = itemBinding;
    }

    public void setItemBinding(String desc){
        this.itemBinding.setDesc(desc);
    }

    public GroupHeadItemBinding getItemBinding() {
        return itemBinding;
    }
}
