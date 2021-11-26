package com.hwl.beta.utils;

import android.text.TextUtils;

import com.hwl.beta.HWLApp;
import com.hwl.beta.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private StringUtils() {
        throw new AssertionError();
    }

    /**
     * is null or its length is 0 or it is made by space
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return
     * true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0 || "null".equals(str));
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * is null or its length is 0
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return
     * false.
     */
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * compare two string
     *
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtils#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtils.isEquals(actual, expected);
    }

    /**
     * get length of CharSequence
     *
     * @param str
     * @return if str is null or empty, return 0, else return
     * {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String) str : str
                .toString()));
    }

    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * capitalize first letter
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length())
                .append(Character.toUpperCase(c))
                .append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     *
     * @param href
     * @return
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern
                .compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source
                .replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    public static String getCookedStrs(ArrayList<String> strs, String separator) {
        StringBuilder sBuilder = new StringBuilder();
        if (strs != null && strs.size() > 0) {
            for (String str : strs) {
                sBuilder.append(str + separator);
            }
            String s = sBuilder.toString();
            return s.substring(0, s.length() - 1);
        }
        return sBuilder.toString();
    }

    public static double getDoubleSize(double size) {
        BigDecimal b = new BigDecimal(size / 1024 / 1024);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return size;
    }

    public static double getDoubleSizeSame(double size, int progress) {
        BigDecimal b = new BigDecimal(size * progress / 100);
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return size;
    }

    public static double getSizeDouble(double size, int dotSize) {
        BigDecimal b = new BigDecimal(size);
        size = b.setScale(dotSize, BigDecimal.ROUND_HALF_UP).doubleValue();
        return size;
    }

    public static boolean isJsonString(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


//    public static String getPinYinFromStr(String str) {
//        String string;
//        StringBuilder sortletterBuilder = new StringBuilder();
//        for (int k = 0; k < str.length(); k++) {
//            sortletterBuilder.append(CharacterParser.getInstance().getSelling(str.substring(k,
// k + 1)).toUpperCase().substring(0, 1));
//        }
//        if (TextUtils.isEmpty(sortletterBuilder) || !sortletterBuilder.toString().substring(0,
// 1).matches("[A-Z]")) {
//            string = "#";
//        } else {
//            string = sortletterBuilder.toString();
//        }
//        return string;
//    }

    public static String hidePhoneStr(String phone) {
        String newPhone;
        if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
            newPhone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone
                    .length());
        } else {
            newPhone = phone;
        }
        return newPhone;
    }

    public static String getRandomString() {
        int randomInt = (int) (Math.random() * 100 + 1);
        return String.valueOf((randomInt * 5 + randomInt + 3880));
    }

    /**
     * 文件大小转化
     *
     * @param size
     * @return
     */
    public static String fileSizeFormat(long size) {
        long kb = 1024;
        long mb = 1024 * 1024;
        long gb = 1024 * 1024 * 1024;

        if (size >= kb && size < mb) {
            return size / kb + "KB";
        } else if (size >= mb && size < gb) {
            return size / mb + "MB";
        } else if (size >= gb) {
            return size / gb + "GB";
        }
        return size + "B";
    }

    /*----------------通过比较版本名判断是否更新----------------*/
    public static boolean compareVersionName(String serverVersion, String localVersion) {
        if (StringUtils.isEmpty(serverVersion)) {
            return false;
        }
        String[] strServer = serverVersion.split("\\.");
        String[] strLocal = localVersion.split("\\.");
        for (int i = 0; i < strServer.length; i++) {
            try {
                if (Integer.valueOf(strServer[i]) > Integer.valueOf(strLocal[i])) {
                    return true;
                } else if (Integer.valueOf(strServer[i]) < Integer.valueOf(strLocal[i])) {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /*----------------验证密码格式是否正确---------------------*/
    public static boolean isFormatSecret(String str) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{6,20}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static boolean isFormatPaySecret(String str) {
        Pattern pattern = Pattern.compile("^[0-9]{6}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /*----------------验证手机号码格式是否正确------------------*/
    public static boolean isPhone(String phone) {
        if (isBlank(phone)) return false;

//        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[0-9]|17[03678]|18[0-9]|14[579])" +
                "[0-9]{8}$");
        return p.matcher(phone).find();
    }

    public static boolean isEmail(String email) {
        if (isBlank(email)) return false;

        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");
        return emailPattern.matcher(email).find();
    }

    /**
     * 返回列表序号
     *
     * @param num
     * @return
     */
    public static String formatNumber(int num) {
        if (num < 9) {
            return "0" + (num + 1);
        } else {
            return (num + 1) + "";
        }
    }

    public static String formatPriceStr(double price) {
        String priceStr = String.format("%.2f", price);
//        if (priceStr.endsWith(".00")) {
//            priceStr = priceStr.replaceAll("\\.00", "");
//        }
        return priceStr;
    }

    //去掉小数点，保留整数
    public static String formatIntegerPriceStr(double price) {
        String priceStr = String.format("%.2f", price);
        if (priceStr.endsWith(".00")) {
            priceStr = priceStr.replaceAll("\\.00", "");
        }
        return priceStr;
    }

    public static String formatPriceStrNoDecimal(double price) {
        String priceStr = String.format("%.2f", price);
        if (priceStr.endsWith(".00")) {
            priceStr = priceStr.replaceAll("\\.00", "");
        }
        return priceStr;
    }

    public static String formatDiscountStr(double discount) {
        String discountStr = String.format("%.1f", discount);
        if (discountStr.endsWith(".0")) {
            discountStr = discountStr.replaceAll("\\.0", "");
        }
        return discountStr;
    }


    public static String formatHtml(String content) {

        String newS = content.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;",
                "&").replaceAll("&quot;", "'")
                .replaceAll("&nbsp;", " ").replaceAll("\\\\", "");
        return newS;
    }

    public static String formatVastPriceStr(double price) {
        //保留两位数
        String priceStr = String.format("%.2f", price);
        //小数点及后面不参与排序
        char[] cutPrice = priceStr.substring(0, priceStr.length() - 3).toCharArray();
        //倒叙，以便于逗号的插入
        String temp1 = "";
        for (int i = cutPrice.length - 1; i >= 0; i--) {
            temp1 += cutPrice[i];
        }
        //插入逗号
        char[] temp2 = temp1.toCharArray();
        String temp3 = "";
        for (int i = 1; i <= temp2.length; i++) {
            if (i % 3 == 0) {
                temp3 = temp3 + temp2[i - 1] + ",";
            } else {
                temp3 += temp2[i - 1];
            }
        }
        //倒叙回来
        char[] temp4 = temp3.toCharArray();
        String temp5 = "";
        for (int i = temp4.length - 1; i >= 0; i--) {
            temp5 += temp4[i];
        }
        //拼接之前的剔除的小数点以及后两位数
        priceStr = temp5 + priceStr.substring(priceStr.length() - 3, priceStr.length());
        if (priceStr.startsWith(",")) {
            priceStr = priceStr.substring(1, priceStr.length());
        }
        if (priceStr.endsWith(".00")) {
            priceStr = priceStr.replaceAll("\\.00", "");
        }
        return priceStr;
    }


    public static String formatVastPriceStrWith2(double price) {
        //保留两位数
        String priceStr = String.format("%.2f", price);
        //小数点及后面不参与排序
        char[] cutPrice = priceStr.substring(0, priceStr.length() - 3).toCharArray();
        //倒叙，以便于逗号的插入
        String temp1 = "";
        for (int i = cutPrice.length - 1; i >= 0; i--) {
            temp1 += cutPrice[i];
        }
        //插入逗号
        char[] temp2 = temp1.toCharArray();
        String temp3 = "";
        for (int i = 1; i <= temp2.length; i++) {
            if (i % 3 == 0) {
                temp3 = temp3 + temp2[i - 1] + ",";
            } else {
                temp3 += temp2[i - 1];
            }
        }
        //倒叙回来
        char[] temp4 = temp3.toCharArray();
        String temp5 = "";
        for (int i = temp4.length - 1; i >= 0; i--) {
            temp5 += temp4[i];
        }
        //拼接之前的剔除的小数点以及后两位数
        priceStr = temp5 + priceStr.substring(priceStr.length() - 3, priceStr.length());
        if (priceStr.startsWith(",")) {
            priceStr = priceStr.substring(1, priceStr.length());
        }
        return priceStr;
    }

    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
        return pattern.matcher(url).matches();
    }

    public static String getUserShowName(String userName, String realName, String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            return nickName;
        } else if (TextUtils.isEmpty(realName)) {
            return realName;
        } else {
            return userName;
        }
    }

    public static String cutString(String content, int count) {
        if (isBlank(content)) {
            return content;
        }
        if (content.length() <= count) {
            return content;
        }
        return content.substring(0, count) + " ...";
    }

}