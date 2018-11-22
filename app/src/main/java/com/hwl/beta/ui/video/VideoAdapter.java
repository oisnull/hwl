package com.hwl.beta.ui.video;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hwl.beta.R;
import com.hwl.beta.utils.ScreenUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/3/3.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoItemViewHolder> {
    private float screenWidth;
    private Context context;
    private List<VideoBean> videoList;
    private IVideoItemListener itemListener;

    public VideoAdapter(Context context, List<VideoBean> videoList) {
        this.context = context;
        this.videoList = videoList;
        this.screenWidth = ScreenUtils.getScreenWidth(context);
    }

    public void setItemClickListener(IVideoItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new VideoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, int position) {
        final VideoBean video = videoList.get(position);
        if (video != null) {// && FileUtils.isExists(video.getPath())
            Glide.with(context).load(Uri.fromFile(new File(video.getPath())))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivVideo);

            holder.tvSize.setText(Formatter.formatFileSize(context, Long.valueOf(video.getSize())));
            holder.ivVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onVideoClick(video.getPath());
                    }
                }
            });

            holder.btnVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onPlayClick(video.getPath());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivVideo;
        public ImageButton btnVideoPlay;
        public TextView tvSize;

        public VideoItemViewHolder(View itemView) {
            super(itemView);

            ivVideo = itemView.findViewById(R.id.iv_video);
            tvSize = itemView.findViewById(R.id.tv_size);
            btnVideoPlay = itemView.findViewById(R.id.btn_video_play);
            //适配imageView，正方形，宽和高都是屏幕宽度的1/3
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivVideo.getLayoutParams();
            params.width = (int) screenWidth / 3 - params.rightMargin - params.leftMargin;
            params.height = (int) screenWidth / 3 - params.topMargin - params.bottomMargin;
            ivVideo.setLayoutParams(params);
        }
    }

    public interface IVideoItemListener {
        void onVideoClick(String videoLocalPath);

        void onPlayClick(String videoLocalPath);
    }
}
