package com.hwl.beta.ui.common;

import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.AppUtils;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareTransfer {

    public static final String APP_DOWNLOAD_URL = "http://111.229.252.205:8082/home/shareapp";
    public static final String APP_LOGO_URL = "http://111.229.252.205:8082/images/logo.png";

    public static void shareApp() {

        //MobShareUtils.open(HWLApp.getContext(), AppUtils.getAppName(),
        //       "想要了解你周围发生了什么有趣的事吗，快到这里来看看呗!", APP_LOGO_URL, APP_DOWNLOAD_URL);

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(AppUtils.getAppName() + "内测模式开启");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("想要了解你周围发生了什么有趣的事吗，快到这里来看看呗!");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(APP_LOGO_URL);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl(APP_DOWNLOAD_URL);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(APP_DOWNLOAD_URL);
        // 启动分享GUI
        oks.show(HWLApp.getContext());
    }
}
