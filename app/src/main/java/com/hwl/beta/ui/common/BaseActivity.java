package com.hwl.beta.ui.common;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.hwl.beta.swipeback.SwipeBackHelper;

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
    }

    public void onResume() {
        super.onResume();
    }
}
