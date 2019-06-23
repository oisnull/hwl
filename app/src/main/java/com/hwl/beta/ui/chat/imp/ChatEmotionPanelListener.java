package com.hwl.beta.ui.chat.imp;

import android.app.Activity;
import android.widget.Toast;

import com.hwl.beta.emotion.interfaces.IEmotionPanelListener;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;

/**
 * Created by Administrator on 2019/06/23.
 */

public class ChatEmotionPanelListener implements IEmotionPanelListener {
    protected Activity context;

    public ChatEmotionPanelListener(Activity activity) {
        this.context = activity;
    }

    @Override
    public boolean onSendTextClick(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onSendImageClick(String key) {
        Toast.makeText(context, key, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendSoundClick(float seconds, String filePath) {
    }

    @Override
    public void onSelectImageClick() {
        UITransfer.toImageSelectActivity(context, ImageSelectType.CHAT_PUBLISH, 6, 1);
    }

    @Override
    public void onSelectVideoClick() {
        UITransfer.toVideoSelectActivity(context, 3);
    }

    @Override
    public void onSelectFavoriteClick() {
        Toast.makeText(context, "发送收藏信息功能稍后开放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraClick() {
        UITransfer.toSystemCamera(context, 2);
    }

    @Override
    public void onLocationClick() {
        Toast.makeText(context, "发送位置信息功能稍后开放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddClick() {
        Toast.makeText(context, "添加表情功能稍后开放...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSettingClick() {
        Toast.makeText(context, "表情设置功能稍后开放...", Toast.LENGTH_SHORT).show();
    }
}
