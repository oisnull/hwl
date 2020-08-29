package com.hwl.beta.ui.user.bean;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/31.
 */

public class ImageViewBean {

    public ImageViewBean() {
    }

    public ImageViewBean(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageViewBean(String imageUrl, int imageRes) {
        this(imageUrl);
        this.imageRes = imageRes;
    }

    public ImageViewBean(String imageUrl, String imageCircleUrl) {
        this(imageUrl);
        this.imageCircleUrl = imageCircleUrl;
    }

    public String imageUrl;
    public String imageCircleUrl;
    public int imageRes;

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (StringUtils.isBlank(imageUrl))
            return;
        Glide.with(view.getContext()).load(imageUrl)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
				//.priority(Priority.HIGH)
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter({"imageCircleUrl"})
    public static void loadCircleImage(ImageView view, String imageCircleUrl) {
        if (StringUtils.isBlank(imageCircleUrl))
            return;
        Glide.with(view.getContext()).load(imageCircleUrl)
                .placeholder(R.drawable.empty_photo)
                .error(R.drawable.empty_photo)
				//.priority(Priority.HIGH)
                .dontAnimate()
                .into(view);
    }

    @BindingAdapter("imageRes")
    public static void setSrc(ImageView view, int resId) {
        if (resId <= 0) return;
        view.setImageResource(resId);
    }
}
