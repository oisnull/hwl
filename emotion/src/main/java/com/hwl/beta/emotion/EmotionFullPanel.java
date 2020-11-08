package com.hwl.beta.emotion;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.hwl.beta.emotion.adapter.ChatExtendAdapter;
import com.hwl.beta.emotion.adapter.EmotionPagerAdapter;
import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.emotion.data.EmotionLocal;
import com.hwl.beta.emotion.interfaces.IEmotionItemListener;
import com.hwl.beta.emotion.interfaces.IEmotionPanelListener;
import com.hwl.beta.emotion.model.EmojiModel;
import com.hwl.beta.emotion.model.EmojiPageContainer;
import com.hwl.beta.emotion.utils.EmotionKeyboardUtils;
import com.hwl.beta.emotion.utils.EmotionUtils;
import com.hwl.beta.emotion.widget.AutoHeightLayout;
import com.hwl.beta.emotion.widget.EmotionEditText;
import com.hwl.beta.emotion.widget.EmotionFunctionLayout;
import com.hwl.beta.emotion.widget.EmotionFunctionViewPager;
import com.hwl.beta.emotion.widget.EmotionIndicatorView;
import com.hwl.beta.emotion.widget.EmotionToolBarView;

public class EmotionFullPanel extends AutoHeightLayout implements View.OnClickListener {
    public static final int FUNC_TYPE_EMOTION = -1;
    public static final int FUNC_TYPE_EXTENDS = -2;
    public static final String TAG = "EmotionFullPanel";

    LayoutInflater mInflater;
    Context context;
    ImageView ivVoice, ivKeyboard, ivEmotions, ivEmotionExtends;
    EmotionEditText etChatText;
    AudioRecorderButton btnVoiceRecord;
    Button btnSend;
    EmotionFunctionLayout eflEmotionFunction;

    //emotion function
    EmotionFunctionViewPager efvContainer;
    EmotionIndicatorView eivDotContainer;
    EmotionToolBarView etvEmotionBar;
    IEmotionPanelListener panelListener;
    Runnable onHeightChanged;

    public EmotionFullPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initEmotionPanel();
    }

    protected void initEmotionPanel() {
        View view = mInflater.inflate(R.layout.emotion_full_panel, this);
        ivVoice = view.findViewById(R.id.iv_voice);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        btnVoiceRecord = view.findViewById(R.id.btn_voice_record);
        etChatText = view.findViewById(R.id.et_chat_text);
        ivEmotions = view.findViewById(R.id.iv_emotions);
        ivEmotionExtends = view.findViewById(R.id.iv_emotion_extends);
        btnSend = view.findViewById(R.id.btn_send);
        eflEmotionFunction = view.findViewById(R.id.efl_emotion_function);
        View spLine = view.findViewById(R.id.sp_line);
        spLine.setFocusable(true);
        spLine.requestFocus();
        spLine.setFocusableInTouchMode(true);
        spLine.requestFocusFromTouch();

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        ivEmotions.setOnClickListener(this);
        ivEmotionExtends.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnVoiceRecord.setAudioFinishRecorderListener(new AudioRecorderButton
                .AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                panelListener.onSendSoundClick(seconds, filePath);
            }
        });

        View funcView = mInflater.inflate(R.layout.emotion_function_panel, null);
        efvContainer = funcView.findViewById(R.id.efv_container);
        eivDotContainer = funcView.findViewById(R.id.eiv_dot_container);
        etvEmotionBar = funcView.findViewById(R.id.etv_emotion_bar);

        View extendView = mInflater.inflate(R.layout.emotion_extends_panel, null);
        GridView gvExtends = extendView.findViewById(R.id.gv_extends);
        gvExtends.setAdapter(new ChatExtendAdapter(context));
        gvExtends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        panelListener.onSelectImageClick();
                        break;
                    case 1:
                        panelListener.onCameraClick();
                        break;
                    case 2:
                        panelListener.onLocationClick();
                        break;
                    case 3:
                        panelListener.onSelectVideoClick();
                        break;
                    case 4:
                        panelListener.onSelectFavoriteClick();
                        break;
                    default:
                        break;
                }
            }
        });

        eflEmotionFunction.addFuncView(FUNC_TYPE_EMOTION, funcView);
        eflEmotionFunction.addFuncView(FUNC_TYPE_EXTENDS, extendView);
        eflEmotionFunction.addOnKeyBoardListener(new EmotionFunctionLayout.OnFuncKeyBoardListener() {
            @Override
            public void OnFuncPop(int height) {
                if (onHeightChanged != null)
                    onHeightChanged.run();
            }

            @Override
            public void OnFuncClose() {
            }
        });
//        eflEmotionFunction.setOnFuncChangeListener(new EmotionFunctionLayout
//        .OnFuncChangeListener() {
//            @Override
//            public void onFuncChange(int key) {
//            }
//        });

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
        final EmojiPageContainer defaultEmojiContainer = new EmojiPageContainer.Builder()
                .setDefaultResId(R.drawable.chat_emotion)
                .setAllEmojis(EmotionLocal.defaultEmotions)
                .build();

//        final EmojiPageContainer extendEmojiContainer = new EmojiPageContainer.Builder()
//                .setLine(4)
//                .setRow(2)
//                .setDefaultResId(R.drawable.chat_add)
//                .setLastItemIsDeleteButton(false)
//                .setAllEmojis(EmotionExtends.extendEmotions)
//                .setShowTitle(true)
//                .build();

        final EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(context,
                new IEmotionItemListener() {
                    @Override
                    public void onItemClick(EmojiModel emoji) {
                        if (emoji.source == 0) {
                            EmotionUtils.addEmotion(etChatText, emoji.key);
                        } else {
                            panelListener.onSendImageClick(emoji.key);
                        }
                    }

                    @Override
                    public void onItemDeleteClick() {
                        EmotionUtils.deleteEmotion(etChatText);
                    }
                });
        emotionPagerAdapter.add(defaultEmojiContainer);
//        emotionPagerAdapter.add(extendEmojiContainer);

        efvContainer.setAdapter(emotionPagerAdapter);
        efvContainer.setCurrentItem(0);
        efvContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currIndex = 0;
            String currTag = null;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                EmojiPageContainer pageContainer = emotionPagerAdapter.getPageContainer(position);
                if (currTag != null && currTag != pageContainer.getId()) {
                    if (pageContainer.IsShowIndicator()) {
                        eivDotContainer.setVisibility(VISIBLE);
                        eivDotContainer.updateCount(pageContainer.getPageCount());
                    } else {
                        eivDotContainer.setVisibility(GONE);
                    }

                } else {
                    if (pageContainer.IsShowIndicator())
                        eivDotContainer.setSelectIndicator(position, currIndex);
                }
                etvEmotionBar.setSelected(pageContainer.getId());

                currIndex = position;
                currTag = pageContainer.getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        etvEmotionBar.addItemButton(defaultEmojiContainer.getDefaultResId(),
                defaultEmojiContainer.getId(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etvEmotionBar.setSelected(defaultEmojiContainer.getId());
                        efvContainer.setCurrentItem(0);
                    }
                });
//        etvEmotionBar.addItemButton(extendEmojiContainer.getDefaultResId(),
//                extendEmojiContainer.getId(), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        etvEmotionBar.setSelected(extendEmojiContainer.getId());
//                        efvContainer.setCurrentItem(emotionPagerAdapter
//                        .getPageContainerStartPosition(extendEmojiContainer.getId()));
//                    }
//                });
        etvEmotionBar.setSelected(defaultEmojiContainer.getId());
        eivDotContainer.updateCount(defaultEmojiContainer.getPageCount());
    }

    public void toggleEmotionView() {
        eflEmotionFunction.toggleFuncView(FUNC_TYPE_EMOTION, isSoftKeyboardPop(), etChatText);
    }

    public void toggleExtendsView() {
        eflEmotionFunction.toggleFuncView(FUNC_TYPE_EXTENDS, isSoftKeyboardPop(), etChatText);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        eflEmotionFunction.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        eflEmotionFunction.setVisibility(true);
        if (onHeightChanged != null)
            onHeightChanged.run();
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (eflEmotionFunction.isOnlyShowSoftKeyboard()) {
            reset();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && eflEmotionFunction.hasViewShow()) {
            reset();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_keyboard) {
            showKeyboard();
        } else if (id == R.id.iv_voice) {
            showVoice();
        } else if (id == R.id.iv_emotions) {
            showEmotions();
        } else if (id == R.id.iv_emotion_extends) {
            toggleExtendsView();
        } else if (id == R.id.btn_send) {
            if (panelListener.onSendTextClick(etChatText.getText() + "")) {
                etChatText.setText(null);
            }
        }
    }

    public void reset() {
        EmotionKeyboardUtils.closeSoftKeyboard(this);
        eflEmotionFunction.hideAllFuncView();
    }

    private void showVoice() {
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

        toggleEmotionView();
    }

    public void setEmotionPanelListener(IEmotionPanelListener panelListener) {
        this.panelListener = panelListener;
        if (panelListener != null)
            etvEmotionBar.setEmotionToolbarListener(panelListener);
    }

    public void setOnHeightChanged(Runnable onHeightChanged) {
        this.onHeightChanged = onHeightChanged;
    }
}
