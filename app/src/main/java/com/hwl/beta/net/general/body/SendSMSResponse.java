package com.hwl.beta.net.general.body;


/**
 * Created by Administrator on 2018/1/15.
 */
public class SendSMSResponse {
    private int Status;
    private String CurrentMobile;

    public String getCurrentMobile() {
        return CurrentMobile;
    }

    public int getStatus() {
        return Status;
    }
}