package com.hwl.beta.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by Administrator on 2018/3/31.
 */

public class LoadingDialog {
    private static ProgressDialog mProgressDialog;

    public static void show(Activity activity) {
        show(activity, "正在提交数据...");
    }

    public static void show(Activity activity, String loadingText) {
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(loadingText);
        mProgressDialog.show();
    }

    public static void hide() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }
}
