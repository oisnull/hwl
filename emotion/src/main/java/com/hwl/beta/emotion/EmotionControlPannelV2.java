package com.hwl.beta.emotion;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.emotion.widget.EmotionEditText;

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionControlPannelV2 extends LinearLayout implements View.OnClickListener {

    Context context;
    InputMethodManager softInputManager;
    FragmentActivity activity;

    IEmotionPannelListener emotionEvent;
    IDefaultEmotionListener defaultEmotionListener;

    ViewGroup contentContainerView;
    int softInputHeight = 0;
    int pannelTheme = EmotionPannelTheme.EMOTION_ALL;

    TextView tvLoading;
    ImageView ivVoice, ivKeyboard, ivSysEmotion, ivExtendsEmotion;//语音，键盘，表情，扩展
    AudioRecorderButton btnVoiceRecord;
    Button btnMessageSend;//语音录制按钮,文本发送按钮
    EmotionEditText etChatMessage;//消息文本内容
    GridView gvExtendsEmotion;//功能扩展（照片，拍照，位置）
    LinearLayout llSysEmotion;//默认表情
    RecyclerView rvEmotionExtends;//表情扩展

    public EmotionControlPannelV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.activity = ((FragmentActivity) context);

        //获取扩展属性的定义，并设置默认值
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.EmotionControlPannel);
        this.pannelTheme = typedArray.getInteger(R.styleable.EmotionControlPannel_pannel_theme,
                EmotionPannelTheme.EMOTION_ALL);

        //初始化绑定组件
        initView();

        typedArray.recycle();
    }

    public EmotionControlPannelV2 setSoftInputHeight(int softInputHeight) {
        this.softInputHeight = softInputHeight;
        return this;
    }

    public EmotionControlPannelV2 setContentContainerView(ViewGroup contentContainerView) {
        this.contentContainerView = contentContainerView;
        return this;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_control_pannel, this,
                false);
        softInputManager = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);

        tvLoading = view.findViewById(R.id.tv_loading);
        ivVoice = view.findViewById(R.id.iv_voice);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        btnVoiceRecord = view.findViewById(R.id.btn_voice_record);
        etChatMessage = view.findViewById(R.id.et_chat_message);
        ivSysEmotion = view.findViewById(R.id.iv_sys_emotion);
        ivExtendsEmotion = view.findViewById(R.id.iv_extends_emotion);
        btnMessageSend = view.findViewById(R.id.btn_message_send);
        gvExtendsEmotion = view.findViewById(R.id.gv_extends_emotion);
        llSysEmotion = view.findViewById(R.id.ll_sys_emotion);

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        btnVoiceRecord.setOnClickListener(this);
        etChatMessage.setOnClickListener(this);
        ivSysEmotion.setOnClickListener(this);
        ivExtendsEmotion.setOnClickListener(this);
        btnMessageSend.setOnClickListener(this);

        etChatMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showKeyboard();
                }
                return false;
            }
        });

        this.addView(view);
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
    }

    private void showKeyboard() {
        ivVoice.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.GONE);
        llSysEmotion.setVisibility(View.GONE);
        showSoftInput();
    }

    private void showSysEmotion() {
        if (llSysEmotion.isShown()) return;
        llSysEmotion.setVisibility(View.VISIBLE);
        ivVoice.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.GONE);
        hideSoftInput();
    }

    private void showExtendsEmotion() {
        llSysEmotion.setVisibility(View.GONE);
        ivVoice.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.VISIBLE);
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        etChatMessage.requestFocus();
        etChatMessage.post(new Runnable() {
            @Override
            public void run() {
                softInputManager.showSoftInput(etChatMessage, 0);
            }
        });
    }

    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        softInputManager.hideSoftInputFromWindow(etChatMessage.getWindowToken(), 0);
    }
}
