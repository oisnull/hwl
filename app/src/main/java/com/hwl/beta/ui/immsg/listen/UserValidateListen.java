package com.hwl.beta.ui.immsg.listen;

import com.hwl.im.common.DefaultConsumer;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.im.improto.ImMessageResponse;
import com.hwl.im.improto.ImUserValidateResponse;

public class UserValidateListen extends AbstractMessageListenExecutor<ImUserValidateResponse> {

    private DefaultConsumer<String> succCallback;
    private DefaultConsumer<String> failedCallback;
//    static Logger log = LogManager.getLogger(UserValidateListen.class.getName());

    public UserValidateListen(DefaultConsumer<String> succCallback) {
        this.succCallback = succCallback;
    }

    public UserValidateListen(DefaultConsumer<String> succCallback, DefaultConsumer<String> failedCallback) {
        this.succCallback = succCallback;
        this.failedCallback = failedCallback;
    }

    @Override
    public void success(ImUserValidateResponse response) {
        super.success(response);

//        log.debug("User validate {} , listen content : {}", response.getIsSuccess() ? "success" : "failed",
//                response.toString());

        if (response.getIsSuccess()) {
            if (this.succCallback != null)
                this.succCallback.accept(response.getSessionid());
        } else {
            if (this.failedCallback != null)
                this.failedCallback.accept(response.getMessage());
        }
    }

    @Override
    public void failed(int responseCode, String message) {
//        log.debug("User validate receive failed : {}", message);
        if (this.failedCallback != null)
            this.failedCallback.accept(message);
    }

    @Override
    public ImUserValidateResponse getResponse(ImMessageResponse response) {
        return response.getUserValidateResponse();
    }

}