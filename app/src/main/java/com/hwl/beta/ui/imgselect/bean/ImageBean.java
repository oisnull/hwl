package com.hwl.beta.ui.imgselect.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/20.
 */
public class ImageBean implements Serializable, Parcelable {
    private String name;
    private String path;
    private long size;         //图片的大小
    private int width;         //图片的宽度
    private int height;        //图片的高度
    private String mimeType;   //图片的类型
    private long addTime;      //图片的创建时间
    private boolean isSelect;

    public ImageBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public static Creator<ImageBean> getCREATOR() {
        return CREATOR;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>
            () {
        @Override
        public ImageBean createFromParcel(Parcel source) {
            return new ImageBean(source);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.mimeType);
        dest.writeLong(this.addTime);
        dest.writeInt(this.isSelect ? 1 : 0);
    }

    protected ImageBean(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.size = in.readLong();
        this.width = in.readInt();
        this.height = in.readInt();
        this.mimeType = in.readString();
        this.addTime = in.readLong();
        this.isSelect = in.readInt() == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return true;
        ImageBean ib = (ImageBean) obj;
        if (ib.getPath() == null) return false;
        if (ib.getPath().equals(this.getPath()))
            return true;
        return false;
    }
}
