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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.utils.EmotionUtils;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionDefaultPanelV2 extends LinearLayout {

    Context context;
    ImageView ivDefaultEmotions;
    EmotionEditText etMessage;
    LinearLayout llSysEmotion, llEmotionContainer;
    Button btnSend, btnCancel;
    ViewGroup contentContainerView;
    NoHorizontalScrollerViewPager vpEmotionContainer;
    InputMethodManager softInputManager;
    FragmentActivity activity;
    int localSoftInputHeight = 0;
    int contentContainerHeight = 0;
    IEmotionPanelListener panelListener;

    public EmotionDefaultPanelV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = ((FragmentActivity) context);

        init();
    }

    public EmotionDefaultPanelV2 setEmotionPanelListener(IEmotionPanelListener panelListener) {
        this.panelListener = panelListener;
        return this;
    }

    public EmotionDefaultPanelV2 setLocalSoftInputHeight(int localSoftInputHeight) {
        this.localSoftInputHeight = localSoftInputHeight;
        return this;
    }

    public EmotionDefaultPanelV2 setContentContainerView(ViewGroup contentContainerView) {
        this.contentContainerView = contentContainerView;
        return this;
    }

    public EmotionDefaultPanelV2 setContentContainerHeight(int contentContainerHeight) {
        if (this.contentContainerHeight <= 0) // just set once
            this.contentContainerHeight = contentContainerHeight - localSoftInputHeight;
        return this;
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_default_panel_v2,
                this, false);
        softInputManager = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        llSysEmotion = view.findViewById(R.id.ll_sys_emotion);
        etMessage = view.findViewById(R.id.et_message);
        ivDefaultEmotions = view.findViewById(R.id.iv_default_emotions);
        btnSend = view.findViewById(R.id.btn_send);
        btnCancel = view.findViewById(R.id.btn_cancel);
        llEmotionContainer = view.findViewById(R.id.ll_emotion_container);
        vpEmotionContainer = view.findViewById(R.id.vp_emotion_container);

        ivDefaultEmotions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultEmotion();
            }
        });
        llSysEmotion.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideEmotionPanel();
                }
                return false;
            }
        });
		
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panelListener != null)
                    panelListener.cancelClick();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panelListener != null){
					if(panelListener.sentClick(etMessage.getText()+"")){
						etMessage.setText("");
					}
				}                    
            }
        });

        this.bindEditTextEvents();
        this.bindSysEmotionData();
        this.addView(view);
    }

	public void setHintMessage(String hintText){
		etMessage.setHint(hintText);
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

    public void showKeyboard() {
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
        LayoutParams params = (LayoutParams) contentContainerView
                .getLayoutParams();
        params.height = contentContainerView.getHeight() > this.contentContainerHeight ? this
                .contentContainerHeight : contentContainerView.getHeight();
        params.height = params.height == 715 ? 728 : params.height;
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        etMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LayoutParams) contentContainerView.getLayoutParams()).weight = 1.0F;
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

	public interface IEmotionPanelListener{
		void cancelClick();
		boolean sentClick(String content);
	}
}
