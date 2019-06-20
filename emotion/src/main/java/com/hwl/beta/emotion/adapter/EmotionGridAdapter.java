package com.hwl.beta.emotion.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hwl.beta.emotion.interfaces.IEmotionItemListener;
import com.hwl.beta.emotion.R;
import com.hwl.beta.emotion.model.EmojiModel;
import com.hwl.beta.emotion.model.EmojiPageModel;
import com.hwl.beta.emotion.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class EmotionGridAdapter extends BaseAdapter {
    Context context;
    List<EmojiModel> emojis = new ArrayList<>();
    IEmotionItemListener emotionListener;
    int emotionWidth;
	boolean isShowTitle;

    public EmotionGridAdapter(Context context,
                              EmojiPageModel pageModel,
                              int emotionWidth,
                              IEmotionItemListener emotionListener) {
        this.context = context;
        this.emotionListener = emotionListener;
        this.emotionWidth = emotionWidth;
        this.isShowTitle = pageModel.getShowTitle();
        this.emojis.addAll(pageModel.getEmojis());
        if (pageModel.isLastItemIsDeleteButton()) {
            this.emojis.add(null);
        }
    }

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
    public View getView(int position, View convertView, ViewGroup parent) {
        if(isShowTitle){
			return getTitleView(position,convertView,parent);
		}else{
			return getImageView(position,convertView,parent);
		}
    }

    private View getImageView(final int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(context);
        iv.setPadding(emotionWidth / 8, emotionWidth / 8, emotionWidth / 8, emotionWidth / 8);
        iv.setLayoutParams(new AbsListView.LayoutParams(emotionWidth, emotionWidth));

        if (emojis.get(position) == null) {
            iv.setImageResource(R.drawable.emotion_delete);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emotionListener.onItemDeleteClick();
                }
            });
        } else {
            iv.setImageResource(emojis.get(position).res);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emotionListener.onItemClick(emojis.get(position));
                }
            });
        }

        return iv;
    }

	 private View getTitleView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_extend_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
			viewHolder.ivIcon.setPadding(emotionWidth / 8, emotionWidth / 8, emotionWidth / 8, emotionWidth / 8);
			viewHolder.ivIcon.setLayoutParams(new AbsListView.LayoutParams(emotionWidth, emotionWidth));
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivIcon.setImageResource(emojis.get(position).res);
        viewHolder.tvTitle.setText(emojis.get(position).title);
		convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emotionListener.onItemClick(emojis.get(position));
                }
            });
        return convertView;
    }

	 class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }
}