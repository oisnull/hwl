package com.hwl.beta.emotion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hwl.beta.emotion.utils.EmotionUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionGridViewAdapter extends BaseAdapter {
    final Context context;
    List<String> names;
    int emotionWidth;
    IDefaultEmotionListener defaultEmotionListener;

    public EmotionGridViewAdapter(Context context, List<String> names, int emotionWidth) {
        this.context = context;
        this.names = names;
        this.emotionWidth = emotionWidth;
    }

    @Override
    public int getCount() {
        // +1 最后一个为删除按钮
        return names.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(context);
        iv.setPadding(emotionWidth / 8, emotionWidth / 8, emotionWidth / 8, emotionWidth / 8);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(emotionWidth, emotionWidth);
        iv.setLayoutParams(params);

        //判断是否为最后一个item
        if (position == getCount() - 1) {
            iv.setImageResource(R.drawable.emotion_delete);
            if(defaultEmotionListener!=null){
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        defaultEmotionListener.onDefaultItemDeleteClick();
                    }
                });
            }
        } else {
            final String emotionName = names.get(position);
            iv.setImageResource(EmotionUtils.getDefaultEmotionByName(emotionName));
            if(defaultEmotionListener!=null){
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        defaultEmotionListener.onDefaultItemClick(emotionName);
                    }
                });
            }
        }

        return iv;
    }

    public void setDefaultEmotionListener(IDefaultEmotionListener defaultEmotionListener) {
        this.defaultEmotionListener = defaultEmotionListener;
    }
}
