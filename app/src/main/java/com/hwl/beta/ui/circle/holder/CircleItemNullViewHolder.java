package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleItemNullBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;

public class CircleItemNullViewHolder extends RecyclerView.ViewHolder {

    private CircleItemNullBinding itemBinding;

    public CircleItemNullViewHolder(CircleItemNullBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleItemListener itemListener) {
        this.itemBinding.setAction(itemListener);
    }

    public CircleItemNullBinding getItemBinding() {
        return this.itemBinding;
    }
}
