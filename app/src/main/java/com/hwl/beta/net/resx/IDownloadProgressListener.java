package com.hwl.beta.net.resx;

/**
 * Created by Administrator on 2018/3/5.
 */

public interface IDownloadProgressListener {

    void onProcess(boolean isComplete, long totalByte, long currentByte);

}
