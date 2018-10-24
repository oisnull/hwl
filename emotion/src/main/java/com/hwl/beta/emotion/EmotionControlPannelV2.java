package com.hwl.beta.emotion;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    ViewGroup contentContainerView;
    FragmentActivity activity;

    IEmotionPannelListener emotionEvent;
    IDefaultEmotionListener defaultEmotionListener;
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

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.emotion_control_pannel, this,
                false);

        tvLoading = view.findViewById(R.id.tv_loading);
        ivVoice = view.findViewById(R.id.iv_voice);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        btnVoiceRecord = view.findViewById(R.id.btn_voice_record);
        etChatMessage = view.findViewById(R.id.et_chat_message);
        ivSysEmotion = view.findViewById(R.id.iv_sys_emotion);
        ivExtendsEmotion = view.findViewById(R.id.iv_extends_emotion);
        btnMessageSend = view.findViewById(R.id.btn_message_send);
        gvExtendsEmotion = view.findViewById(R.id.gv_extends_emotion);

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        btnVoiceRecord.setOnClickListener(this);
        etChatMessage.setOnClickListener(this);
        ivSysEmotion.setOnClickListener(this);
        ivExtendsEmotion.setOnClickListener(this);
        btnMessageSend.setOnClickListener(this);

        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_voice) {

        }
    }
}
