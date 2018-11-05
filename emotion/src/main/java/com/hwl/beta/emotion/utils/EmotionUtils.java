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

        defaultEmotions.put("[表情1]", R.drawable.a1);
        defaultEmotions.put("[表情2]", R.drawable.a2);
        defaultEmotions.put("[表情3]", R.drawable.a3);
        defaultEmotions.put("[表情4]", R.drawable.a4);
        defaultEmotions.put("[表情5]", R.drawable.a5);
        defaultEmotions.put("[表情6]", R.drawable.a6);
        defaultEmotions.put("[表情7]", R.drawable.a7);
        defaultEmotions.put("[表情8]", R.drawable.a8);
        defaultEmotions.put("[表情9]", R.drawable.a9);
        defaultEmotions.put("[表情10]", R.drawable.a10);
        defaultEmotions.put("[表情11]", R.drawable.a11);
        defaultEmotions.put("[表情12]", R.drawable.a12);
        defaultEmotions.put("[表情13]", R.drawable.a13);
        defaultEmotions.put("[表情14]", R.drawable.a14);
        defaultEmotions.put("[表情15]", R.drawable.a15);
        defaultEmotions.put("[表情16]", R.drawable.a16);
        defaultEmotions.put("[表情17]", R.drawable.a17);
        defaultEmotions.put("[表情18]", R.drawable.a18);
        defaultEmotions.put("[表情19]", R.drawable.a19);
        defaultEmotions.put("[表情20]", R.drawable.a20);
        defaultEmotions.put("[表情21]", R.drawable.a21);
        defaultEmotions.put("[表情22]", R.drawable.a22);
        defaultEmotions.put("[表情23]", R.drawable.a23);
        defaultEmotions.put("[表情24]", R.drawable.a24);
        defaultEmotions.put("[表情25]", R.drawable.a25);
        defaultEmotions.put("[表情26]", R.drawable.a26);
        defaultEmotions.put("[表情27]", R.drawable.a27);
        defaultEmotions.put("[表情28]", R.drawable.a28);
        defaultEmotions.put("[表情29]", R.drawable.a29);
        defaultEmotions.put("[表情30]", R.drawable.a30);
        defaultEmotions.put("[表情31]", R.drawable.a31);
        defaultEmotions.put("[表情32]", R.drawable.a32);
        defaultEmotions.put("[表情33]", R.drawable.a33);
        defaultEmotions.put("[表情34]", R.drawable.a34);
        defaultEmotions.put("[表情35]", R.drawable.a35);
        defaultEmotions.put("[表情36]", R.drawable.a36);
        defaultEmotions.put("[表情37]", R.drawable.a37);
        defaultEmotions.put("[表情38]", R.drawable.a38);
        defaultEmotions.put("[表情39]", R.drawable.a39);
        defaultEmotions.put("[表情40]", R.drawable.a40);
        defaultEmotions.put("[表情41]", R.drawable.a41);
        defaultEmotions.put("[表情42]", R.drawable.a42);
        defaultEmotions.put("[表情43]", R.drawable.a43);
        defaultEmotions.put("[表情44]", R.drawable.a44);
        defaultEmotions.put("[表情45]", R.drawable.a45);
        defaultEmotions.put("[表情46]", R.drawable.a46);
        defaultEmotions.put("[表情47]", R.drawable.a47);
        defaultEmotions.put("[表情48]", R.drawable.a48);
        defaultEmotions.put("[表情49]", R.drawable.a49);
        defaultEmotions.put("[表情50]", R.drawable.a50);
        defaultEmotions.put("[表情51]", R.drawable.a51);
        defaultEmotions.put("[表情52]", R.drawable.a52);
        defaultEmotions.put("[表情53]", R.drawable.a53);
        defaultEmotions.put("[表情54]", R.drawable.a54);
        defaultEmotions.put("[表情55]", R.drawable.a55);
        defaultEmotions.put("[表情56]", R.drawable.a56);
        defaultEmotions.put("[表情57]", R.drawable.a57);
        defaultEmotions.put("[表情58]", R.drawable.a58);
        defaultEmotions.put("[表情59]", R.drawable.a59);
        defaultEmotions.put("[表情60]", R.drawable.a60);
        defaultEmotions.put("[表情61]", R.drawable.a61);
        defaultEmotions.put("[表情62]", R.drawable.a62);
        defaultEmotions.put("[表情63]", R.drawable.a63);
        defaultEmotions.put("[表情64]", R.drawable.a64);
        defaultEmotions.put("[表情65]", R.drawable.a65);
        defaultEmotions.put("[表情66]", R.drawable.a66);
        defaultEmotions.put("[表情67]", R.drawable.a67);
        defaultEmotions.put("[表情68]", R.drawable.a68);
        defaultEmotions.put("[表情69]", R.drawable.a69);
        defaultEmotions.put("[表情70]", R.drawable.a70);
        defaultEmotions.put("[表情71]", R.drawable.a71);
        defaultEmotions.put("[表情72]", R.drawable.a72);
        defaultEmotions.put("[表情73]", R.drawable.a73);
        defaultEmotions.put("[表情74]", R.drawable.a74);
        defaultEmotions.put("[表情75]", R.drawable.a75);
        defaultEmotions.put("[表情76]", R.drawable.a76);
        defaultEmotions.put("[表情77]", R.drawable.a77);
        defaultEmotions.put("[表情78]", R.drawable.a78);
        defaultEmotions.put("[表情79]", R.drawable.a79);
        defaultEmotions.put("[表情80]", R.drawable.a80);


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
