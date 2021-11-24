package com.hwl.beta.ui.imgselect.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ImageSelectBean extends BaseObservable {
    private int selectType;
    private String dirName;
    public String dirFullName;
    private String imageCount;

    public ImageSelectBean() {
    }

    public boolean isShowCamera() {
        return ImageSelectType.isShowCamera(this.selectType);
    }

    public ImageSelectBean(int selectType) {
        this.selectType = selectType;
    }

    public int getSelectType() {
        return selectType;
    }

    public void setSelectType(int selectType) {
        this.selectType = selectType;
    }

    @Bindable
    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
//        notifyPropertyChanged(BR.dirName);
    }

    @Bindable
    public String getImageCount() {
        return imageCount;
    }

    public void setImageCount(String imageCount) {
        this.imageCount = imageCount;
//        notifyPropertyChanged(BR.imageCount);
    }
}
