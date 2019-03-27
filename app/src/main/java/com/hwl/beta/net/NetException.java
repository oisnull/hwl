package com.hwl.beta.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwl.beta.AppConfig;
import com.hwl.beta.net.resx.IDownloadProgressListener;

import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2019/3/26.
 */

public class NetException {
	private NetExceptionCode code;

    public NetException(NetExceptionCode code) {
        super(code.getDesc());
        this.code = code;
    }

    public CustomException(NetExceptionCode code, String msg) {
        super(msg);
        this.code = code;
    }

    public NetExceptionCode getCode() {
        return code;
    }
}
