package com.hwl.beta.emotion.interfaces;

/**
 * Created by Administrator on 2019/6/20.
 */

public interface IEmotionPanelListener extends IEmotionToolbarListener {
    boolean onSendTextClick(String text);

    void onSendImageClick(String key);

    void onSendSoundClick(float seconds, String filePath);

    void onSelectImageClick();

    void onSelectVideoClick();

    void onSelectFavoriteClick();

    void onCameraClick();

    void onLocationClick();

//    void onHeightChanged(int height);
}
