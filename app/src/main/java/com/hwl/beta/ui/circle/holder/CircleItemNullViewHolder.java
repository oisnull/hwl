package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hwl.beta.databinding.CircleItemNullBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;

public class CircleItemNullViewHolder extends RecyclerView.ViewHolder {

    private CircleItemNullBinding itemBinding;
    private String defaultYOUDesc = "你的朋友圈还没有人发布信息...";
    private String defaultTADesc = "TA没有发布任何信息...";

    public CircleItemNullViewHolder(CircleItemNullBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(boolean isOwner) {
        setItemBinding(isOwner, null);
    }

    public void setItemBinding(boolean isOwner, ICircleItemListener itemListener) {
        setItemBinding(isOwner ? defaultYOUDesc : defaultTADesc, itemListener);
    }

    public void setItemBinding(ICircleItemListener itemListener) {
        setItemBinding(null, itemListener);
    }

    public void setItemBinding(String desc, final ICircleItemListener itemListener) {
        this.itemBinding.tvDesc.setText(desc);

        if (itemListener != null)
            this.itemBinding.tvPublish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onPublishClick();
                }
            });
        else
            this.itemBinding.tvPublish.setVisibility(View.GONE);
    }

    public CircleItemNullBinding getItemBinding() {
        return this.itemBinding;
    }
}
