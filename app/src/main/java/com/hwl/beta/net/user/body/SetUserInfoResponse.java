package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SetUserInfoResponse {

    private int Status;
    private String Symbol;
    private String Name;
    private String HeadImage;

    public int getStatus() {
        return Status;
    }

    public String getSymbol() {
        return Symbol;
    }

    public String getName() {
        return Name;
    }

    public String getHeadImage() {
        return HeadImage;
    }
}