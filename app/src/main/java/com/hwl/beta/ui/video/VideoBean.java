package com.hwl.beta.ui.video;

/**
 * Created by Administrator on 2018/3/3.
 */

public class VideoBean {
    private String name;//视频文件名
    private long size;//视频大小
    private long duration;//视频长度
    private String path;

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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
