package com.hwl.beta.emotion.audio;

import android.media.MediaRecorder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class AudioRecorderManager {

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;
    private static AudioRecorderManager mInstance;
    private boolean isPrepared;
    private RecordListener recordListener;

    public AudioRecorderManager(String dir) {
        mDir = dir;
    }

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    public static AudioRecorderManager getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioRecorderManager.class) {
                if (mInstance == null) {
                    mInstance = new AudioRecorderManager(dir);
                }
            }
        }
        return mInstance;
    }

    /**
     * 准备
     */
    public void prepareAudio() {
        try {
            isPrepared = false;
            File dir = new File(mDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = generateFileName();
            File file = new File(dir, fileName);

            mCurrentFilePath = file.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());
            //设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            //设置音频的格式为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            //准备结束
            isPrepared = true;
            recordListener.success(file.getPath());
        } catch (IOException e) {
            recordListener.error(e.getMessage());
        }
    }

    private String generateFileName() {
        return new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + getRandom(6) + ".amr";
    }

    private static String getRandom(int i) {
        Random jjj = new Random();
        if (i == 0)
            return "";
        String jj = "";
        for (int k = 0; k < i; k++) {
            jj = jj + jjj.nextInt(9);
        }
        return jj;
    }

    public int getVoiceLevel(int maxLevel) {
        if (isPrepared) {
            //获得最大的振幅getMaxAmplitude() 1-32767
            try {
                return maxLevel * mMediaRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {

            }
        }
        return 1;
    }

    public void release() {
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                //e.printStackTrace();
                mMediaRecorder = null;
                mMediaRecorder = new MediaRecorder();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFilePath != null) {
            File file = new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath = null;
        }
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    public interface RecordListener {
        void success(String path);

        void error(String error);
    }
}
