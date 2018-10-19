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

public class EmotionControlPannel extends LinearLayout implements View.OnClickListener {

    final Context currContext;
    final FragmentActivity activity;
    private IEmotionPannelListener emotionEvent;
    private IDefaultEmotionListener defaultEmotionListener;
    private int pannelTheme = EmotionPannelTheme.VOICE_TEXT_EMOTION;
    //private int currentViewIndex = -1;
    private TextView tvLoading;
//    private byte messageType = 0;

    ImageView ivChatVoice, ivChatKeyboard, ivChatEmotion, ivChatExtends;//语音，键盘，表情，扩展
    AudioRecorderButton btnChatVoice;
    Button btnChatMessageSend;//语音录制按钮,文本发送按钮
    EmotionEditText etChatMessage;//消息文本内容
    GridView gvChatExtends;//功能扩展（照片，拍照，位置）
    LinearLayout llEmotionExtends;//表情扩展
    RecyclerView rvEmotionExtends;//表情扩展操作

    public EmotionControlPannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.currContext = context;
        this.activity = ((FragmentActivity) currContext);

        //获取扩展属性的定义，并设置默认值
        TypedArray typedArray = context.obtainStyledAttributes(attrs
                , R.styleable.EmotionControlPannel);
        this.pannelTheme = typedArray.getInteger(R.styleable.EmotionControlPannel_pannel_theme, EmotionPannelTheme.VOICE_TEXT_EMOTION);

        //初始化绑定组件
        init();

        typedArray.recycle();
    }

    public void setEmotionPannelListener(IEmotionPannelListener emotionEvent) {
        this.emotionEvent = emotionEvent;
    }

    public void setDefaultEmotionListener(IDefaultEmotionListener defaultEmotionListener){
        this.defaultEmotionListener=defaultEmotionListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private void init() {
        View view = LayoutInflater.from(currContext).inflate(R.layout.emotion_control_pannel, this, false);

        tvLoading = view.findViewById(R.id.tv_loading);
        ivChatVoice = view.findViewById(R.id.iv_chat_voice);
        ivChatKeyboard = view.findViewById(R.id.iv_chat_keyboard);
        btnChatVoice = view.findViewById(R.id.btn_chat_voice);
        etChatMessage = view.findViewById(R.id.et_chat_message);
        ivChatEmotion = view.findViewById(R.id.iv_chat_emoticons);
        ivChatExtends = view.findViewById(R.id.iv_chat_extends);
        btnChatMessageSend = view.findViewById(R.id.btn_chat_message_send);
        gvChatExtends = view.findViewById(R.id.gv_chat_extends);//发送功能扩展
        gvChatExtends.setAdapter(new ChatExtendAdapter(currContext));//加载功能扩展
        gvChatExtends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(currContext, "当前图标：" + position, Toast.LENGTH_SHORT).show();
                if (emotionEvent != null) {
                    switch (position) {
                        case 0:
                            emotionEvent.onSelectImageClick();
                            break;
                        case 1:
                            emotionEvent.onCameraClick();
                            break;
                        case 2:
                            emotionEvent.onPositionClick();
                            break;
                        case 3:
                            emotionEvent.onSelectVideoClick();
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        NoHorizontalScrollerViewPager vpEmotionExtendContainer = view.findViewById(R.id.vp_emotion_extend_container);
        List<Fragment> emotionExtendFragments = new ArrayList<>(1);
        EmotionTemplateFragment emotionTemplateFragment = new EmotionTemplateFragment();
        emotionTemplateFragment.setDefaultEmotionListener(new IDefaultEmotionListener() {
            @Override
            public void onDefaultItemClick(String name) {
                if (pannelTheme == EmotionPannelTheme.EMOTION) {
//                    Toast.makeText(activity,name,Toast.LENGTH_SHORT).show();
                    if(defaultEmotionListener!=null){
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
                    addEmotion(etChatMessage,name);
                }
            }

            @Override
            public void onDefaultItemDeleteClick() {
                if (pannelTheme == EmotionPannelTheme.EMOTION) {
                    if(defaultEmotionListener!=null){
                        defaultEmotionListener.onDefaultItemDeleteClick();
                    }
                } else {
                    deleteEmotion(etChatMessage);
                }
            }
        });
        emotionExtendFragments.add(emotionTemplateFragment);
        vpEmotionExtendContainer.setAdapter(new EmotionExtendContainerPagerAdapter(activity.getSupportFragmentManager(), emotionExtendFragments));

        rvEmotionExtends = view.findViewById(R.id.rv_emotion_extends);
        rvEmotionExtends.setAdapter(new EmotionExtendHorizontalAdapter(currContext));
        rvEmotionExtends.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        rvEmotionExtends.setLayoutManager(new GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false));
        if(pannelTheme==EmotionPannelTheme.EMOTION){
            rvEmotionExtends.setVisibility(GONE);
        }

        llEmotionExtends = view.findViewById(R.id.ll_emtion_extends);

        ivChatVoice.setOnClickListener(this);
        ivChatKeyboard.setOnClickListener(this);
        btnChatVoice.setOnClickListener(this);
        etChatMessage.setOnClickListener(this);
        ivChatEmotion.setOnClickListener(this);
        ivChatExtends.setOnClickListener(this);
        btnChatMessageSend.setOnClickListener(this);

        btnChatVoice.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                if (emotionEvent != null) {
                    emotionEvent.onSendSoundClick(seconds, filePath);
                }
            }
        });

        etChatMessage.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (emotionEvent != null && event.getAction() == MotionEvent.ACTION_DOWN) {
                    emotionEvent.onFunctionPop(getSupportSoftInputHeight());
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
        etChatMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String content = ((EditText) v).getText().toString();
                //Toast.makeText(activity,content,Toast.LENGTH_SHORT).show();
                if (hasFocus) {
                    gvChatExtends.setVisibility(View.GONE);
                    if (content != null && content.length() > 0) {
                        btnChatMessageSend.setVisibility(View.VISIBLE);
                        ivChatExtends.setVisibility(View.GONE);
                        llEmotionExtends.setVisibility(View.GONE);
                    }
                } else {

                }
                //showEmotionFunction();
            }
        });

        etChatMessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    ivChatExtends.setVisibility(View.GONE);
                    btnChatMessageSend.setVisibility(View.VISIBLE);
                } else {
                    ivChatExtends.setVisibility(View.VISIBLE);
                    btnChatMessageSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        switch (pannelTheme) {
            case EmotionPannelTheme.VOICE_TEXT_EMOTION:
            default:
                break;
            case EmotionPannelTheme.EMOTION:
                ivChatVoice.setVisibility(View.GONE);
                ivChatKeyboard.setVisibility(View.GONE);
                btnChatVoice.setVisibility(View.GONE);
                etChatMessage.setVisibility(View.GONE);
                ivChatExtends.setVisibility(View.GONE);
                btnChatMessageSend.setVisibility(View.GONE);
                llEmotionExtends.setVisibility(View.GONE);
                break;
        }

        this.addView(view);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_chat_voice) {
            ivChatVoice.setVisibility(View.GONE);
            ivChatKeyboard.setVisibility(View.VISIBLE);
            btnChatVoice.setVisibility(View.VISIBLE);
            etChatMessage.setVisibility(View.GONE);
            ivChatExtends.setVisibility(View.VISIBLE);
            btnChatMessageSend.setVisibility(View.GONE);
            gvChatExtends.setVisibility(View.GONE);
            llEmotionExtends.setVisibility(View.GONE);
            setSystemKeyboard(false);
            this.setEditTextFocus(false);

        } else if (i == R.id.iv_chat_keyboard) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboard.setVisibility(View.GONE);
            btnChatVoice.setVisibility(View.GONE);
            etChatMessage.setVisibility(View.VISIBLE);
            ivChatExtends.setVisibility(View.VISIBLE);
            btnChatMessageSend.setVisibility(View.GONE);
            gvChatExtends.setVisibility(View.GONE);
            llEmotionExtends.setVisibility(View.GONE);
            this.setEditTextFocus(true);
            this.setSystemKeyboard(true);

        } else if (i == R.id.btn_chat_voice) {
        } else if (i == R.id.et_chat_message) {
            gvChatExtends.setVisibility(View.GONE);
            llEmotionExtends.setVisibility(View.GONE);
            //this.setEditTextFocus(false);
            String content = etChatMessage.getText() + "";
            //Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
            if (content != null && content.length() > 0) {
                btnChatMessageSend.setVisibility(View.VISIBLE);
                ivChatExtends.setVisibility(View.GONE);
            }

        } else if (i == R.id.iv_chat_emoticons) {
            if (pannelTheme == EmotionPannelTheme.EMOTION) {
                this.setSystemKeyboard(false);
                if (llEmotionExtends.getVisibility() == GONE) {
                    llEmotionExtends.setVisibility(View.VISIBLE);
                } else {
                    llEmotionExtends.setVisibility(GONE);
                }
                ivChatVoice.setVisibility(View.GONE);
                etChatMessage.setVisibility(View.GONE);
                ivChatExtends.setVisibility(View.GONE);
            } else {
                //Toast.makeText(currContext, "chat emotion list", Toast.LENGTH_SHORT).show();
                this.setSystemKeyboard(false);
                this.setEditTextFocus(true);

                llEmotionExtends.setVisibility(View.VISIBLE);
                ivChatVoice.setVisibility(View.VISIBLE);
                etChatMessage.setVisibility(View.VISIBLE);
                ivChatExtends.setVisibility(View.VISIBLE);
            }
            ivChatKeyboard.setVisibility(View.GONE);
            btnChatVoice.setVisibility(View.GONE);
            btnChatMessageSend.setVisibility(View.GONE);
            gvChatExtends.setVisibility(View.GONE);
            if (emotionEvent != null) {
                emotionEvent.onFunctionPop(getSupportSoftInputHeight());
            }

        } else if (i == R.id.iv_chat_extends) {
            this.setSystemKeyboard(false);
            this.setEditTextFocus(false);

            llEmotionExtends.setVisibility(View.GONE);
            ivChatVoice.setVisibility(View.VISIBLE);
            etChatMessage.setVisibility(View.VISIBLE);
            ivChatKeyboard.setVisibility(View.GONE);
            btnChatVoice.setVisibility(View.GONE);
            ivChatExtends.setVisibility(View.VISIBLE);
            btnChatMessageSend.setVisibility(View.GONE);
            gvChatExtends.setVisibility(View.VISIBLE);
            if (emotionEvent != null) {
                emotionEvent.onFunctionPop(getSupportSoftInputHeight());
            }

        } else if (i == R.id.btn_chat_message_send) {
            btnChatMessageSend.setVisibility(View.GONE);
            ivChatExtends.setVisibility(View.VISIBLE);

            if (emotionEvent != null) {
                if (emotionEvent.onSendMessageClick(etChatMessage.getText() + "")) {
                    etChatMessage.setText("");
                }
            }

        }
    }

    private void setEditTextFocus(boolean isLose) {
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
        gvChatExtends.setVisibility(View.GONE);
        llEmotionExtends.setVisibility(View.GONE);
        setSystemKeyboard(false);
        this.setEditTextFocus(false);
    }

    private void showEmotionFunction() {
        int softInputHeight = getSupportSoftInputHeight();
//        if (softInputHeight == 0) {
//            softInputHeight = getKeyBoardHeight();
//        }
        //setSystemKeyboard(false);
        tvLoading.getLayoutParams().height = softInputHeight;
        tvLoading.setVisibility(View.VISIBLE);
    }

    public void addEmotion(EmotionEditText emotionEditText,String emotionName){
        int start = emotionEditText.getSelectionStart();
        emotionEditText.setText(emotionEditText.getText().insert(start, emotionName));
        CharSequence info = emotionEditText.getText();
        if (info instanceof Spannable) {
            Spannable spanText = (Spannable) info;
            Selection.setSelection(spanText, start + emotionName.length());
        }
    }

    public void deleteEmotion(EmotionEditText emotionEditText){
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

//    //http://blog.csdn.net/feixiangsmile/article/details/76726420?locationNum=10&fps=1
//    private void deleteEmotion() {
//        String content = etChatMessage.getText().toString();
//        if (StringUtils.isBlank(content)) {
//            return;
//        }
//        int start = etChatMessage.getSelectionStart();
//        Log.d("deleteEmotion", start + "-当前光标的位置");
//        String startContent = content.substring(0, start);
//        String endContent = content.substring(start, content.length());
//        String lastContent = content.substring(start - 1, start);
//        int last = startContent.lastIndexOf("[");
//        int lastChar = startContent.substring(0, startContent.length() - 1).lastIndexOf("]");
//
//        if ("]".equals(lastContent) && last > lastChar) {
//            if (last != -1) {
//                etChatMessage.setText(startContent.substring(0, last) + endContent);
//                // 定位光标位置
//                CharSequence info = etChatMessage.getText();
//                if (info instanceof Spannable) {
//                    Spannable spanText = (Spannable) info;
//                    Selection.setSelection(spanText, last);
//                }
//                return;
//            }
//        }
//        etChatMessage.setText(startContent.substring(0, start - 1) + endContent);
//        // 定位光标位置
//        CharSequence info = etChatMessage.getText();
//        if (info instanceof Spannable) {
//            Spannable spanText = (Spannable) info;
//            Selection.setSelection(spanText, start - 1);
//        }
//    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight() {
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
}
