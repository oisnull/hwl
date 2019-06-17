package com.hwl.beta.emotion.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.hwl.beta.emotion.R;
import com.hwl.beta.emotion.model.EmojiModel;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/3.
 */


public class EmotionExtends {

    public final static ArrayList<EmojiModel> extendEmotions = new ArrayList<>();

    static {
        defaultEmotions.add(new EmojiModel("图片", R.drawable.chat_photo));
        defaultEmotions.add(new EmojiModel("拍照", R.drawable.chat_take_photo));
        defaultEmotions.add(new EmojiModel("位置", R.drawable.chat_location));
        defaultEmotions.add(new EmojiModel("视频", R.drawable.chat_video));
        defaultEmotions.add(new EmojiModel("收藏", R.drawable.chat_video));
    }
}
