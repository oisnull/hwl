package com.hwl.beta.emotion;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hwl.beta.emotion.adapter.EmotionPagerAdapter;
import com.hwl.beta.emotion.data.EmotionLocal;
import com.hwl.beta.emotion.model.EmojiPageContainer;
import com.hwl.beta.emotion.utils.EmotionKeyboardUtils;
import com.hwl.beta.emotion.widget.AutoHeightLayout;
import com.hwl.beta.emotion.widget.EmotionEditText;
import com.hwl.beta.emotion.widget.EmotionFunctionLayout;
import com.hwl.beta.emotion.widget.EmotionFunctionViewPagerV2;
import com.hwl.beta.emotion.widget.EmotionIndicatorViewV2;
import com.hwl.beta.emotion.widget.EmotionToolBarViewV2;

public class EmotionDefaultPanelV3 extends AutoHeightLayout implements View.OnClickListener {
    public static final int FUNC_TYPE_EMOTION = -1;
    public static final int FUNC_TYPE_EXTENDS = -2;

    LayoutInflater mInflater;
    Context context;
    ImageView ivVoice, ivKeyboard, ivEmotions, ivEmotionExtends;
    EmotionEditText etChatText;
    Button btnVoiceRecord, btnSend;
    EmotionFunctionLayout eflEmotionFunction;

    //emotion function
    EmotionFunctionViewPagerV2 efvContainer;
    EmotionIndicatorViewV2 eivDotContainer;
    EmotionToolBarViewV2 etvEmotionBar;

    protected boolean mDispatchKeyEventPreImeLock = false;

    public EmotionDefaultPanelV3(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initEmotionPanel();
    }

    protected void initEmotionPanel() {
        View view = mInflater.inflate(R.layout.emotion_control_panel_v3, this);
        ivVoice = view.findViewById(R.id.iv_voice);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        btnVoiceRecord = view.findViewById(R.id.btn_voice_record);
        etChatText = view.findViewById(R.id.et_chat_text);
        ivEmotions = view.findViewById(R.id.iv_emotions);
        ivEmotionExtends = view.findViewById(R.id.iv_emotion_extends);
        btnSend = view.findViewById(R.id.btn_send);
        eflEmotionFunction = view.findViewById(R.id.efl_emotion_function);

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        ivEmotions.setOnClickListener(this);
        ivEmotionExtends.setOnClickListener(this);

        View funcView = mInflater.inflate(R.layout.emotion_function_panel, null);
        efvContainer = funcView.findViewById(R.id.efv_container);
        eivDotContainer = funcView.findViewById(R.id.eiv_dot_container);
        etvEmotionBar = funcView.findViewById(R.id.etv_emotion_bar);

        eflEmotionFunction.addFuncView(FUNC_TYPE_EMOTION, funcView);
        eflEmotionFunction.setOnFuncChangeListener(new EmotionFunctionLayout.OnFuncChangeListener() {
            @Override
            public void onFuncChange(int key) {
//        if (FUNC_TYPE_EMOTION == key) {
//            ivEmotions.setImageResource(R.drawable.chat_emotion);
//        } else {
//            ivEmotions.setImageResource(R.drawable.chat_emotion);
//        }
//        checkVoice();
            }
        });

        initEditView();
        initEmotionData();
    }

    protected void initEditView() {
        etChatText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!etChatText.isFocused()) {
                    etChatText.setFocusable(true);
                    etChatText.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        etChatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    btnSend.setVisibility(VISIBLE);
                    ivEmotionExtends.setVisibility(GONE);
                } else {
                    ivEmotionExtends.setVisibility(VISIBLE);
                    btnSend.setVisibility(GONE);
                }
            }
        });
    }

    private void initEmotionData() {
        EmojiPageContainer defaultEmojiContainer = new EmojiPageContainer.Builder()
				.setDefaultResId(R.drawable.ic_emotion_default)
                .setAllEmojis(EmotionLocal.defaultEmotions)
                .build();
				
        EmojiPageContainer extendEmojiContainer = new EmojiPageContainer.Builder()
				.setLine(4)
				.setRow(2)
				.setLastItemIsDeleteButton(false)
                .setAllEmojis(EmotionExtends.extendEmotions)
				.setShowTitle(true)
                .build();

        EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(context);
        emotionPagerAdapter.add(defaultEmojiContainer);
        emotionPagerAdapter.add(extendEmojiContainer);
		emotionPagerAdapter.setEmotionListener(new IDefaultEmotionListener() {
			@Override
			public void onDefaultItemClick(EmojiModel emoji) {

			}

			@Override
			public void onDefaultItemDeleteClick() {
                //EmotionUtils.deleteEmotion(etChatText);
			}
		});

        efvContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currIndex = 0;
			String currTag = null;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                eivDotContainer.setSelectIndicator(position, currIndex);
                currIndex = position;

				EmojiPageContainer pageContainer = emotionPagerAdapter.getPageContainer(position);
				etvEmotionBar.setSelected(pageContainer.getId());
				if(currTag!=null&&currTag!=pageContainer.getId()){
					if (pageContainer.IsShowIndicator()) {
						eivDotContainer.setVisibility(VISIBLE);
						eivDotContainer.updateCount(pageContainer.getPageCount());
					} else {
						eivDotContainer.setVisibility(GONE);
					}
				}
				currTag = pageContainer.getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        efvContainer.setAdapter(emotionPagerAdapter);
        efvContainer.setCurrentItem(0);

        etvEmotionBar.addItemButton(defaultEmojiContainer.getDefaultResId(), defaultEmojiContainer.getId(),new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					etvEmotionBar.setSelected(defaultEmojiContainer.getId());
					efvContainer.setCurrentItem(0);
				}
			});
        //etvEmotionBar.addItemButton(R.drawable.chat_add, "default1");
        //etvEmotionBar.addItemButton(R.drawable.chat_context, "default2");
        //etvEmotionBar.addItemButton(R.drawable.chat_emotion, "default3");
        //etvEmotionBar.addItemButton(R.drawable.chat_emotion_add, "default4");
        //etvEmotionBar.addItemButton(R.drawable.chat_emotion_setting, "default5");
        //etvEmotionBar.addItemButton(R.drawable.chat_keyboard, "default6");
        //etvEmotionBar.addItemButton(R.drawable.chat_location, "default7");
        //etvEmotionBar.addItemButton(R.drawable.chat_take_photo, "default8");
        //etvEmotionBar.addItemButton(R.drawable.ic_emotion_default, "default9");
        //etvEmotionBar.addItemButton(R.drawable.chat_video, "default10");
    }

    protected void toggleFuncView(int key) {
        eflEmotionFunction.toggleFuncView(key, isSoftKeyboardPop(), etChatText);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        eflEmotionFunction.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        eflEmotionFunction.setVisibility(true);
//        onFuncChange(eflEmotionFunction.DEF_KEY);
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (eflEmotionFunction.isOnlyShowSoftKeyboard()) {
            reset();
        } else {
//            onFuncChange(eflEmotionFunction.getCurrentFuncKey());
        }
    }

//    public void addOnFuncKeyBoardListener(EmotionFunctionLayout.OnFuncKeyBoardListener l) {
//        eflEmotionFunction.addOnKeyBoardListener(l);
//    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_keyboard) {
            showKeyboard();
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == R.id.iv_voice) {
            showVoice();
        } else if (i == R.id.iv_emotions) {
            showEmotions();
            toggleFuncView(FUNC_TYPE_EMOTION);
        } else if (i == R.id.iv_emotion_extends) {
            toggleFuncView(FUNC_TYPE_EXTENDS);
        }
    }

    public void reset() {
        EmotionKeyboardUtils.closeSoftKeyboard(this);
        eflEmotionFunction.hideAllFuncView();
    }

    protected void showVoice() {
        ivVoice.setVisibility(View.GONE);
        ivKeyboard.setVisibility(View.VISIBLE);
        btnVoiceRecord.setVisibility(View.VISIBLE);
        etChatText.setVisibility(View.GONE);
        ivEmotionExtends.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);

        reset();
    }

    private void showKeyboard() {
        ivVoice.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        etChatText.setVisibility(View.VISIBLE);

        if (etChatText.getText().length() > 0) {
            ivEmotionExtends.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        } else {
            ivEmotionExtends.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        }
        EmotionKeyboardUtils.openSoftKeyboard(etChatText);
    }

    private void showEmotions() {
        ivVoice.setVisibility(View.VISIBLE);
        etChatText.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);

        if (etChatText.getText().length() > 0) {
            ivEmotionExtends.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        } else {
            ivEmotionExtends.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onBackKeyClick() {
//        if (eflEmotionFunction.isShown()) {
//            mDispatchKeyEventPreImeLock = true;
//            reset();
//        }
//    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (eflEmotionFunction.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (EmotionKeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (EmotionKeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }
}
