package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.CircleMsgcountItemBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;

public class CircleMsgcountItemViewHolder extends RecyclerView.ViewHolder {

    private CircleMsgcountItemBinding itemBinding;

    public CircleMsgcountItemViewHolder(CircleMsgcountItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public CircleMsgcountItemBinding getItemBinding() {
        return this.itemBinding;
    }

    public void setItemBinding(final ICircleItemListener itemListener, int msgCount) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setMsgCount(msgCount + "");
    }

    public void setMessageItemVisibility(int visibility) {
        itemBinding.llMessageTip.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
            itemBinding.llMessageTip.setPadding(0, 60, 0, 60);
        }
    }
}