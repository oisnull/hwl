package com.hwl.beta.emotion.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

import com.hwl.beta.emotion.utils.EmotionUtils;

/**
 * Created by Administrator on 2018/2/22.
 */

public class EmotionEditText extends android.support.v7.widget.AppCompatEditText {
    private OnKeyListener keyListener;

    public EmotionEditText(Context context) {
        super(context);
    }

    public EmotionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EmotionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text), type);
        } else {
            super.setText(text, type);
        }
    }

    private CharSequence replace(CharSequence text) {
        return EmotionUtils.replaceEmotionText(getContext(), text);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    private class MyInputConnection extends InputConnectionWrapper {

        public MyInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (keyListener != null) {
                return keyListener.onKey(EmotionEditText.this, event.getKeyCode(), event);
            } else {
                return super.sendKeyEvent(event);
            }
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            // magic: in latest Android, deleteSurroundingText(1, 0) will be called for backspace
            if (beforeLength == 1 && afterLength == 0) {
                // backspace
                //return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    //设置监听回调
    public void setSoftKeyListener(OnKeyListener listener) {
        keyListener = listener;
    }
}
