package com.hwl.beta.ui.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.hwl.beta.badge.Badge;
import com.hwl.beta.badge.QBadgeView;

import io.reactivex.functions.Action;

public class BadgeNumber {

    public static Badge bindBadgeView(Context context, View targetView) {
        return bindBadgeView(context, targetView, 0, null);
    }

    public static Badge bindBadgeView(Context context, View targetView, int number) {
        return bindBadgeView(context, targetView, number, null);
    }

    public static Badge bindBadgeView(Context context, View targetView, int number, final Action action) {
        Badge badge = new QBadgeView(context).bindTarget(targetView);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeNumber(number);
        badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
            @Override
            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                if (dragState == STATE_SUCCEED) {
                    if (action != null) {
                        try {
                            action.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return badge;
    }
}
