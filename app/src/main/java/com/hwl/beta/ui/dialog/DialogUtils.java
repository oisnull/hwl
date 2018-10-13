package com.hwl.beta.ui.dialog;

import android.support.v4.app.FragmentActivity;
import android.view.View;

public class DialogUtils {
    private static AddFriendDialogFragment addFriendDialogFragment;
    private static LocationDialogFragment locationDialogFragment;

    public static void showAddFriendDialog(FragmentActivity fragmentActivity, String title,
                                           String content, View.OnClickListener clickListener) {
        if (addFriendDialogFragment == null) {
            addFriendDialogFragment = new AddFriendDialogFragment();
        }
        addFriendDialogFragment.setTitle(title);
        addFriendDialogFragment.setRemark(content);
        addFriendDialogFragment.setOnClickListener(clickListener);
        addFriendDialogFragment.show(fragmentActivity.getFragmentManager(),
                "AddFriendDialogFragment");
    }

    public static void closeAddFriendDialog() {
        if (addFriendDialogFragment != null) {
            addFriendDialogFragment.dismiss();
            addFriendDialogFragment = null;
        }
    }

    public static void showLocationDialog(FragmentActivity fragmentActivity, String title, String
            content, View.OnClickListener
            clickListener) {
        if (locationDialogFragment == null) {
            locationDialogFragment = new LocationDialogFragment();
        }
        locationDialogFragment.setTitleShow(title);
        locationDialogFragment.setContentShow(content);
        locationDialogFragment.setResetLocationClickListener(clickListener);
        locationDialogFragment.show(fragmentActivity.getSupportFragmentManager(),
                "LocationDialogFragment");
    }

    public static void closeLocationDialog() {
        if (locationDialogFragment != null) {
            locationDialogFragment.dismiss();
            locationDialogFragment = null;
        }
    }
}
