package com.hwl.beta.emotion.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.R;
import com.hwl.beta.emotion.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class EmotionIndicatorViewV2 extends LinearLayout {

    private Context context;
    private List<View> dotViews;//存储点的控件对象列表
    private int size = 6;
    private int marginSize = 15;
    private int pointSize;//指示器的大小
    private int marginLeft;//间距

    public EmotionIndicatorViewV2(Context context) {
        this(context, null);
    }

    public EmotionIndicatorViewV2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmotionIndicatorViewV2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        pointSize = DisplayUtils.dp2px(context, size);
        marginLeft = DisplayUtils.dp2px(context, marginSize);
    }

    public void updateCount(int count) {
        if (count <= 0) return;

        if (dotViews == null) {
            dotViews = new ArrayList<>(count);
        } else {
            dotViews.clear();
        }

        View dotView;
        LayoutParams lp;
        for (int i = 0; i < count; i++) {
            dotView = new View(context);
            lp = new LayoutParams(pointSize, pointSize);
            if (i != 0)
                lp.leftMargin = marginLeft;
            dotView.setLayoutParams(lp);
            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.dark_dot);
            } else {
                dotView.setBackgroundResource(R.drawable.white_dot);
            }
            dotViews.add(dotView);
            this.addView(dotView);
        }
    }

    public void setSelectIndicator(int index, int oldIndex) {
        if (index < 0) {
            index = 0;
        }
        if (oldIndex < 0) {
            oldIndex = 0;
        }
        View v1 = dotViews.get(index);
        if (v1 != null) {
            v1.setBackgroundResource(R.drawable.dark_dot);
        }
        if (index == oldIndex) return;
        View v2 = dotViews.get(oldIndex);
        if (v2 != null) {
            v2.setBackgroundResource(R.drawable.white_dot);
        }
    }
}
