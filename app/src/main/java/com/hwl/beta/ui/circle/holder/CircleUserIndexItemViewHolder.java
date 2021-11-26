package com.hwl.beta.ui.circle.holder;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;

import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;
import com.hwl.beta.ui.common.NineImagesAdapter;
import com.hwl.beta.ui.convert.DBCircleAction;

public class CircleUserIndexItemViewHolder extends RecyclerView.ViewHolder {

    private CircleUserIndexItemBinding itemBinding;
    private Context context;

    public CircleUserIndexItemViewHolder(Context context, CircleUserIndexItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
        this.context = context;
    }

    public void setItemBinding(String timeYear,
                               String timeMonth,
                               String timeDay,
                               final Circle info,
                               final ICircleUserItemListener itemListener) {
//        this.itemBinding.setTimeMonth(timeMonth);
//        this.itemBinding.setTimeDay(timeDay);

        this.itemBinding.llContentContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onItemViewClick(info);
            }
        });

        if (TextUtils.isEmpty(timeYear)) {
            this.itemBinding.tvYear.setVisibility(View.GONE);
        } else {
//            this.itemBinding.setTimeYear(timeYear);
            this.itemBinding.tvYear.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(info.getContent())) {
            this.itemBinding.tvContent.setVisibility(View.GONE);
        } else {
//            this.itemBinding.setContent(info.getContent());
            this.itemBinding.tvContent.setVisibility(View.VISIBLE);
        }

        if (info.getImages() != null && info.getImages().size() > 0) {
            NineImagesAdapter imagesAdapter = new NineImagesAdapter(context,
                    DBCircleAction.convertToNineImageModels(info.getImages()),
                    new NineImagesAdapter.ImageItemListener() {
                        @Override
                        public void onImageClick(int position, String imageUrl) {
                            itemListener.onItemViewClick(info);
                        }
                    }, 6);
            this.itemBinding.rvImages.setVisibility(View.VISIBLE);
            this.itemBinding.rvImages.setAdapter(imagesAdapter);
            this.itemBinding.rvImages.setLayoutManager(new GridLayoutManager(context,
                    imagesAdapter.getColumnCount()));
        } else {
            this.itemBinding.rvImages.setVisibility(View.GONE);
        }
    }

    public CircleUserIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
