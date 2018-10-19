package com.hwl.beta.emotion.utils;

import com.hwl.beta.emotion.R;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/1/3.
 */


public class EmotionUtils {
    /**
     * 表情类型标志符
     */
    //public static final int EMOTION_CLASSIC_TYPE = 0x0001;//经典表情

    /**
     * key-表情文字;
     * value-表情图片资源
     */
    private static LinkedHashMap<String, Integer> defaultEmotions;
    //private static String defaultEmotionNamePatternString;
    public static final String REGEXDEFAULTEMOTION = "\\[([\u4e00-\u9fa5\\w])+\\]";

    static {
        defaultEmotions = new LinkedHashMap<>();

        defaultEmotions.put("[微笑]", R.drawable.emoji_1f60a);
        defaultEmotions.put("[撇嘴]", R.drawable.emoji_1f616);
        defaultEmotions.put("[色]", R.drawable.emoji_1f60d);
        defaultEmotions.put("[发呆]", R.drawable.emoji_1f633);
        defaultEmotions.put("[得意]", R.drawable.emoji_1f60e);
        defaultEmotions.put("[流泪]", R.drawable.emoji_1f62d);
        defaultEmotions.put("[无奈]", R.drawable.emoji_1f602);
        defaultEmotions.put("[闭嘴]", R.drawable.emoji_1f910);
        defaultEmotions.put("[口罩]", R.drawable.emoji_1f637);
        defaultEmotions.put("[睡]", R.drawable.emoji_1f634);
        defaultEmotions.put("[大哭]", R.drawable.emoji_1f62b);
        defaultEmotions.put("[尴尬]", R.drawable.emoji_1f605);
        defaultEmotions.put("[发怒]", R.drawable.emoji_1f621);
        defaultEmotions.put("[生气]", R.drawable.emoji_1f624);
        defaultEmotions.put("[惊恐]", R.drawable.emoji_1f631);
        defaultEmotions.put("[调皮]", R.drawable.emoji_1f60b);
        defaultEmotions.put("[调皮2]", R.drawable.emoji_1f61c);
        defaultEmotions.put("[呲牙]", R.drawable.emoji_1f604);
        defaultEmotions.put("[难过]", R.drawable.emoji_2639);
        defaultEmotions.put("[酷]", R.drawable.emoji_1f913);
        defaultEmotions.put("[汗]", R.drawable.emoji_1f630);
        defaultEmotions.put("[汗2]", R.drawable.emoji_1f613);
        defaultEmotions.put("[飞吻]", R.drawable.emoji_1f618);
        defaultEmotions.put("[亲亲]", R.drawable.emoji_1f619);
        defaultEmotions.put("[可爱]", R.drawable.emoji_1f917);
        defaultEmotions.put("[白眼]", R.drawable.emoji_1f644);
        defaultEmotions.put("[傲慢]", R.drawable.emoji_1f912);
        defaultEmotions.put("[尬笑]", R.drawable.emoji_1f601);
        defaultEmotions.put("[幻想]", R.drawable.emoji_1f607);
        defaultEmotions.put("[困]", R.drawable.emoji_1f62a);
        defaultEmotions.put("[惊讶]", R.drawable.emoji_1f628);
        defaultEmotions.put("[蔑视]", R.drawable.emoji_1f612);
        defaultEmotions.put("[流汗]", R.drawable.emoji_1f625);
        defaultEmotions.put("[搞怪]", R.drawable.emoji_1f608);
        defaultEmotions.put("[憨笑]", R.drawable.emoji_1f603);
        defaultEmotions.put("[坏笑]", R.drawable.emoji_1f60f);
        defaultEmotions.put("[思考]", R.drawable.emoji_1f914);
        defaultEmotions.put("[财迷]", R.drawable.emoji_1f911);
        defaultEmotions.put("[受伤]", R.drawable.emoji_1f915);
        defaultEmotions.put("[猪头]", R.drawable.emoji_1f437);
        defaultEmotions.put("[拳头]", R.drawable.emoji_1f44a);
        defaultEmotions.put("[巴掌]", R.drawable.emoji_1f44b);
        defaultEmotions.put("[OK]", R.drawable.emoji_1f44c);
        defaultEmotions.put("[强]", R.drawable.emoji_1f44d);
        defaultEmotions.put("[弱]", R.drawable.emoji_1f44e);
        defaultEmotions.put("[欢迎]", R.drawable.emoji_1f44f);
        defaultEmotions.put("[中指]", R.drawable.emoji_1f595);
        defaultEmotions.put("[耶]", R.drawable.emoji_270c);
        defaultEmotions.put("[国旗]", R.drawable.emoji_1f1e8_1f1f3);
        defaultEmotions.put("[祈福]", R.drawable.emoji_1f64f);
        defaultEmotions.put("[生日]", R.drawable.emoji_1f382);


        //loadDefaultEmotionNames();
    }

//    private static void loadDefaultEmotionNames() {
//        StringBuilder patternString = new StringBuilder(
//                defaultEmotions.size() * 2);
//        patternString.append('(');
//        for (int i = 0; i < defaultEmotions.size(); i++) {
//            String s = EmotionUtils.getDefaultEmotionMap().keyAt(i);
//            patternString.append(Pattern.quote(s));
//            patternString.append('|');
//        }
//        patternString.replace(patternString.length() - 1,
//                patternString.length(), ")");
//        defaultEmotionNamePatternString = patternString.toString();
//    }

    //public static String getDefaultEmotionPatternNames() {
    //    return defaultEmotionNamePatternString;
    //}

    /**
     * 根据名称获取默认表情图标R值
     */
    public static int getDefaultEmotionByName(String name) {
        Integer integer = defaultEmotions.get(name);
        return integer == null ? -1 : integer;
    }

    /**
     * 获取默认表情列表
     */
    public static LinkedHashMap<String, Integer> getDefaultEmotionMap() {
        return defaultEmotions;
    }
}
