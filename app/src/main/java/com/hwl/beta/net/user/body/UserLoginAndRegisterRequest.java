package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/14.
 */

public class UserLoginAndRegisterRequest{
    private String Email;
    private String Mobile;
    private String CheckCode;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getCheckCode() {
        return CheckCode;
    }

    public void setCheckCode(String checkCode) {
        CheckCode = checkCode;
    }
}
