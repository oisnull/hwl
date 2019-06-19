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
        defaultEmotions.add(new EmojiModel("photo", R.drawable.chat_photo,"图片"));
        defaultEmotions.add(new EmojiModel("take_photo", R.drawable.chat_take_photo,"拍照"));
        defaultEmotions.add(new EmojiModel("location", R.drawable.chat_location,"位置"));
        defaultEmotions.add(new EmojiModel("video", R.drawable.chat_video,"视频"));
        defaultEmotions.add(new EmojiModel("favorite", R.drawable.chat_video,"收藏"));
    }
}
