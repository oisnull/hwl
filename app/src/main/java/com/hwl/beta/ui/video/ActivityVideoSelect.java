package com.hwl.beta.ui.video;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.hwl.beta.R;
import com.hwl.beta.databinding.VideoActivitySelectBinding;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.UITransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/3/3.
 */

public class ActivityVideoSelect extends BaseActivity {
    VideoActivitySelectBinding binding;
    Activity activity;
    List<VideoBean> videos;
    Dialog setUserDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        videos = new ArrayList<>();
        loadVideos(0);

        binding = DataBindingUtil.setContentView(this, R.layout.video_activity_select);
        binding.tbTitle.setImageLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        }).setTitle("视频选择器").setImageRightHide();

        final VideoAdapter videoAdapter = new VideoAdapter(activity, videos);
        videoAdapter.setItemClickListener(new VideoAdapter.IVideoItemListener() {
            @Override
            public void onVideoClick(String videoLocalPath) {
                setSelectDialog(videoLocalPath);
            }

            @Override
            public void onPlayClick(String videoLocalPath) {
                setSelectDialog(videoLocalPath);
            }
        });
        binding.rvVideoList.setAdapter(videoAdapter);
        binding.rvVideoList.setLayoutManager(new GridLayoutManager(activity, 3));
        binding.tvVideoCount.setText(videos.size() + " 个");

        ArrayAdapter typeAdapter = new ArrayAdapter(activity, R.layout.video_type_spinner, new String[]{"手机视频", "HWL视频"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spVideoType.setAdapter(typeAdapter);
        binding.spVideoType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadVideos(position);
                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadVideos(int dirType) {
        String limit[] = {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA};
        ContentResolver cr = getContentResolver();
        Cursor cursor = null;
        if (dirType == 0) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, limit, null, null, null);
            }
        } else {
            cursor = cr.query(Uri.fromFile(new File(CustomVideoView.getVideoStoreDir())), limit, null, null, null);
        }
        if (cursor == null) return;
        videos.clear();
        while (cursor.moveToNext()) {
            VideoBean video = new VideoBean();
            video.setName(cursor.getString(0));
            video.setSize(cursor.getLong(1));
            video.setDuration(cursor.getLong(2));
            video.setPath(cursor.getString(3));
            videos.add(video);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Intent intent = new Intent();
            intent.putExtra("videopath", data.getStringExtra("videopath"));
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void setSelectDialog(final String videoLocalPath) {
        setUserDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.video_action_dialog, null);
        root.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("videopath", videoLocalPath);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
        root.findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toVideoPlayActivity(activity, ActivityVideoPlay.MODE_ACTION, videoLocalPath,1);
            }
        });
        setUserDialog.setContentView(root);
        setUserDialog.show();
    }
}
