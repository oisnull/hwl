package com.hwl.beta.ui.chat.bean;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.utils.FileUtils;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/31.
 */

public class ChatImageViewBean {

    public ChatImageViewBean() {
    }

    public ChatImageViewBean(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ChatImageViewBean(String imageUrl, String chatImageUrl) {
        this(imageUrl);
        this.chatImageUrl = chatImageUrl;
    }

    public String imageUrl;
    public String chatImageUrl;

    public static void loadImage(ImageView view, String imageUrl) {
        if (StringUtils.isBlank(imageUrl))
            return;
        Glide.with(view.getContext()).load(imageUrl)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view);
    }

    public static void loadChatImage(ImageView view, String imageUrl) {
        if (StringUtils.isBlank(imageUrl))
            return;
        Glide.with(view.getContext()).load(imageUrl)
//                .asBitmap()
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view);
    }


    public static String getShowUrl(String localUrl, String previewUrl, String orgUrl) {
        if (!StringUtils.isBlank(localUrl)) {// &&
            if (FileUtils.isExists(localUrl)) {
                return localUrl;
            }
        }
        if (!StringUtils.isBlank(previewUrl)) {
            return previewUrl;
        }
        if (!StringUtils.isBlank(orgUrl)) {
            return orgUrl;
        }
        return previewUrl;
    }
}
