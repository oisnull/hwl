package com.hwl.beta.emotion;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/1/5.
 */

public class NoHorizontalScrollerViewPager extends ViewPager {
    public NoHorizontalScrollerViewPager(Context context) {
        super(context);
    }

    public NoHorizontalScrollerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
