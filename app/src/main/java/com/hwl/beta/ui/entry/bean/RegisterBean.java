package com.hwl.beta.ui.entry.bean;

import com.hwl.beta.HWLApp;
import com.hwl.beta.R;
import com.hwl.beta.utils.MD5;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/3/27.
 */

public class RegisterBean extends LoginBean {

    private String passwordOK;
    private String checkCode;

    public String getPasswordOK() {
        return passwordOK;
    }

    public void setPasswordOK(String passwordOK) {
        this.passwordOK = passwordOK;
    }

    public String getMd5PasswordOK() {
        if (StringUtils.isBlank(this.passwordOK))
            return null;
        return MD5.encode(this.passwordOK);
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public boolean checkAccountAndCode() {
        if (!super.checkAccount()) {
            return false;
        }

        if (StringUtils.isBlank(this.checkCode)) {
            this.messageCode = HWLApp.getContext().getString(R.string.code_error_tip);
            return false;
        }

        return true;
    }

    @Override
    public boolean checkParams() {
        if (!super.checkParams()) {
            return false;
        }

        if (StringUtils.isBlank(this.checkCode)) {
            this.messageCode = HWLApp.getContext().getString(R.string.code_error_tip);
            return false;
        }

        if (StringUtils.isBlank(this.passwordOK)) {
            this.messageCode = HWLApp.getContext().getString(R.string.passwordok_error_tip);
            return false;
        }

        if (!super.getPassword().equals(this.passwordOK)) {
            this.messageCode = HWLApp.getContext().getString(R.string.password_ok_error_tip);
            return false;
        }

        return true;
    }
}
