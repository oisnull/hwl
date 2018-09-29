package com.hwl.beta.ui.imgselect.action;

import android.widget.CheckBox;

import com.hwl.beta.ui.imgselect.bean.ImageBean;

/**
 * Created by Administrator on 2018/4/4.
 */

public interface IImageSelectItemListener {
    void onCameraClick();

    void onImageClick(ImageBean image);

    void onCheckBoxClick(CheckBox cb, ImageBean image);
}
