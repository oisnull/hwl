package com.hwl.beta.api.dialog;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class TokenInvalidDialog {

    private static TokenInvalidDialog instance = null;

    public static TokenInvalidDialog getInstance() {
        if (instance == null) {
            instance = new TokenInvalidDialog();
        }
        return instance;
    }

    public void show(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setMessage("TOKEN过期,请重新登录!")
                .setPositiveButton("确定", (dialog, which) -> {

                }).create();
        alertDialog.show();
    }
}
