package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.List;

public class CircleUserIndexItemViewHolder extends RecyclerView.ViewHolder {

    private CircleUserIndexItemBinding itemBinding;

    public CircleUserIndexItemViewHolder(CircleUserIndexItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(String timeYear,
                               String timeMonth,
                               String timeDay,
                               String content,
                               List<CircleImage> images) {
        this.itemBinding.setTimeYear(timeYear);
        this.itemBinding.setTimeMonth(timeMonth);
        this.itemBinding.setTimeDay(timeDay);
        this.itemBinding.setContent(content);
        this.setImageViews(images);
    }

    private void setImageViews(final List<CircleImage> images) {
        if (images == null || images.size() <= 0) {
            this.itemBinding.fblImageContainer.setVisibility(View.GONE);
            return;
        }
        this.itemBinding.fblImageContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < this.itemBinding.fblImageContainer.getChildCount(); i++) {
            if (i == 0 && images.size() == 1) {
                this.itemBinding.fblImageContainer.getChildAt(i).setVisibility(View.VISIBLE);
                ImageView iv = (ImageView) this.itemBinding.fblImageContainer.getChildAt(i);
                ImageViewBean.loadImage(iv, images.get(i).getImageUrl());
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.itemBinding.fblImageContainer.getLayoutParams();
                params.width = params.width / 2;
                this.itemBinding.fblImageContainer.setLayoutParams(params);
            } else if ((i + 1) <= images.size()) {
                this.itemBinding.fblImageContainer.getChildAt(i).setVisibility(View.VISIBLE);
                ImageView iv = (ImageView) this.itemBinding.fblImageContainer.getChildAt(i);
                ImageViewBean.loadImage(iv, images.get(i).getImageUrl());
            } else {
                this.itemBinding.fblImageContainer.getChildAt(i).setVisibility(View.GONE);
            }
        }
    }

    public CircleUserIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
