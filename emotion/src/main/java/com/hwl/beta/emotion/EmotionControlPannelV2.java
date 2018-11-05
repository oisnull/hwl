package com.hwl.beta.emotion;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.emotion.widget.EmotionEditText;

import java.util.ArrayList;
import java.util.List;

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
    int pannelTheme = EmotionPannelTheme.EMOTION_ALL;
    int localSoftInputHeight = 0;

    ImageView ivVoice, ivKeyboard, ivSysEmotion, ivExtendsEmotion;//语音，键盘，表情，扩展
    AudioRecorderButton btnVoiceRecord;
    Button btnMessageSend;//语音录制按钮,文本发送按钮
    EmotionEditText etChatMessage;//消息文本内容
    GridView gvExtendsEmotion;//功能扩展（照片，拍照，位置）
    LinearLayout llEmotionContainer, llSysEmotion;//默认表情
    RecyclerView rvEmotionExtends;//表情扩展
    NoHorizontalScrollerViewPager vpSysEmotionContainer;

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

    public EmotionControlPannelV2 setLocalSoftInputHeight(int localSoftInputHeight) {
        this.localSoftInputHeight = localSoftInputHeight;
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

        ivVoice.setOnClickListener(this);
        ivKeyboard.setOnClickListener(this);
        btnVoiceRecord.setOnClickListener(this);
        etChatMessage.setOnClickListener(this);
        ivSysEmotion.setOnClickListener(this);
        ivExtendsEmotion.setOnClickListener(this);
        btnMessageSend.setOnClickListener(this);
        bindSysEmotionData();

        etChatMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && isShowEmotionContainer()) {
                    showKeyboard();
                }
                return false;
            }
        });

        this.addView(view);
    }

    private void bindSysEmotionData() {
        List<Fragment> emotionExtendFragments = new ArrayList<>(1);
        EmotionTemplateFragment emotionTemplateFragment = new EmotionTemplateFragment();
        emotionTemplateFragment.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                if (pannelTheme == EmotionPannelTheme.EMOTION) {
//                    Toast.makeText(activity,name,Toast.LENGTH_SHORT).show();
                    if (defaultEmotionListener != null) {
                        defaultEmotionListener.onDefaultItemClick(name);
                    }
                } else {
//                    int start = etChatMessage.getSelectionStart();
//                    etChatMessage.setText(etChatMessage.getText().insert(start, name));
//                    CharSequence info = etChatMessage.getText();
//                    if (info instanceof Spannable) {
//                        Spannable spanText = (Spannable) info;
//                        Selection.setSelection(spanText, start + name.length());
//                    }
//                    addEmotion(etChatMessage,name);
                }
            }

            @Override
            public void onDefaultItemDeleteClick() {
                if (pannelTheme == EmotionPannelTheme.EMOTION) {
                    if (defaultEmotionListener != null) {
                        defaultEmotionListener.onDefaultItemDeleteClick();
                    }
                } else {
//                    deleteEmotion(etChatMessage);
                }
            }
        });
        emotionExtendFragments.add(emotionTemplateFragment);
        vpSysEmotionContainer.setAdapter(new EmotionExtendContainerPagerAdapter(activity
                .getSupportFragmentManager(), emotionExtendFragments));

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
        hideEmotionContainer(false);
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
        if (isShowEmotionContainer()) {
            lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
            hideEmotionContainer(true);//隐藏表情布局，显示软件盘

            //软件盘显示后，释放内容高度
            etChatMessage.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unlockContentHeightDelayed();
                }
            }, 200L);
        }
    }

    private void showSysEmotion() {
        ivVoice.setVisibility(View.VISIBLE);
        etChatMessage.setVisibility(View.VISIBLE);
        ivExtendsEmotion.setVisibility(View.VISIBLE);
        ivKeyboard.setVisibility(View.GONE);
        btnVoiceRecord.setVisibility(View.GONE);
        btnMessageSend.setVisibility(View.GONE);
        gvExtendsEmotion.setVisibility(View.GONE);
        llSysEmotion.setVisibility(View.VISIBLE);

        if (isShowEmotionContainer()) {
            lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
            hideEmotionContainer(true);//隐藏表情布局，显示软件盘
            unlockContentHeightDelayed();//软件盘显示后，释放内容高度
        } else {
            if (isSoftInputShown()) {//同上
                lockContentHeight();
                hideSoftInput();
                etChatMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showEmotionContainer();
                    }
                }, 200L);
                unlockContentHeightDelayed();
            } else {
                etChatMessage.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showEmotionContainer();
                    }
                }, 200L);
            }
        }
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
    }

    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentContainerView
                .getLayoutParams();
        params.height = contentContainerView.getHeight();
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
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = this.localSoftInputHeight;
        }
        llEmotionContainer.getLayoutParams().height = softInputHeight;
        llEmotionContainer.setVisibility(View.VISIBLE);
    }

    private void hideEmotionContainer(boolean showSoftInput) {
        if (llEmotionContainer.isShown()) {
            llEmotionContainer.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    private boolean isShowEmotionContainer() {
        return llEmotionContainer.isShown();
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

    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }

    private int getSupportSoftInputHeight() {
        Rect r = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
        int softInputHeight = screenHeight - r.bottom;
        if (Build.VERSION.SDK_INT >= 20) {
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        return softInputHeight;
    }

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
}
