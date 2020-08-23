package com.hwl.beta.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hwl.beta.BuildConfig;
import com.hwl.beta.R;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.CustLog;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusUtil;

/**
 * Created by Administrator on 2018/1/4.
 */

public class TestActivity extends BaseActivity {

    Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);
        mActivity = this;


        Button btnPackage = findViewById(R.id.btn_friend_request_add);
        btnPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageCountSP.setFriendRequestCount(MessageCountSP.getFriendRequestCount() + 1);
                EventBusUtil.sendFriendRequestEvent();
            }
        });

        Button btnLocationTest = findViewById(R.id.btn_location_test);
        btnLocationTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toTestActivityLocation(mActivity);
            }
        });

        Button btnImTest = findViewById(R.id.btn_im_test);
        btnImTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toTestActivityIM(mActivity);
            }
        });

        Button btnViewLogs = findViewById(R.id.btn_view_logs);
        btnViewLogs.setOnClickListener(v -> {
            UITransfer.toTestActivityLogs(mActivity);
        });

//        Button button = findViewById(R.id.btn_test);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MessageNotifyManage.play();
//            }
//        });
//
//        Button btnShareTest = findViewById(R.id.btn_share_test);
//        btnShareTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showShare();
//            }
//        });
//
//        Button btnPackage = findViewById(R.id.btn_package);
//        btnPackage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mActivity, AppUtils.getAppPackageName(), Toast.LENGTH_SHORT)
// .show();
//            }
//        });
    }

    private void showShare() {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("分享测试");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//
//// 启动分享GUI
//        oks.show(this);
    }
}
