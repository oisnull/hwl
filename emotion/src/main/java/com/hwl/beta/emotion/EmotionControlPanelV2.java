package com.hwl.beta.emotion;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hwl.beta.emotion.adapter.ChatExtendAdapter;
import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.emotion.utils.EmotionUtils;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionControlPanelV2 extends LinearLayout implements View.OnClickListener {

    Context context;
    InputMethodManager softInputManager;
    FragmentActivity activity;

    ViewGroup contentContainerView;
    int panelTheme = EmotionPanelTheme.EMOTION_ALL;
    int localSoftInputHeight = 0;
    int contentContainerHeight = 0;
    PanelListener panelListener;
    Runnable onPanelHeightChanged;

    ImageView ivVoice, ivKeyboard, ivSysEmotion, ivExtendsEmotion;//语音，键盘，表情，扩展
    AudioRecorderButton btnVoiceRecord;
    Button btnMessageSend;//语音录制按钮,文本发送按钮
    EmotionEditText etChatMessage;//消息文本内容
    GridView gvExtendsEmotion;//功能扩展（照片，拍照，位置）
    LinearLayout llEmotionContainer, llSysEmotion, llEmotionPanel;//默认表情
    RecyclerView rvEmotionExtends;//表情扩展
    NoHorizontalScrollerViewPager vpSysEmotionContainer;

    public EmotionControlPanelV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = ((FragmentActivity) context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.EmotionControlPanel);
        this.panelTheme = typedArray.getInteger(R.styleable.EmotionControlPanel_panelTheme_theme,
                EmotionPanelTheme.EMOTION_ALL);

        initView();

        typedArray.recycle();
    }

    public EmotionControlPanelV2 setLocalSoftInputHeight(int localSoftInputHeight) {
        this.localSoftInputHeight = localSoftInputHeight;
        return this;
    }

    public EmotionControlPanelV2 setContentContainerView(ViewGroup contentContainerView) {
        this.contentContainerView = contentContainerView;
        return this;
    }

    public EmotionControlPanelV2 setContentContainerHeight(int contentContainerHeight) {
        if (this.contentContainerHeight <= 0) // just set once
            this.contentContainerHeight = contentContainerHeight - localSoftInputHeight;
        return this;
    }

    public EmotionControlPanelV2 setEmotionPanelListener(PanelListener panelListener) {
        this.panelListener = panelListener;
        return this;
    }

    public EmotionControlPanelV2 setOnPanelHeightChanged(Runnable onPanelHeightChanged) {
        this.onPanelHeightChanged = onPanelHeightChanged;
        return this;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_control_panel, this,
                false);
        softInputManager = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);

        ivVoice = view.findViewById(R.id.iv_voice);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        btnVoiceRecord = view.findViewById(R.id.btn_voice_record);
        etChatMessage = view.findViewById(R.id.et_chat_message);
        ivSysEmotion = view.findViewById(R.id.iv_sys_emotion);
        ivExtendsEmotion = view.findViewById(R.id.iv_extends_emotion);
        btnMessageSend = view.findViewById(R.id.btn_message_send);
        gvExtendsEmotion = view.findViewById(R.id.gv_extends_emotion);
        llSysEmotion = view.findViewById(R.id.ll_sys_emotion);
        llEmotionContainer = view.findViewById(R.id.ll_emotion_container);
        vpSysEmotionContainer = view.findViewById(R.id.vp_sys_emotion_container);
        rvEmotionExtends = view.findViewById(R.id.rv_emotion_extends);
        llEmotionPanel = view.findViewById(R.id.ll_emotion_panel);

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        btnVoiceRecord.setOnClickListener(this);
        ivSysEmotion.setOnClickListener(this);
        ivExtendsEmotion.setOnClickListener(this);
        btnMessageSend.setOnClickListener(this);

        btnVoiceRecord.setAudioFinishRecorderListener(new AudioRecorderButton
                .AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                if (panelListener != null) {
                    panelListener.onSendSoundClick(seconds, filePath);
                }
            }
        });

        this.bindEditTextEvents();
        this.bindSysEmotionData();
        this.bindExtendsEmotionData();
        this.bindButtonEmotionActionBar();

        this.addView(view);
    }

    private void bindEditTextEvents() {
        etChatMessage.setOnClickListener(this);
        etChatMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showKeyboard();
                    if (onPanelHeightChanged != null)
                        onPanelHeightChanged.run();
                }
                return false;
            }
        });
        etChatMessage.setSoftKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.KEYCODE_DEL == KeyEvent.KEYCODE_DEL) {
                    EmotionUtils.deleteEmotion(etChatMessage);
                }
                return false;
            }
        });
        etChatMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showSendButton(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etChatMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String content = ((EditText) v).getText().toString();
                showSendButton(hasFocus && content != null && content.length() > 0);
            }
        });
    }

    private void bindSysEmotionData() {
        List<Fragment> emotionExtendFragments = new ArrayList<>(1);
        EmotionTemplateFragment emotionTemplateFragment = new EmotionTemplateFragment();
        emotionTemplateFragment.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                EmotionUtils.addEmotion(etChatMessage, name);
            }

            @Override
            public void onDefaultItemDeleteClick() {
                EmotionUtils.deleteEmotion(etChatMessage);
            }
        });
        emotionExtendFragments.add(emotionTemplateFragment);
        vpSysEmotionContainer.setAdapter(new EmotionExtendContainerPagerAdapter(activity
                .getSupportFragmentManager(), emotionExtendFragments));

    }

    private void bindExtendsEmotionData() {
        gvExtendsEmotion.setAdapter(new ChatExtendAdapter(context));
        gvExtendsEmotion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (panelListener != null) {
                    switch (position) {
                        case 0:
                            panelListener.onSelectImageClick();
                            break;
                        case 1:
                            panelListener.onCameraClick();
                            break;
                        case 2:
                            panelListener.onPositionClick();
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
            }
        });
    }

    private void bindButtonEmotionActionBar() {
        rvEmotionExtends.setAdapter(new EmotionExtendHorizontalAdapter(context, new
                EmotionExtendHorizontalAdapter.ItemListener() {
                    @Override
                    public void onCustomizeItemClick() {
//                        Toast.makeText(context, "onCustomizeItemClick", Toast.LENGTH_SHORT)
// .show();
                    }

                    @Override
                    public void onDefaultItemClick() {
//                        Toast.makeText(context, "onDefaultItemClick", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAddItemClick() {
                        Toast.makeText(context, "添加自定义表情功能稍后开放...", Toast.LENGTH_SHORT).show();
                    }
                }));
        rvEmotionExtends.setHasFixedSize(true);
        rvEmotionExtends.setLayoutManager(new GridLayoutManager(activity, 1, GridLayoutManager
                .HORIZONTAL, false));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_voice) {
            showVoice();
        } else if (id == R.id.iv_keyboard || id == R.id.et_chat_message) {
            showKeyboard();
        } else if (id == R.id.iv_sys_emotion) {
            showSysEmotion();
        } else if (id == R.id.iv_extends_emotion) {
            showExtendsEmotion();
        } else if (id == R.id.btn_message_send) {
            showSendButton(true);
            if (panelListener != null && panelListener.onSendMessageClick(etChatMessage.getText()
                    + "")) {
                etChatMessage.setText("");
            }
        }
        if (onPanelHeightChanged != null)
            onPanelHeightChanged.run();
    }

    private void showSendButton(boolean isShow) {
        if (isShow) {
            ivExtendsEmotion.setVisibility(View.GONE);
            btnMessageSend.setVisibility(View.VISIBLE);
        } else {
            ivExtendsEmotion.setVisibility(View.VISIBLE);
            btnMessageSend.setVisibility(View.GONE);
        }
    }

    private void showVoice() {
        ivVoice.setVisibility(View.GONE);
        ivKeyboard.setVisibility(View.VISIBLE);
        btnVoiceRecord.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.GONE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.GONE);
        llSysEmotion.setVisibility(View.GONE);

        hideSoftInput();
        hideEmotionContainer();
    }

    public void hideEmotionPanel() {
        this.showKeyboard(false);
    }

    public void showKeyboard() {
        this.showKeyboard(true);
    }

    private void showKeyboard(boolean isShowPanel) {
        ivVoice.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        etChatMessage.setVisibility(View.VISIBLE);
        gvExtendsEmotion.setVisibility(View.GONE);
        llSysEmotion.setVisibility(View.GONE);

        if (etChatMessage.getText().length() > 0) {
            ivExtendsEmotion.setVisibility(View.GONE);
            btnMessageSend.setVisibility(View.VISIBLE);
        } else {
            ivExtendsEmotion.setVisibility(View.VISIBLE);
            btnMessageSend.setVisibility(View.GONE);
        }

        if (isShowPanel) {
            lockContentHeight();
            showSoftInput();
            hideEmotionContainer();
            unlockContentHeightDelayed();
        } else {
            hideSoftInput();
            hideEmotionContainer();
        }
    }

    private void showSysEmotion() {
        ivVoice.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.GONE);
        llSysEmotion.setVisibility(View.VISIBLE);

        if (etChatMessage.getText().length() > 0) {
            ivExtendsEmotion.setVisibility(View.GONE);
            btnMessageSend.setVisibility(View.VISIBLE);
        } else {
            ivExtendsEmotion.setVisibility(View.VISIBLE);
            btnMessageSend.setVisibility(View.GONE);
        }

        lockContentHeight();
        hideSoftInput();
        showEmotionContainer();
        unlockContentHeightDelayed();
    }

    private void showExtendsEmotion() {
        ivVoice.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.VISIBLE);
        llSysEmotion.setVisibility(View.GONE);

        lockContentHeight();
        hideSoftInput();
        showEmotionContainer();
        unlockContentHeightDelayed();
    }

    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentContainerView
                .getLayoutParams();
        params.height = contentContainerView.getHeight() > this.contentContainerHeight ? this
                .contentContainerHeight : contentContainerView.getHeight();
        params.weight = 0.0F;
    }

    private void unlockContentHeightDelayed() {
        etChatMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) contentContainerView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }

    private void showEmotionContainer() {
        etChatMessage.postDelayed(new Runnable() {
            @Override
            public void run() {
                llEmotionContainer.getLayoutParams().height = localSoftInputHeight;
                llEmotionContainer.setVisibility(View.VISIBLE);
            }
        }, 200L);
    }

    private void hideEmotionContainer() {
        if (llEmotionContainer.isShown()) {
            llEmotionContainer.setVisibility(View.GONE);
        }
    }

    private void showSoftInput() {
        etChatMessage.requestFocus();
        softInputManager.showSoftInput(etChatMessage, 0);
    }

    private void hideSoftInput() {
        softInputManager.hideSoftInputFromWindow(etChatMessage.getWindowToken(), 0);
    }

    public interface PanelListener {
        boolean onSendMessageClick(String text);

        void onSendSoundClick(float seconds, String filePath);

        boolean onSendExtendsClick();

        void onSelectImageClick();

        void onSelectVideoClick();

        void onSelectFavoriteClick();

        void onCameraClick();

        void onPositionClick();

        void onFunctionPop(int popHeight);
    }
}
