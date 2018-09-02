package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/14.
 */

public class UserLoginRequest{
    private String Email;
    private String Mobile;
    private String Password;

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
