package com.hwl.beta.emotion.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.hwl.beta.emotion.utils.EmotionUtils;

/**
 * Created by Administrator on 2018/4/7.
 */

public class EmotionTextView extends android.support.v7.widget.AppCompatTextView {
    public EmotionTextView(Context context) {
        super(context);
    }

    public EmotionTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text), type);
        } else {
            super.setText(text, type);
        }
    }

    private CharSequence replace(CharSequence text) {
        return EmotionUtils.replaceEmotionText(getContext(), text);
    }
}
