package com.hwl.beta.emotion;

import android.content.Context;
import android.opengl.Visibility;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwl.beta.emotion.adapter.EmotionPagerAdapter;
import com.hwl.beta.emotion.data.EmotionLocal;
import com.hwl.beta.emotion.interfaces.IEmotionItemListener;
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

/**
 * Created by Administrator on 2018/1/4.
 */

public class EmotionDefaultPanel extends AutoHeightLayout {
    public static final int FUNC_TYPE_EMOTION = -1;

    Context context;
    LayoutInflater inflater;

    ImageView ivDefaultEmotions;
    EmotionEditText etMessage;
    LinearLayout llEmotionPanel;
    EmotionFunctionLayout eflEmotionFunction;

    //emotion function
    EmotionFunctionViewPager efvContainer;
    EmotionIndicatorView eivDotContainer;

    public EmotionDefaultPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflater = LayoutInflater.from(context);

        initEmotionPanel();
    }

    private void initEmotionPanel() {
        View view = inflater.inflate(R.layout.emotion_default_panel, this);
        llEmotionPanel = view.findViewById(R.id.ll_emotion_panel);
        eflEmotionFunction = view.findViewById(R.id.efl_emotion_function);
        ivDefaultEmotions = view.findViewById(R.id.iv_default_emotions);

        ivDefaultEmotions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEmotionView();
            }
        });
        eflEmotionFunction.addOnKeyBoardListener(new EmotionFunctionLayout.OnFuncKeyBoardListener() {
            @Override
            public void OnFuncPop(int height) {

            }

            @Override
            public void OnFuncClose() {
                if (!eflEmotionFunction.isShown())
                    setPanelVisibility(GONE);
            }
        });

        View funcView = inflater.inflate(R.layout.emotion_function_panel, null);
        efvContainer = funcView.findViewById(R.id.efv_container);
        eivDotContainer = funcView.findViewById(R.id.eiv_dot_container);
        EmotionToolBarView etvEmotionBar = funcView.findViewById(R.id.etv_emotion_bar);
        etvEmotionBar.setVisibility(GONE);

        eflEmotionFunction.addFuncView(FUNC_TYPE_EMOTION, funcView);
    }

    public void setPanelVisibility(int visibility) {
        llEmotionPanel.setVisibility(visibility);
    }

    public void setEditText(EmotionEditText et) {
        etMessage = et;
        etMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!etMessage.isFocused()) {
                    etMessage.setFocusable(true);
                    etMessage.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        initEmotionData();
    }

    private void initEmotionData() {
        final EmojiPageContainer defaultEmojiContainer = new EmojiPageContainer.Builder()
                .setDefaultResId(R.drawable.chat_emotion)
                .setAllEmojis(EmotionLocal.defaultEmotions)
                .build();

        final EmotionPagerAdapter emotionPagerAdapter = new EmotionPagerAdapter(context,
                new IEmotionItemListener() {
                    @Override
                    public void onItemClick(EmojiModel emoji) {
                        if (emoji.source == 0) {
                            EmotionUtils.addEmotion(etMessage, emoji.key);
                        }
                    }

                    @Override
                    public void onItemDeleteClick() {
                        EmotionUtils.deleteEmotion(etMessage);
                    }
                });
        emotionPagerAdapter.add(defaultEmojiContainer);

        eivDotContainer.updateCount(defaultEmojiContainer.getPageCount());
        efvContainer.setAdapter(emotionPagerAdapter);
        efvContainer.setCurrentItem(0);
        efvContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currIndex = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                EmojiPageContainer pageContainer = emotionPagerAdapter.getPageContainer(position);
                if (pageContainer.IsShowIndicator())
                    eivDotContainer.setSelectIndicator(position, currIndex);
                currIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void toggleEmotionView() {
        eflEmotionFunction.toggleFuncView(FUNC_TYPE_EMOTION, isSoftKeyboardPop(), etMessage);
    }

    public void reset() {
        EmotionKeyboardUtils.closeSoftKeyboard(this);
        eflEmotionFunction.hideAllFuncView();
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        eflEmotionFunction.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        eflEmotionFunction.setVisibility(true);
        setPanelVisibility(VISIBLE);
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
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && eflEmotionFunction.isShown()) {
            reset();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
