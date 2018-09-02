package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/14.
 */

public class ResetUserPasswordRequest {
    private long UserId;
    private String OldPassword;
    private String Password;
    private String PasswordOK;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordOK() {
        return PasswordOK;
    }

    public void setPasswordOK(String passwordOK) {
        PasswordOK = passwordOK;
    }
}
