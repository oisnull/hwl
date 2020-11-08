package com.hwl.beta.emotion;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.hwl.beta.emotion.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/5.
 */

public class EmotionExtendHorizontalAdapter extends RecyclerView
        .Adapter<EmotionExtendHorizontalAdapter.ViewHolder> {

    Context context;
    List<ExtendImageModel> images;
    ItemListener itemListener;

    public EmotionExtendHorizontalAdapter(Context context, ItemListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;

        images = new ArrayList<>();
        ExtendImageModel model1 = new ExtendImageModel();
        model1.selected = true;
        model1.title = "默认表情";
        model1.resource = R.drawable.ic_emotion_default;

        ExtendImageModel model2 = new ExtendImageModel();
        model2.selected = false;
        model2.isEnableSelected = false;
        model2.title = "添加";
        model2.resource = R.drawable.ic_emotion_add;

        // ExtendImageModel model3 = new ExtendImageModel();
        // model3.selected = false;
        // model3.title = "收藏表情";
        // model3.resource = R.drawable.ic_emotion_default;

        images.add(model2);
        images.add(model1);
        // images.add(model3);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_extend_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ExtendImageModel img = images.get(position);

        int width = ScreenUtils.getScreenWidth(context);
        float itemW = width / 6;
        ViewGroup.LayoutParams lp = holder.ivExtend.getLayoutParams();
        lp.width = (int) itemW;

        holder.ivExtend.setImageResource(img.resource);
        this.setItemSelectStatus(holder.ivExtend, img.selected);

        holder.ivExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItemSelectStatus(position);
                if (itemListener != null) {
                    switch (position) {
                        case 0:
                            itemListener.onAddItemClick();
                            break;
                        case 1:
                            itemListener.onDefaultItemClick();
                           break;
                        default:
                            itemListener.onCustomizeItemClick();
                            break;
                    }
                }
            }
        });
    }

    private void updateItemSelectStatus(int position) {
        if (!images.get(position).isEnableSelected) return;
        for (int i = 0; i < images.size(); i++) {
            ExtendImageModel img = images.get(i);
            if (i == position && img.isEnableSelected) {
                img.selected = true;
            } else {
                img.selected = false;
            }
        }

        notifyDataSetChanged();
    }

    private void setItemSelectStatus(ImageView ivExtend, boolean isSelected) {
        if (isSelected) {
            ivExtend.setBackgroundColor(context.getResources().getColor(R.color
                    .main_background));
        } else {
            ivExtend.setBackgroundColor(Color.TRANSPARENT);
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
        public boolean isEnableSelected = true;
        public boolean selected = false;//是否被选中
    }

    public interface ItemListener {
        void onCustomizeItemClick();

        void onDefaultItemClick();

        void onAddItemClick();
    }
}
