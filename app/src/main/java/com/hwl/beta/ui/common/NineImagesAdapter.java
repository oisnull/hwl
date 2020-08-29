package com.hwl.beta.ui.common;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.utils.ScreenUtils;

import java.util.List;

public class NineImagesAdapter extends RecyclerView.Adapter<NineImagesAdapter.ImageViewHolder> {
    private List<NineImageModel> images;
    private LayoutInflater layoutInflater;
    private Context context;
    private int imageWidth;
    private int columnCount;
    private int itemSpacing = 5;
    private ImageItemListener itemListener;

    public NineImagesAdapter(Context context, List<NineImageModel> images,
                             ImageItemListener imageItemListener) {
        this(context, images, imageItemListener, 0);
    }

    public NineImagesAdapter(Context context, List<NineImageModel> images,
                             ImageItemListener imageItemListener, int fixColumnCount) {
        this.context = context;
        this.itemListener = imageItemListener;
        this.images = images;
        layoutInflater = LayoutInflater.from(context);
        if (this.images == null || this.images.size() <= 0) {
            columnCount = 1;
        } else {
            columnCount = images.size() > 3 ? 3 : images.size();
        }
        if (fixColumnCount <= 0)
            imageWidth = ScreenUtils.getScreenWidth(context) / columnCount;
        else
            imageWidth = ScreenUtils.getScreenWidth(context) / fixColumnCount;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.common_image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        if (images.get(position) != null) {
            Glide.with(context).load(images.get(position).imageUrl)
                    .placeholder(R.drawable.empty_photo)
                    .error(R.drawable.empty_photo)
                    .into(holder.iv);

            this.setLayoutParams(position, holder.iv);

            if (this.itemListener != null) {
                holder.iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemListener.onImageClick(position, images.get(position).imageUrl);
                    }
                });
            }
        }
    }

    private void setLayoutParams(int position, ImageView iv) {
        LinearLayout.LayoutParams layoutParams =
                (LinearLayout.LayoutParams) iv.getLayoutParams();
        layoutParams.width = getFactWidth(position);
        layoutParams.height = getFactHeight(position, layoutParams.width);
        int index = position + 1;
        if (index > 1) {
            if (index > columnCount) {
                if (index % columnCount != 1) {
                    layoutParams.setMargins(itemSpacing, itemSpacing, 0, 0);
                } else {
                    layoutParams.setMargins(0, itemSpacing, 0, 0);
                }
            } else {
                layoutParams.setMargins(itemSpacing, 0, 0, 0);
            }
        }
        iv.setLayoutParams(layoutParams);
    }

    private int getFactWidth(int position) {
        if (columnCount == 1 && images.get(position).imageWidth > 0 && images.get(position).imageWidth <= imageWidth) {
            return images.get(position).imageWidth;
        }
        return imageWidth;
    }

    private int getFactHeight(int position, int factWidth) {
        if (columnCount == 1 && images.get(position).imageHeight > 0 && images.get(position).imageHeight <= factWidth) {
            return images.get(position).imageHeight;
        }
        return factWidth;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        ImageViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_image);
        }
    }

    public static class NineImageModel {
        public String imageUrl;
        public int imageHeight;
        public int imageWidth;
    }

    public interface ImageItemListener {
        void onImageClick(int position, String imageUrl);
    }
}
