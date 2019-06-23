package com.hwl.beta.emotion.interfaces;

import com.hwl.beta.emotion.model.EmojiModel;

/**
 * Created by Administrator on 2019/6/20.
 */

public interface IEmotionItemListener {
    void onItemClick(EmojiModel emoji);

    void onItemDeleteClick();
}
