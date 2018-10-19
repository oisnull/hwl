package com.hwl.beta.emotion;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hwl.beta.emotion.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionExtendHorizontalAdapter extends RecyclerView.Adapter<EmotionExtendHorizontalAdapter.ViewHolder> {

    final Context context;
    List<ExtendImageModel> images;

    public EmotionExtendHorizontalAdapter(Context context) {
        this.context = context;

        images = new ArrayList<>();
        ExtendImageModel model1 = new ExtendImageModel();
        model1.selected = false;
        model1.title = "默认表情";
        model1.resource = R.drawable.ic_emotion_add;
        images.add(model1);

        ExtendImageModel model2 = new ExtendImageModel();
        model2.selected = true;
        model2.title = "添加";
        model2.resource = R.drawable.ic_emotion_default;
        images.add(model2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_extend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExtendImageModel img = images.get(position);

        //动态计算底部tab的宽度。
        int width = ScreenUtils.getScreenWidth(context);
        float itemW = width / 6;
        ViewGroup.LayoutParams lp = holder.ivExtend.getLayoutParams();
        lp.width = (int) itemW;

        holder.ivExtend.setImageResource(img.resource);
        if (img.selected) {
            holder.ivExtend.setBackgroundColor(context.getResources().getColor(R.color.main_background));
        } else {
            holder.ivExtend.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivExtend;

        public ViewHolder(View itemView) {
            super(itemView);
            ivExtend = itemView.findViewById(R.id.iv_extends);
        }
    }

    public class ExtendImageModel {
        public String title;//说明文本
        public int resource;//图标
        public boolean selected = false;//是否被选中
    }
}
