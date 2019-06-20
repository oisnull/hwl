package com.hwl.beta.emotion.interfaces;

/**
 * Created by Administrator on 2019/6/20.
 */

public interface IEmotionPanelListener {
    boolean onSendMessageClick(String text);

    void onSendSoundClick(float seconds, String filePath);

    boolean onSendExtendsClick();

    void onSelectImageClick();

    void onSelectVideoClick();

    void onSelectFavoriteClick();

    void onCameraClick();

    void onLocationClick();
}
