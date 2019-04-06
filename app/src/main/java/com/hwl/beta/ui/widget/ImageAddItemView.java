package com.hwl.beta.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.imgselect.bean.ImageBean;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageAddItemView extends LinearLayout {
    Context context;
    Activity activity;
    WarpLinearLayout wllImageContainer;
    int screenWidth;
    final int MAX_IMAGE_COUNT = 9;
    List<String> imagePaths = new ArrayList<>();

    public ImageAddItemView(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        init();
    }

    public ImageAddItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = (Activity) context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        init();
    }

    public ImageAddItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.activity = (Activity) context;
        screenWidth = ScreenUtils.getScreenWidth(context);
        init();
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<ImageBean> paths) {
        if (paths == null || paths.size() <= 0) return;
        for (int i = 0; i < paths.size(); i++) {
            imagePaths.add(paths.get(i).getPath());
            addImages(paths.get(i).getPath());
        }
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_image_item_operate, this
                , false);

        wllImageContainer = view.findViewById(R.id.wll_image_container);
        View viewImageAdd = View.inflate(context, R.layout.common_image_add_item, null);
        ImageView ivAdd = viewImageAdd.findViewById(R.id.iv_add);
        setImageContent(ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePaths.size() >= MAX_IMAGE_COUNT) {
                    Toast.makeText(context, "只能发布" + MAX_IMAGE_COUNT + "张图片",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                UITransfer.toImageSelectActivity(activity, ImageSelectType.CHAT_PUBLISH,
                        MAX_IMAGE_COUNT - imagePaths.size(), 1);

            }
        });
        wllImageContainer.addView(viewImageAdd);

        this.addView(view);
    }

    private void setImageContent(ImageView ivImage) {
        RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
        params.width = screenWidth / 4 - params.rightMargin - params.leftMargin;
        params.height = screenWidth / 4 - params.topMargin - params.bottomMargin;
        ivImage.setLayoutParams(params);
    }

    private void addImages(final String localPath) {
        View view = View.inflate(activity, R.layout.common_publish_image_item, null);
        ImageView ivImage = view.findViewById(R.id.iv_image);
        ImageView ivDelete = view.findViewById(R.id.iv_delete);
        setImageContent(ivImage);
        Glide.with(activity).load(localPath)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImage);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePaths.remove(localPath);
                wllImageContainer.removeView((View) v.getParent());
            }
        });
        wllImageContainer.addView(view, 0);
    }
}
