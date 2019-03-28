package com.hwl.beta.ui.entry.standard;

import com.hwl.beta.ui.entry.bean.LoginBean;

import io.reactivex.Observable;

public interface LoginStandard {

    LoginBean getLoginBean();

    Observable userLogin(LoginBean loginBean);
}