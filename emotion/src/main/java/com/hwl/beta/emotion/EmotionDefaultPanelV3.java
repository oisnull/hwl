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
        eflEmotionFunction.addFuncView(FUNC_TYPE_EMOTION, funcView);
        efvContainer = funcView.findViewById(R.id.efv_container);
        eivDotContainer = funcView.findViewById(R.id.eiv_dot_container);
        etvEmotionBar = funcView.findViewById(R.id.etv_emotion_bar);
//        efvContainer.setOnIndicatorListener(new EmotionFunctionViewPager
//        .OnEmoticonsPageViewListener() {
//            @Override
//            public void emoticonSetChanged(PageSetEntity pageSetEntity) {
//                etvEmotionBar.setToolBtnSelect(pageSetEntity.getUuid());
//            }
//
//            @Override
//            public void playTo(int position, PageSetEntity pageSetEntity) {
//                eivDotContainer.playTo(position, pageSetEntity);
//            }
//
//            @Override
//            public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
//                eivDotContainer.playBy(oldPosition, newPosition, pageSetEntity);
//            }
//        });
//        etvEmotionBar.setOnToolBarItemClickListener(new EmotionToolBarView
//        .OnToolBarItemClickListener() {
//            @Override
//            public void onToolBarItemClick(PageSetEntity pageSetEntity) {
////                efvContainer.setCurrentPageSet(pageSetEntity);
//            }
//        });
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
                .setAllEmojis(EmotionLocal.defaultEmotions)
                .build();

        EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(context);
        emotionPagerAdapter.add(defaultEmojiContainer);

        efvContainer.setAdapter(emotionPagerAdapter);
        efvContainer.setCurrentItem(0);
        efvContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currIndex = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                //页面在滑动后调用
            }

            @Override
            public void onPageSelected(int position) {
                //页面跳转完后调用
                eivDotContainer.setSelectIndicator(position, currIndex);
                currIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //页面状态改变时调用 1:表示正在滑动 ，2：表示滑动完毕，0：表示什么都没做
            }
        });

        if (defaultEmojiContainer.IsShowIndicator()) {
            eivDotContainer.setVisibility(VISIBLE);
            eivDotContainer.updateCount(defaultEmojiContainer.getPageCount());
        } else {
            eivDotContainer.setVisibility(GONE);
        }

        etvEmotionBar.AddItemButton(R.drawable.ic_emotion_default, "default");
        etvEmotionBar.AddItemButton(R.drawable.chat_add, "default1");
        etvEmotionBar.AddItemButton(R.drawable.chat_context, "default2");
        etvEmotionBar.AddItemButton(R.drawable.chat_emotion, "default3");
        etvEmotionBar.AddItemButton(R.drawable.chat_emotion_add, "default4");
        etvEmotionBar.AddItemButton(R.drawable.chat_emotion_setting, "default5");
        etvEmotionBar.AddItemButton(R.drawable.chat_keyboard, "default6");
        etvEmotionBar.AddItemButton(R.drawable.chat_location, "default7");
        etvEmotionBar.AddItemButton(R.drawable.chat_take_photo, "default8");
        etvEmotionBar.AddItemButton(R.drawable.ic_emotion_default, "default9");
        etvEmotionBar.AddItemButton(R.drawable.chat_video, "default10");
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

//    public boolean dispatchKeyEventInFullScreen(KeyEvent event) {
//        if (event == null) {
//            return false;
//        }
//        switch (event.getKeyCode()) {
//            case KeyEvent.KEYCODE_BACK:
//                if (EmotionKeyboardUtils.isFullScreen((Activity) getContext()) &&
//                eflEmotionFunction.isShown()) {
//                    reset();
//                    return true;
//                }
//            default:
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    boolean isFocused;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES
//                    .LOLLIPOP) {
//                        isFocused = etChatText.getShowSoftInputOnFocus();
//                    } else {
//                        isFocused = etChatText.isFocused();
//                    }
//                    if (isFocused) {
//                        etChatText.onKeyDown(event.getKeyCode(), event);
//                    }
//                }
//                return false;
//        }
//    }

//    public EmoticonsEditText getEtChat() {
//        return etChatText;
//    }
//
//    public Button getBtnVoice() {
//        return mBtnVoice;
//    }
//
//    public Button getBtnSend() {
//        return mBtnSend;
//    }
//
//    public EmoticonsFuncView getEmoticonsFuncView() {
//        return mEmoticonsFuncView;
//    }
//
//    public EmoticonsIndicatorView getEmoticonsIndicatorView() {
//        return mEmoticonsIndicatorView;
//    }
//
//    public EmoticonsToolBarView getEmoticonsToolBarView() {
//        return mEmoticonsToolBarView;
//    }
}
