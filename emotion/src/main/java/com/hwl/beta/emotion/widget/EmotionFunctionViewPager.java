package com.hwl.beta.emotion.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EmotionFunctionViewPager extends ViewPager {

    public EmotionFunctionViewPager(@NonNull Context context) {
        super(context);
    }

    public EmotionFunctionViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    float mLastX;
    float mLastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0) {
            float x = ev.getX();
            float y = ev.getY();
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    float xDiff = Math.abs(x - mLastY);
                    float yDiff = Math.abs(y - mLastY);
                    //在第一页，判断到是向左边滑动，即想滑动第二页
                    if (xDiff > 0 && x - mLastX < 0 && xDiff * 0.5f > yDiff) {
                        //告诉父容器不要拦截事件
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
            mLastX = x;
            mLastY = y;
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(ev);
    }

}
