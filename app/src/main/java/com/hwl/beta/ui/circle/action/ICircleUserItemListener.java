package com.hwl.beta.ui.circle.action;

import com.hwl.beta.db.ext.CircleExt;

public interface ICircleUserItemListener {
    void onItemNullViewClick();

    void onItemViewClick(CircleExt info);

    void onBackImageClick();
}
