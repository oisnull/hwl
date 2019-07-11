package com.hwl.beta.ui.circle.action;

import com.hwl.beta.db.entity.Circle;

public interface ICircleUserItemListener {
    void onItemNullViewClick();

    void onItemViewClick(Circle info);

    //void onBackImageClick();
}
