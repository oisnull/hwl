package com.hwl.beta.ui.circle.holder;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.databinding.CircleUserItemDefaultBinding;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;

public class CircleUserItemDefaultViewHolder extends RecyclerView.ViewHolder {

    private CircleUserItemDefaultBinding itemBinding;

    public CircleUserItemDefaultViewHolder(CircleUserItemDefaultBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleUserItemListener itemListener,
                               String timeMonth,
                               String timeDay) {
//        this.itemBinding.setAction(itemListener);
//        this.itemBinding.setTimeMonth(timeMonth);
//        this.itemBinding.setTimeDay(timeDay);
    }

    public CircleUserItemDefaultBinding getItemBinding() {
        return this.itemBinding;
    }
}
