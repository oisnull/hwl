package com.hwl.beta.ui.near.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.NearItemNullBinding;
import com.hwl.beta.ui.near.action.INearCircleItemListener;

/**
 * Created by Administrator on 2018/3/14.
 */

public class NearCircleNullViewHolder extends RecyclerView.ViewHolder {

    private NearItemNullBinding itemBinding;

    public NearCircleNullViewHolder(NearItemNullBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(INearCircleItemListener itemListener) {
        this.itemBinding.setAction(itemListener);
    }

    public NearItemNullBinding getItemBinding() {
        return this.itemBinding;
    }
}
