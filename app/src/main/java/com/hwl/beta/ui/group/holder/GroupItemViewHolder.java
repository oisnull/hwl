package com.hwl.beta.ui.group.holder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.GroupItemBinding;

import java.util.List;

public class GroupItemViewHolder extends RecyclerView.ViewHolder {
    GroupItemBinding itemBinding;

    public GroupItemViewHolder(GroupItemBinding itemBinding) {
        super(itemBinding.getRoot());

        this.itemBinding = itemBinding;
    }

    public void setItemBinding(View.OnClickListener itemListener, List<String> groupUserImages, String groupName) {
        this.itemBinding.getRoot().setOnClickListener(itemListener);
        if (groupUserImages != null && groupUserImages.size() > 0) {
            this.itemBinding.ivGroupImage.setImagesData(groupUserImages);
        }
        this.itemBinding.setName(groupName);
    }

    public GroupItemBinding getItemBinding() {
        return itemBinding;
    }
}
