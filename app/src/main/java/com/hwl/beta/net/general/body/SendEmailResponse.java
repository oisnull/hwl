package com.hwl.beta.net.general.body;

/**
 * Created by Administrator on 2018/1/14.
 */

public class SendEmailResponse {
    private int Status;
    private String CurrentEmail;

    public SendEmailResponse() {
    }

    public SendEmailResponse(int status, String currentEmail) {
        Status = status;
        CurrentEmail = currentEmail;
    }

    public String getCurrentEmail() {
        return CurrentEmail;
    }

    public int getStatus() {
        return Status;
    }
}
