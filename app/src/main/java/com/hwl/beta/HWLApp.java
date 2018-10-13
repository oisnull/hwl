package com.hwl.beta;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Administrator on 2018/1/24.
 */

public class HWLApp extends Application {

    protected static HWLApp application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;

//        AppInstallStatus.setFrist();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static HWLApp getContext() {
        return application;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
//            @Override
//            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
////                layout.setPrimaryColorsId(R.color.main_background, android.R.color.white);//全局设置主题颜色
//                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//            }
//        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
//            @Override
//            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
//                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
//            }
//        });
    }
}
