package com.hwl.beta.net.near;

/**
 * Created by Administrator on 2018/3/18.
 */

public class NetImageInfo {
    private int Width;
    private int Height;
    private String Url;

    public NetImageInfo() {
    }

    public NetImageInfo(int width, int height, String url) {
        Width = width;
        Height = height;
        Url = url;
    }

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
