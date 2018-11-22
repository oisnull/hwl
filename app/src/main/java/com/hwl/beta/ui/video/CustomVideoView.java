package com.hwl.beta.ui.video;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2018/3/3.
 */

public class CustomVideoView extends VideoView {
    IVideoListener videoListener;

    public static String getVideoStoreDir(){
        //需要判断 是否存在， 是否可读。
        return Environment.getExternalStorageDirectory() + "/hwl_videos/";
    }

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
//    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setVideoListener(IVideoListener videoListener) {
        this.videoListener = videoListener;
    }

    @Override
    public void pause() {
        super.pause();
        if (videoListener != null) videoListener.onPause();
    }

    @Override
    public void start() {
        super.start();
        if (videoListener != null) videoListener.onPlay();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSize = getDefaultSize(0, widthMeasureSpec);
//        int heightSize = getDefaultSize(0, heightMeasureSpec);
//        setMeasuredDimension(widthSize, heightSize);
    }

    interface IVideoListener {
        void onPlay();

        void onPause();
    }
}
