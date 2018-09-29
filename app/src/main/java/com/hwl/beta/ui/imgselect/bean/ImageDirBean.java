package com.hwl.beta.ui.imgselect.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/20.
 */

public class ImageDirBean {
    private String name;  //当前文件夹的名字
    private String path;  //当前文件夹的路径
    private ImageBean firstImage;   //当前文件夹需要要显示的缩略图，默认为最近的一次图片
    private ArrayList<ImageBean> images;  //当前文件夹下所有图片的集合

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageBean getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(ImageBean firstImage) {
        this.firstImage = firstImage;
    }

    public ArrayList<ImageBean> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageBean> images) {
        this.images = images;
    }

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     */
    @Override
    public boolean equals(Object o) {
        try {
            ImageDirBean other = (ImageDirBean) o;
            return this.path.equalsIgnoreCase(other.getPath()) && this.name.equalsIgnoreCase
                    (other.getName());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
