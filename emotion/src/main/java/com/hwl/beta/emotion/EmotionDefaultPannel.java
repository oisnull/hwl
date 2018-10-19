package com.hwl.beta.emotion;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwl.beta.emotion.adapter.ChatExtendAdapter;
import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionDefaultPannel extends LinearLayout {

    final Context currContext;
    final FragmentActivity activity;
    IDefaultEmotionListener defaultEmotionListener;
    IDefaultPannelListener pannelListener;

    ImageView ivChatEmotion;
    Button btnChatMessageSend;
    EmotionEditText etChatMessage;//消息文本内容
    NoHorizontalScrollerViewPager vpEmotionExtendContainer;

    public EmotionDefaultPannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.currContext = context;
        this.activity = ((FragmentActivity) currContext);

        //初始化绑定组件
        init();
    }

    public void setDefaultEmotionListener(IDefaultEmotionListener defaultEmotionListener) {
        this.defaultEmotionListener = defaultEmotionListener;
    }

    public void setDefaultPannelListener(IDefaultPannelListener pannelListener) {
        this.pannelListener = pannelListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init() {
        View view = LayoutInflater.from(currContext).inflate(R.layout.emotion_default_pannel, this, false);

        etChatMessage = view.findViewById(R.id.et_chat_message);
        ivChatEmotion = view.findViewById(R.id.iv_chat_emoticons);
        btnChatMessageSend = view.findViewById(R.id.btn_chat_message_send);
        vpEmotionExtendContainer = view.findViewById(R.id.vp_emotion_extend_container);

        List<Fragment> emotionExtendFragments = new ArrayList<>(1);
        EmotionTemplateFragment emotionTemplateFragment = new EmotionTemplateFragment();
        emotionTemplateFragment.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                addEmotion(etChatMessage, name);
            }

            @Override
            public void onDefaultItemDeleteClick() {
                deleteEmotion(etChatMessage);
            }
        });
        emotionExtendFragments.add(emotionTemplateFragment);
        vpEmotionExtendContainer.setAdapter(new EmotionExtendContainerPagerAdapter(activity.getSupportFragmentManager(), emotionExtendFragments));


        etChatMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vpEmotionExtendContainer.setVisibility(GONE);
                }
                return false;
            }
        });
        etChatMessage.setSoftKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.KEYCODE_DEL == KeyEvent.KEYCODE_DEL) {
                    deleteEmotion(etChatMessage);
                }
                return false;
            }
        });
        ivChatEmotion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSystemKeyboard(false);
                vpEmotionExtendContainer.setVisibility(VISIBLE);
            }
        });

        btnChatMessageSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pannelListener != null) {
                    if (pannelListener.onSendMessageClick(etChatMessage.getText() + "")) {
                        etChatMessage.setText("");
                    }
                }
            }
        });
        this.addView(view);
    }

    public void setHintText(String text) {
        etChatMessage.setHint(text);
    }

    public void setSendButtonText(String text) {
        btnChatMessageSend.setText(text);
    }

    public void setEditTextFocus(boolean isLose) {
        if (!isLose) {
            etChatMessage.setFocusable(true);
            etChatMessage.setFocusableInTouchMode(true);
        } else {
            etChatMessage.requestFocus();
        }
    }

    public void setSystemKeyboard(boolean isOpen) {
        InputMethodManager imm = (InputMethodManager) currContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isOpen) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            imm.hideSoftInputFromWindow(etChatMessage.getWindowToken(), 0);
        }
    }

    public void hideEmotionFunction() {
        setSystemKeyboard(false);
        this.setEditTextFocus(false);
    }

    public void addEmotion(EmotionEditText emotionEditText, String emotionName) {
        int start = emotionEditText.getSelectionStart();
        emotionEditText.setText(emotionEditText.getText().insert(start, emotionName));
        CharSequence info = emotionEditText.getText();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            Selection.setSelection(spanText, start + emotionName.length());
        }
    }

    public void deleteEmotion(EmotionEditText emotionEditText) {
        String content = emotionEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        int start = emotionEditText.getSelectionStart();
        Log.d("deleteEmotion", start + "-当前光标的位置");
        String startContent = content.substring(0, start);
        String endContent = content.substring(start, content.length());
        String lastContent = content.substring(start - 1, start);
        int last = startContent.lastIndexOf("[");
        int lastChar = startContent.substring(0, startContent.length() - 1).lastIndexOf("]");

        if ("]".equals(lastContent) && last > lastChar) {
            if (last != -1) {
                emotionEditText.setText(startContent.substring(0, last) + endContent);
                // 定位光标位置
                CharSequence info = emotionEditText.getText();
                if (info instanceof Spannable) {
                    Spannable spanText = (Spannable) info;
                    Selection.setSelection(spanText, last);
                }
                return;
            }
        }
        emotionEditText.setText(startContent.substring(0, start - 1) + endContent);
        // 定位光标位置
        CharSequence info = emotionEditText.getText();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            Selection.setSelection(spanText, start - 1);
        }
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    public int getSupportSoftInputHeight() {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }

        if (softInputHeight <= 0) {
            //Log.d("EmotionControlPannel", "EmotionKeyboard--Warning: value of softInputHeight is below zero!");
            return 787;
        }
//        //存一份到本地
//        if (softInputHeight > 0) {
//            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
//        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    /**
     * 获取软键盘高度，由于第一次直接弹出表情时会出现小问题，787是一个均值，作为临时解决方案
     *
     * @return
     */
    public int getKeyBoardHeight() {
        return 787;//sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 787);
    }

    public interface IDefaultPannelListener {
        boolean onSendMessageClick(String text);
    }
}
