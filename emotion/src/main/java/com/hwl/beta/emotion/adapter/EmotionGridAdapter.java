package com.hwl.beta.emotion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hwl.beta.emotion.interfaces.IDefaultEmotionListener;
import com.hwl.beta.emotion.R;
import com.hwl.beta.emotion.model.EmojiModel;
import com.hwl.beta.emotion.model.EmojiPageModel;
import com.hwl.beta.emotion.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class EmotionGridAdapter extends BaseAdapter {
    Context context;
    List<EmojiModel> emojis = new ArrayList<>();
    IDefaultEmotionListener defaultEmotionListener;
    int emotionWidth;

    public EmotionGridAdapter(Context context,
                              EmojiPageModel pageModel,
                              int emotionWidth,
                              IDefaultEmotionListener defaultEmotionListener) {
        this.context = context;
        this.defaultEmotionListener = defaultEmotionListener;
        this.emotionWidth = emotionWidth;
        this.emojis.addAll(pageModel.getEmojis());
        if (pageModel.isLastItemIsDeleteButton()) {
            this.emojis.add(null);
        }

//        calc(pageModel.getLine());
    }

//    private void calc(int line) {
//        Activity activity = (Activity) context;
//        // 获取屏幕宽度
//        int screenWidth = DisplayUtils.getScreenWidthPixels(activity);
//        // item的间距
//        int padding = DisplayUtils.dp2px(activity, 10);
//        // 动态计算item的宽度和高度
//        emotionWidth = (screenWidth - padding * 8) / line;//DisplayUtils.dp2px(getActivity(), 40);
//        //动态计算gridview的总高度
////        gvHeight = itemWidth * 3 + padding * 6;
//    }

    @Override
    public int getCount() {
        return emojis.size();
    }

    @Override
    public Object getItem(int position) {
        return emojis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(context);
        iv.setPadding(emotionWidth / 8, emotionWidth / 8, emotionWidth / 8, emotionWidth / 8);
        iv.setLayoutParams(new AbsListView.LayoutParams(emotionWidth, emotionWidth));

        if (emojis.get(position) == null) {
            iv.setImageResource(R.drawable.emotion_delete);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultEmotionListener.onDefaultItemDeleteClick();
                }
            });
        } else {
            iv.setImageResource(emojis.get(position).res);
//            iv.setImageResource(R.drawable.emotion_delete);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultEmotionListener.onDefaultItemClick(emojis.get(position).key);
                }
            });
        }

        return iv;
    }
}