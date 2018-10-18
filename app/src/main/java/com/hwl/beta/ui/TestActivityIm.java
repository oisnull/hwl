package com.hwl.beta.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.hwl.beta.R;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.utils.DateUtils;
import com.hwl.imcore.improto.ImTestConnectionMessageResponse;

import java.util.Date;

public class TestActivityIm extends BaseActivity {

    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_im);
        mActivity = this;

        initView();
    }

    private void initView() {
        final TextView tvToServer = findViewById(R.id.tv_to_server);
        final TextView tvReceiveServer = findViewById(R.id.tv_receive_server);
        tvToServer.setText("开始发送IM测试数据 ......");

        IMClientEntry.sendTestConnectMessage(new IMDefaultSendOperateListener<ImTestConnectionMessageResponse>
                ("TestContent", true) {
            @Override
            public void success1() {
                tvToServer.setText("IM数据发送到服务器成功.");
                tvReceiveServer.setText("开始接收服务器返回的数据 ......");
            }

            @Override
            public void failed1() {
                tvToServer.setText("IM数据发送到服务器失败.");
            }

            @Override
            public void success2(ImTestConnectionMessageResponse content) {
                tvReceiveServer.setText("接收服务器返回的数据：" + content.getContent() + " , " + DateUtils
                        .dateToStrLong(new Date
                        (content.getSendTime())));
            }

            @Override
            public void failed2(String message) {
                tvReceiveServer.setText("接收服务器返回的数据：" + message);
            }
        });
    }
}
