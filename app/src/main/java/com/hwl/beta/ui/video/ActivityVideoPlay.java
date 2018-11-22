package com.hwl.beta.ui.video;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.VideoActivityPlayBinding;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.video.action.IVideoPlayListener;
import com.hwl.beta.ui.widget.TimeCount;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/3.
 */

public class ActivityVideoPlay extends BaseActivity {
    public final static int MODE_VIEW = 0;
    public final static int MODE_ACTION = 1;

    Activity activity;
    VideoActivityPlayBinding binding;
    VideoPlayListener videoPlayListener;
    boolean isPlayComplete = false;
    String videoPath;
    int videoMode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        videoPath = getIntent().getStringExtra("videopath");
        videoMode = getIntent().getIntExtra("videomode", 0);

        if (StringUtils.isBlank(videoPath)) {
            Toast.makeText(this, "视频地址错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding = DataBindingUtil.setContentView(activity, R.layout.video_activity_play);
        videoPlayListener = new VideoPlayListener();
        videoPlayListener.onInit();
        binding.setAction(videoPlayListener);

        playVideo(videoPath);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (binding.rlTitle.getVisibility() == View.VISIBLE) {
                binding.rlTitle.setVisibility(View.GONE);
            } else {
                binding.rlTitle.setVisibility(View.VISIBLE);
                videoPlayListener.hideTitle();
            }
        }
        return false;
    }

    private void playVideo(String path) {
        Uri uri = Uri.parse(path);
        binding.vvVideo.setMediaController(new MediaController(activity));
        binding.vvVideo.setVideoURI(uri);
        binding.vvVideo.requestFocus();
        binding.vvVideo.setVideoListener(new VideoListener());
        binding.vvVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                binding.pbVideoStatus.setVisibility(View.GONE);
                binding.btnVideoPlay.setVisibility(View.VISIBLE);
                return false;
            }
        });
        binding.vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.btnVideoPlay.setVisibility(View.VISIBLE);
                binding.rlTitle.setVisibility(View.VISIBLE);
                binding.vvVideo.stopPlayback();
                isPlayComplete = true;
            }
        });
        binding.vvVideo.start();
    }

    public class VideoListener implements CustomVideoView.IVideoListener {

        @Override
        public void onPlay() {
            binding.btnVideoPlay.setVisibility(View.GONE);
            videoPlayListener.hideTitle();
        }

        @Override
        public void onPause() {
            binding.btnVideoPlay.setVisibility(View.VISIBLE);
            binding.rlTitle.setVisibility(View.VISIBLE);
        }
    }

    public class VideoPlayListener implements IVideoPlayListener {

        TimeCount timeCount;

        public void hideTitle() {
            timeCount = new TimeCount(5 * 1000, 1000, null, new TimeCount.TimeCountInterface() {
                @Override
                public void onFinishViewChange(int resId) {
                    binding.rlTitle.setVisibility(View.GONE);
                }

                @Override
                public void onTickViewChange(long millisUntilFinished, int resId) {

                }
            });
            timeCount.start();
        }

        @Override
        public void onInit() {
            if (videoMode == MODE_VIEW)
                binding.tvSend.setVisibility(View.GONE);
        }

        @Override
        public void onBackClick() {
            onBackPressed();
        }

        @Override
        public void onSendClick() {
            Intent intent = new Intent();
            intent.putExtra("videopath", videoPath);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void onPlayClick() {
            if (isPlayComplete) {
                playVideo(videoPath);
                isPlayComplete = false;
            } else {
                binding.vvVideo.start();
            }
        }

        @Override
        public void onPauseClick() {

        }
    }

//    private void downloadVideo() {
//        final String localFilePath = CustomVideoView.getVideoStoreDir() + videoPath.substring(videoPath.lastIndexOf("/") + 1, videoPath.length());
//        final File localFile = new File(localFilePath);
//        DownloadService.downloadStream(
//                videoPath,
//                new IDownloadProgressListener() {
//                    @Override
//                    public void onProcess(boolean isComplete, long totalByte, long currentByte) {
//                        Log.d("ProgressListener", "isComplete=" + isComplete + " totalByte=" + totalByte + " currentByte=" + currentByte);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .map(new Function<Response<ResponseBody>, Boolean>() {
//                    @Override
//                    public Boolean apply(Response<ResponseBody> response) throws Exception {
//                        if (response.isSuccessful() && response.body() != null) {
//                            return FileUtils.writeFile(localFile, response.body().source());
//                        }
//                        return false;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean succ) throws Exception {
//                        Log.d("onSuccess", "下载完成");
//                        if (succ) {
//                            pbVideoStatus.setVisibility(View.GONE);
//                            //播放
//                            videoPath = localFilePath;
//                            playVideo();
//                        } else {
//                            Toast.makeText(activity, "视频下载失败", Toast.LENGTH_SHORT).show();
//                            pbVideoStatus.setVisibility(View.GONE);
//                            btnVideoPlay.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(activity, "视频下载失败", Toast.LENGTH_SHORT).show();
//                        pbVideoStatus.setVisibility(View.GONE);
//                        btnVideoPlay.setVisibility(View.VISIBLE);
//                    }
//                });
//    }
}
