package com.hwl.beta.emotion.audio;

import android.os.Handler;
import android.widget.ImageView;

import com.hwl.beta.emotion.R;

import java.util.Timer;
import java.util.TimerTask;

public class AudioPlay {

    public final static int PLAY_LEFT = 1;
    public final static int PLAY_RIGHT = 2;
    public final static int PLAY_PROCESSING = 1;
    public final static int PLAY_COMPLETE = 0;

    private Handler handler;
    private ImageView imageView;
    private ImageView lastImageView;
    private Timer timer = new Timer();
    private TimerTask timerTask;

    private int status = 0;//0表示未开始播放 1表示已经开始播放
    private int i;

    private int modelType = 1;//类型

    private int[] leftVoiceBg = new int[]{R.drawable.audio_playing2_1, R.drawable.audio_playing2_2, R.drawable.audio_playing2};
    private int[] rightVoiceBg = new int[]{R.drawable.audio_playing_1, R.drawable.audio_playing_2, R.drawable.audio_playing};
    private int[] collectVoiceBg = new int[]{R.drawable.audio_playing_1, R.drawable.audio_playing_2, R.drawable.audio_playing};

    public AudioPlay(ImageView imageView, int modelType) {
        super();
        this.handler = new Handler();
        this.imageView = imageView;
        this.modelType = modelType;
    }

    public int getStatus() {
        return status;
    }

    public void start(final String localFilePath, final long playEndSeconds) {
        if (imageView == null || status == 1) {
            return;
        }
        i = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (imageView != null) {
                    if (modelType == 1) {
                        changeBg(leftVoiceBg[i % 3], false);
                    } else if (modelType == 2) {
                        changeBg(rightVoiceBg[i % 3], false);
                    } else if (modelType == 3) {
                        changeBg(collectVoiceBg[i % 3], false);
                    }
                } else {
                    return;
                }
                if (i == 0) {
                    MediaManager.playSound(imageView.getContext(), localFilePath);
                }
                i++;
            }
        };
        status = 1;
        autoStop(playEndSeconds);
        timer.schedule(timerTask, 0, 300);
    }

    private void autoStop(long playEndSeconds) {
        if (playEndSeconds <= 0) return;
        new AudioTimeCount((long) ((playEndSeconds + 0.2) * 1000), 1000, null,
                new AudioTimeCount.TimeCountInterface() {
                    @Override
                    public void onFinishViewChange(int resId) {
                        stop();
                    }

                    @Override
                    public void onTickViewChange(long millisUntilFinished, int resId) {

                    }
                }).start();
    }

    public void stop() {
        lastImageView = imageView;
        if (lastImageView != null) {
            switch (modelType) {
                case 1:
                    changeBg(R.drawable.audio_playing2, true);
                    break;
                case 2:
                    changeBg(R.drawable.audio_playing, true);
                    break;
                case 3:
                    changeBg(R.drawable.audio_playing, true);
                default:
                    changeBg(R.drawable.audio_playing, true);
                    break;
            }
            if (timerTask != null) {
                timerTask.cancel();
            }
            MediaManager.release();
        }
        status = 0;
    }

    private void changeBg(final int id, final boolean isStop) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (isStop) {
                    lastImageView.setImageResource(id);
                } else {
                    imageView.setImageResource(id);

                }
            }
        });
    }

//    public void setImageView(ImageView imageView) {
//        this.imageView = imageView;
//    }
//
//    public void setModelType(int modelType) {
//        this.modelType = modelType;
//    }

}