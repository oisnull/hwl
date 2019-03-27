package com.hwl.beta.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwl.beta.AppConfig;
import com.hwl.beta.net.resx.IDownloadProgressListener;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2019/3/26.
 */

public class NetExceptionCode {
	IMServiceDisconnect(1, "The im service is disconnected."),
    ChatCodeUserValidateFailure(2, "Validate chat user failure"),
    ChatCodeUserBlack(3, "User is in black list"),
    ChatSendUserMessageFailure(4,"Send chat user message failure"),
    ChatVideoRescUploadFailure(5,"Chat video resx upload failure"),
    ChatVoiceRescUploadFailure(6,"Chat voice resx upload failure"),
    ChatImageRescUploadFailure(7,"Chat image resx upload failure"),
    ChatSendGroupMessageFailure(8,"Send chat group message failure"),
    ChatMessageContentTypeError(9,"Chat message content type error");

    private String desc;
    private int index;

   public NetExceptionCode(int index, String desc) {
        this.desc = desc;
        this.index = index;
    }

    public static String getDesc(int index) {
        for (NetExceptionCode c : NetExceptionCode.values()) {
            if (c.getIndex() == index) {
                return c.desc;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
