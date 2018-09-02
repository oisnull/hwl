package cn.sharesdk;

import android.content.Context;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;

public class MobShareUtils {

    public static void open(final Context context, final String appName, final String appDesc,
                            final String appLogoUrl, final String
                                    downloadPageUrl) {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(appName + "内测模式开启");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("想要了解你周围发生了什么有趣的事吗，快到这里来看看呗!");
        oks.setImageUrl(appLogoUrl);
//        oks.setImagePath(appLogoUrl);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl(downloadPageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(downloadPageUrl);
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if (platform.getName().equals(Wechat.NAME)) {
//                    shareWechat(context, appName, appDesc, appLogoUrl, downloadPageUrl);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setTitle(appName);  //分享标题
                    paramsToShare.setText(appDesc);   //分享文本
                    paramsToShare.setImageUrl(appLogoUrl);//网络图片rul
                    paramsToShare.setUrl(downloadPageUrl);   //网友点进链接后，可以看到分享的详情
                }
            }
        });
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                if (platform.getName().equals(Wechat.NAME)) {
//                    shareWechat(context, appName, appDesc, appLogoUrl, downloadPageUrl);
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
        // 启动分享GUI
        oks.show(context);
    }

    private static void shareWechat(Context context, String appName, String appDesc, String
            appLogoUrl,
                                    String downloadPageUrl) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
        sp.setTitle(appName);  //分享标题
        sp.setText(appDesc);   //分享文本
        sp.setImageUrl(appLogoUrl);//网络图片rul
        sp.setUrl(downloadPageUrl);   //网友点进链接后，可以看到分享的详情

        //3、非常重要：获取平台对象
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        // 执行分享
        wechat.share(sp);
    }
}
