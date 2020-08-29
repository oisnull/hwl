package com.hwl.beta.ui.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.FragmentActivity;

import com.hwl.beta.R;

public class DialogUtils {
    private static AddFriendDialogFragment addFriendDialogFragment;
    private static LocationDialogFragment locationDialogFragment;
    private static LocationLoadingDialogFragment locationLoadingDialogFragment;
    private static Dialog userActionDialog;

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
            content, View.OnClickListener clickListener) {
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

    public static void showLocationLoadingDialog(FragmentActivity fragmentActivity) {
        if (locationLoadingDialogFragment == null) {
            locationLoadingDialogFragment = new LocationLoadingDialogFragment();
        }
        if (locationLoadingDialogFragment.isAdded())
            return;
        locationLoadingDialogFragment.show(fragmentActivity.getSupportFragmentManager(),
                "LocationLoadingDialogFragment");
    }

    public static void closeLocationLoadingDialog() {
        if (locationLoadingDialogFragment != null) {
            locationLoadingDialogFragment.dismiss();
            locationLoadingDialogFragment = null;
        }
    }

    public static void showUserActionDialog(final FragmentActivity activity, View.OnClickListener
            deleteUserClickListener, View.OnClickListener addBlackClickListener) {
        userActionDialog = new Dialog(activity, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(activity).inflate(
                R.layout.dialog_user_action, null);
        //初始化视图
        root.findViewById(R.id.btn_delete).setOnClickListener(deleteUserClickListener);
        root.findViewById(R.id.btn_add_black).setOnClickListener(addBlackClickListener);
        root.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userActionDialog.dismiss();
            }
        });
        userActionDialog.setContentView(root);
        Window dialogWindow = userActionDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = activity.getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        userActionDialog.show();
    }

    public static void closeUserActionDialog() {
        if (userActionDialog != null) {
            userActionDialog.dismiss();
            userActionDialog = null;
        }
    }
}
