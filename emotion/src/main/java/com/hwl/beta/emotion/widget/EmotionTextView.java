package com.hwl.beta.emotion.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.hwl.beta.emotion.utils.EmotionUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Pattern pattern = Pattern.compile(EmotionUtils.REGEXDEFAULTEMOTION);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                if (EmotionUtils.getDefaultEmotionMap().containsKey(matcher.group())) {
                    int id = EmotionUtils.getDefaultEmotionMap().get(matcher.group());
//                    Drawable drawable = getResources().getDrawable(id);
//                    drawable.setBounds(0, 0, 25, 25);
//                    builder.setSpan(new ImageSpan(drawable), matcher.start(), matcher.end(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Bitmap bitmap = BitmapFactory.decodeResource(
                            getResources(), id);
                    if (bitmap != null) {
                        ImageSpan span = new ImageSpan(getContext(), bitmap);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            return builder;
        } catch (Exception e) {
            return text;
        }
    }
}
