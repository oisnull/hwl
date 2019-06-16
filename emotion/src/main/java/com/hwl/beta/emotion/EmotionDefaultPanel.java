package com.hwl.beta.emotion;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.interfaces.IDefaultEmotionListener;
import com.hwl.beta.emotion.utils.EmotionUtils;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionDefaultPanel extends LinearLayout {

    Context context;
    ImageView ivDefaultEmotions;
    EmotionEditText etMessage;
    LinearLayout llSysEmotion, llEmotionContainer;
    ViewGroup contentContainerView;
    NoHorizontalScrollerViewPager vpEmotionContainer;
    InputMethodManager softInputManager;
    FragmentActivity activity;
    int localSoftInputHeight = 0;
    int contentContainerHeight = 0;

    public EmotionDefaultPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = ((FragmentActivity) context);

        init();
    }

    public EmotionDefaultPanel setLocalSoftInputHeight(int localSoftInputHeight) {
        this.localSoftInputHeight = localSoftInputHeight;
        return this;
    }

    public EmotionDefaultPanel setContentContainerView(ViewGroup contentContainerView) {
        this.contentContainerView = contentContainerView;
        return this;
    }

    public EmotionDefaultPanel setContentContainerHeight(int contentContainerHeight) {
        if (this.contentContainerHeight <= 0) // just set once
            this.contentContainerHeight = contentContainerHeight - localSoftInputHeight;
        return this;
    }

    public EmotionDefaultPanel setEditText(EmotionEditText et) {
        this.etMessage = et;
        this.bindEditTextEvents();
        return this;
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_default_panel,
                this, false);
        softInputManager = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        llSysEmotion = view.findViewById(R.id.ll_sys_emotion);
        llEmotionContainer = view.findViewById(R.id.ll_emotion_container);
        vpEmotionContainer = view.findViewById(R.id.vp_emotion_container);
        ivDefaultEmotions = view.findViewById(R.id.iv_default_emotions);
        ivDefaultEmotions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultEmotion();
            }
        });
        llSysEmotion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideEmotionPanel();
                }
                return false;
            }
        });

        this.bindSysEmotionData();
        this.addView(view);
    }

    private void bindSysEmotionData() {
        List<Fragment> emotionExtendFragments = new ArrayList<>(1);
        EmotionTemplateFragment emotionTemplateFragment = new EmotionTemplateFragment();
        emotionTemplateFragment.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                EmotionUtils.addEmotion(etMessage, name);
            }

            @Override
            public void onDefaultItemDeleteClick() {
                EmotionUtils.deleteEmotion(etMessage);
            }
        });
        emotionExtendFragments.add(emotionTemplateFragment);
        vpEmotionContainer.setAdapter(new EmotionExtendContainerPagerAdapter(activity
                .getSupportFragmentManager(), emotionExtendFragments));

    }

    private void bindEditTextEvents() {
        etMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });
//        etMessage.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    showKeyboard();
//                }
//                return false;
//            }
//        });
        etMessage.setSoftKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.KEYCODE_DEL == KeyEvent.KEYCODE_DEL) {
                    EmotionUtils.deleteEmotion(etMessage);
                }
                return false;
            }
        });
    }

    public void hideEmotionPanel() {
        this.setVisibility(GONE);
        hideSoftInput();
    }

    private void showKeyboard() {
        if (!this.isShown())
            this.setVisibility(VISIBLE);
        lockContentHeight();
        showSoftInput();
        hideEmotionContainer();
        unlockContentHeightDelayed();
    }

    private void showDefaultEmotion() {
        if (llEmotionContainer.isShown()) {
            showKeyboard();
        } else {
            lockContentHeight();
            hideSoftInput();
            showEmotionContainer();
            unlockContentHeightDelayed();
        }
    }

    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentContainerView
                .getLayoutParams();
        params.height = contentContainerView.getHeight() > this.contentContainerHeight ? this
                .contentContainerHeight : contentContainerView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        etMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) contentContainerView.getLayoutParams()).weight = 1.0F;
            }
        }, 300L);
    }

    private void showEmotionContainer() {
        etMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                llEmotionContainer.getLayoutParams().height = localSoftInputHeight;
                llEmotionContainer.setVisibility(View.VISIBLE);
            }
        }, 300L);
    }

    private void hideEmotionContainer() {
        if (llEmotionContainer.isShown()) {
            llEmotionContainer.setVisibility(View.GONE);
        }
    }

    private void showSoftInput() {
        etMessage.requestFocus();
        softInputManager.showSoftInput(etMessage, 0);
    }

    private void hideSoftInput() {
        softInputManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
    }
}
