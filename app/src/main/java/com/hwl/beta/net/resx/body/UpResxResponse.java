package com.hwl.beta.net.resx.body;

import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/24.
 */
public class UpResxResponse {
    private boolean Success;
    private String Message;
    private String ResxAccessUrl;

    //image
    private int ImageWidth;
    private int ImageHeight;
    private String ImagePreviewUrl;

    //video
    private double Duration;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResxAccessUrl() {
        return ResxAccessUrl;
    }

    public void setResxAccessUrl(String resxAccessUrl) {
        ResxAccessUrl = resxAccessUrl;
    }

    public int getImageWidth() {
        return ImageWidth;
    }

    public void setImageWidth(int imageWidth) {
        ImageWidth = imageWidth;
    }

    public int getImageHeight() {
        return ImageHeight;
    }

    public void setImageHeight(int imageHeight) {
        ImageHeight = imageHeight;
    }

    public String getImagePreviewUrl() {
        return ImagePreviewUrl;
    }

    public void setImagePreviewUrl(String imagePreviewUrl) {
        ImagePreviewUrl = imagePreviewUrl;
    }

    public double getDuration() {
        return Duration;
    }

    public void setDuration(double duration) {
        Duration = duration;
    }

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

