package com.hwl.beta.ui.chat.action;

import android.widget.CompoundButton;

public interface IChatGroupSettingListener {
    void onGroupUsersClick();

    void onGroupNoteClick();

    void onGroupNameClick();

    void onMyNameClick();

    void onShieldCheckedChanged(CompoundButton buttonView, boolean isChecked);

    void onSearchClick();

    void onClearClick();

    void onExitClick();

    void onDismissClick();
}
