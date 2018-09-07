package main.java.com.hwl.beta.ui.entry.standard;

import java.util.function.Consumer;

import com.hwl.beta.ui.entry.bean.RegisterBean;

/**
 * RegisterStandard
 */
public interface RegisterStandard {

    RegisterBean getRegisterBean();

    void userRegister(RegisterBean registerBean,Runnable succCallback,Consumer<String> errorCallback);

    void sendCode(RegisterBean registerBean,Runnable succCallback,Consumer<String> errorCallback);
}