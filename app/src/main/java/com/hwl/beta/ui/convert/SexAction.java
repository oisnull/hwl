package com.hwl.beta.ui.convert;

/**
 * Created by Administrator on 2018/4/4.
 */

public class SexAction {

    public final static String MAN = "男";
    public final static String WOMAN = "女";
    public final static String OTHER = "";//其它

    public final static int MAN_1 = 1;
    public final static int WOMAN_0 = 0;
    public final static int OTHER_2 = -1;

    public static String getSexName(int sex) {
        String sexName = OTHER;
        switch (sex) {
            case 0:
                sexName = WOMAN;
                break;
            case 1:
                sexName = MAN;
                break;
        }
        return sexName;
    }

    public static int getSex(String sexName) {
        int sex = -1;
        switch (sexName) {
            case WOMAN:
                sex = 0;
                break;
            case MAN:
                sex = 1;
                break;
        }
        return sex;
    }

}
