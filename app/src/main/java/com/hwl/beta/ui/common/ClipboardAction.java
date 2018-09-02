package com.hwl.beta.ui.common;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.EditText;

import com.hwl.beta.utils.StringUtils;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ClipboardAction {
    public static void copy(Context context, String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy", text);
        mClipboardManager.setPrimaryClip(clipData);
    }

    public static void paste(Context context, EditText editText) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        // 粘贴板有数据，并且是文本
        if (mClipboardManager.hasPrimaryClip()
                && mClipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item clipItem = mClipboardManager.getPrimaryClip().getItemAt(0);
            CharSequence text = clipItem.getText();
            if (StringUtils.isNotBlank(text + "") && editText != null) {
                editText.setText(text);
            }
        }
    }
}
