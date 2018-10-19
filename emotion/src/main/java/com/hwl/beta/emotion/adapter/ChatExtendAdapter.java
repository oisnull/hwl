package com.hwl.beta.emotion.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwl.beta.emotion.R;

/**
 * Created by Administrator on 2017/12/31.
 */

public class ChatExtendAdapter extends BaseAdapter {
    final Context context;
    //图片的文字标题
    private static final String[] titles = new String[]
            {"图片", "拍照", "位置", "视频"};
    //图片ID数组
    private static final int[] icons = new int[]{
            R.drawable.chat_photo, R.drawable.chat_take_photo, R.drawable.chat_location, R.drawable.chat_video
    };

    public ChatExtendAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public String getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_extend_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivIcon.setImageResource(icons[position]);
        viewHolder.tvTitle.setText(titles[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView ivIcon;
        TextView tvTitle;
    }
}
