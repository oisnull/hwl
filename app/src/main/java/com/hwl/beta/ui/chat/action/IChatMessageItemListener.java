package com.hwl.beta.ui.chat.action;

import android.view.View;

/**
 * Created by Administrator on 2018/4/5.
 */

public interface IChatMessageItemListener {

    void onHeadImageClick(int position);

    boolean onChatItemLongClick(View view, int position);

    void onImageItemClick(int position);

    void onVideoItemClick(int position);

    void onAudioItemClick(View view, int position);

    void onFaildStatusClick(View view, int position);
}
