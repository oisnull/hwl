package com.hwl.beta.ui.imgselect.adp;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.databinding.ImgselectCameraItemBinding;
import com.hwl.beta.databinding.ImgselectImageItemBinding;
import com.hwl.beta.ui.imgselect.action.IImageSelectItemListener;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.utils.DisplayUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity context;
    List<ImageBean> images;
    LayoutInflater inflater;
    int imageSize;
    IImageSelectItemListener itemListener;
    boolean isShowCamera;

    public ImageAdapter(Activity context, List<ImageBean> images, boolean isShowCamera,
                        IImageSelectItemListener itemListener) {
        this.context = context;
        this.images = images;
        this.isShowCamera = isShowCamera;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        imageSize = DisplayUtils.getImageItemWidth(context);
    }

    public void refreshData(List<ImageBean> imgs) {
        if (imgs != null && imgs.size() > 0) images = imgs;
        else images.clear();

        if (isShowCamera) {
            images.add(0, new ImageBean());
        }
        notifyDataSetChanged();
    }

    //直接创建ViewHolder即可
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ImgselectCameraItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout
                    .imgselect_camera_item, parent, false);
            return new CameraItemViewHolder(itemBinding);
        } else {
            ImgselectImageItemBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.imgselect_image_item,
                    parent, false);
            return new ImageItemViewHolder(itemBinding);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //如果选择的是多张图片，就排除照相的功能
        if (position == 0 && isShowCamera) {
            return 0;
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CameraItemViewHolder) {
            ImgselectCameraItemBinding itemBinding = ((CameraItemViewHolder) holder).getItemBinding();
            itemBinding.setAction(itemListener);
        } else if (holder instanceof ImageItemViewHolder) {
            ImageBean img = images.get(position);
            ImgselectImageItemBinding itemBinding = ((ImageItemViewHolder) holder).getItemBinding();
            itemBinding.setImage(img);
            itemBinding.setAction(itemListener);

            Glide.with(context).load(img.getPath())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(itemBinding.ivImage);

            if (isShowCamera) {
                itemBinding.cbSelect.setVisibility(View.GONE);
            } else {
                itemBinding.cbSelect.setVisibility(View.VISIBLE);
                if (img.isSelect()) {
                    itemBinding.cbSelect.setChecked(true);
                } else {
                    itemBinding.cbSelect.setChecked(false);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class CameraItemViewHolder extends RecyclerView.ViewHolder {
        private ImgselectCameraItemBinding itemBinding;

        public CameraItemViewHolder(ImgselectCameraItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.getRoot().setLayoutParams(new AbsListView.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, imageSize));
        }

        public ImgselectCameraItemBinding getItemBinding() {
            return itemBinding;
        }
    }

    class ImageItemViewHolder extends RecyclerView.ViewHolder {
        private ImgselectImageItemBinding itemBinding;

        public ImageItemViewHolder(ImgselectImageItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            itemBinding.getRoot().setLayoutParams(new AbsListView.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, imageSize));
        }

        public ImgselectImageItemBinding getItemBinding() {
            return itemBinding;
        }
    }
}