package com.hwl.beta.net.resx.body;

/**
 * Created by Administrator on 2018/1/24.
 */
public class UpResxResponse {
    private boolean Success;
    private String OriginalUrl;
    private long OriginalSize;
    private String PreviewUrl;
    private long PreviewSize;
    private int Width;
    private int Height;
    private int PlayTime;

    public void setSuccess(boolean success) {
        Success = success;
    }

    public void setOriginalUrl(String originalUrl) {
        OriginalUrl = originalUrl;
    }

    public void setOriginalSize(long originalSize) {
        OriginalSize = originalSize;
    }

    public void setPreviewUrl(String previewUrl) {
        PreviewUrl = previewUrl;
    }

    public void setPreviewSize(long previewSize) {
        PreviewSize = previewSize;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public void setPlayTime(int playTime) {
        PlayTime = playTime;
    }

    public boolean isSuccess() {
        return Success;
    }

    public String getOriginalUrl() {
        return OriginalUrl;
    }

    public long getOriginalSize() {
        return OriginalSize;
    }

    public String getPreviewUrl() {
        return PreviewUrl;
    }

    public long getPreviewSize() {
        return PreviewSize;
    }

    public int getWidth() {
        return Width;
    }

    public int getHeight() {
        return Height;
    }

    public int getPlayTime() {
        return PlayTime;
    }
}