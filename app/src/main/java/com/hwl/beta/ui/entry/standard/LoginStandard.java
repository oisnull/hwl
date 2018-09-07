package main.java.com.hwl.beta.ui.entry.standard;

import java.util.function.Consumer;

import com.hwl.beta.ui.entry.action.ILoginListener;
import com.hwl.beta.ui.entry.bean.LoginBean;

public interface LoginStandard {

    LoginBean getLoginBean();

    void userLogin(LoginBean loginBean,Runnable succCallback,Consumer<String> errorCallback);
}