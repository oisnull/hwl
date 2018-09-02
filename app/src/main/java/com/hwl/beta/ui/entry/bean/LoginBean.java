package com.hwl.beta.ui.entry.bean;

import com.hwl.beta.HWLApp;
import com.hwl.beta.R;
import com.hwl.beta.utils.MD5;
import com.hwl.beta.utils.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/3/27.
 */

public class LoginBean {
    private String account;
    private String password;
    protected boolean isEmail = false;
    protected String messageCode;

    public boolean getIsEmail() {
        return isEmail;
    }

    public String getAccount() {
        return account;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMd5Password() {
        if (StringUtils.isBlank(this.password))
            return null;
        return MD5.encode(this.password);
    }

    public boolean checkAccount() {
        this.messageCode = null;
        if (StringUtils.isBlank(this.account)) {
            this.messageCode = HWLApp.getContext().getString(R.string.account_error_tip);
            return false;
        }

        if (this.account.contains("@")) {
            this.isEmail = true;
        } else {
            this.isEmail = false;
        }

        if (this.isEmail) {
            Pattern emailPattern = Pattern.compile(HWLApp.getContext().getString(R.string.reg_email));
            if (!emailPattern.matcher(this.account).find()) {
                this.messageCode = HWLApp.getContext().getString(R.string.email_error_tip);
                return false;
            }
        } else {
            Pattern mobilePattern = Pattern.compile(HWLApp.getContext().getString(R.string.reg_mobile));
            if (!mobilePattern.matcher(this.account).find()) {
                this.messageCode = HWLApp.getContext().getString(R.string.mobile_error_tip);
                return false;
            }
        }

        return true;
    }

    public boolean checkParams() {
        if (!this.checkAccount()) {
            return false;
        }

        if (StringUtils.isBlank(this.password)) {
            this.messageCode = HWLApp.getContext().getString(R.string.password_error_tip);
            return false;
        }

        return true;
    }
}
