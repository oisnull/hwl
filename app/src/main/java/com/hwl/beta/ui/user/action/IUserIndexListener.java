package com.hwl.beta.ui.user.action;

import android.view.View;

/**
 * Created by Administrator on 2018/4/2.
 */

public interface IUserIndexListener {

    void onUserHeadClick();

    void onRemarkClick();

    void onCircleClick();

    void onAddUserClick(View view);

    void onSendMessageClick();

    void onDeleteClick();

    void onBlackClick();
}
