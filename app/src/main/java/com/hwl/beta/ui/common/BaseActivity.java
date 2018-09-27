package com.hwl.beta.ui.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hwl.beta.swipeback.SwipeBackHelper;
import com.hwl.beta.ui.busbean.EventBusUtil;
import com.hwl.beta.ui.busbean.EventMessageModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/2/4.
 */

public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setSwipeRelateOffset(300);
        //ViewServer.get(this).addWindow(this);

        if (isRegisterEventBus()) {
            EventBusUtil.register(this);
        }
    }

    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
        if (isRegisterEventBus()) {
            EventBusUtil.unRegister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(EventMessageModel messageModel) {
        if (messageModel != null) {
            receiveEventMessage(messageModel);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(EventMessageModel messageModel) {
        if (messageModel != null) {
            receiveStickyEventMessage(messageModel);
        }
    }

    /**
     * 接收到eventbus分发到的事件
     *
     * @param messageModel 事件
     */
    protected void receiveEventMessage(EventMessageModel messageModel) {

    }

    /**
     * 接收到eventbus分发到的粘性事件
     *
     * @param messageModel 粘性事件
     */
    protected void receiveStickyEventMessage(EventMessageModel messageModel) {

    }
}
