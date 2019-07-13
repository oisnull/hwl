package com.hwl.beta.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hwl.beta.R;

/**
 * Created by Administrator on 2017/12/26.
 */

public class TitleBar extends RelativeLayout {

    final Context currContext;
    ImageView imgLeft, imgRight;
    TextView tvTitle, tvTitleRightInfo;
    RelativeLayout rlTitleLeft;
    LinearLayout llTitleRight;

    public TitleBar(Context context) {
        super(context);
        this.currContext = context;
        init();
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.currContext = context;
        init();
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.currContext = context;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(currContext).inflate(R.layout.widget_title_bar, this, false);

        rlTitleLeft = view.findViewById(R.id.rl_title_left);
        llTitleRight = view.findViewById(R.id.ll_title_right);
        imgLeft = (ImageView) view.findViewById(R.id.img_title_left_back);
        imgRight = (ImageView) view.findViewById(R.id.img_title_right_info);
        tvTitle = (TextView) view.findViewById(R.id.tv_title_content);
        tvTitleRightInfo = (TextView) view.findViewById(R.id.tv_title_right_info);

        this.addView(view);
    }

    public TitleBar setImageLeftResource(int resId, int paddingLeft) {
        imgLeft.setImageResource(resId);
        imgLeft.setPadding(paddingLeft, 0, 0, 0);
        return this;
    }

    public TitleBar setImageLeftResource(int resId) {
        this.setImageLeftResource(resId, 15);
        return this;
    }

    public TitleBar setImageLeftHide() {
        imgLeft.setVisibility(View.GONE);
        return this;
    }

    public TitleBar setTitle(String content) {
        tvTitle.setText(content);
        return this;
    }

    public String getTitle() {
        return tvTitle.getText() + "";
    }

    public TitleBar setTitleRightShow() {
        tvTitleRightInfo.setVisibility(View.VISIBLE);
        return this;
    }

    public TitleBar setTitleRightHide() {
        tvTitleRightInfo.setVisibility(View.GONE);
        return this;
    }

    public TitleBar setTitleRightText(String text) {
        tvTitleRightInfo.setText(text);
        return this;
    }

    public TitleBar setTitleRightBackground(int res) {
        tvTitleRightInfo.setBackgroundResource(res);
        return this;
    }

    public TitleBar setTitleRightClick(OnClickListener click) {
        tvTitleRightInfo.setOnClickListener(click);
        return this;
    }

    public TitleBar setImageLeftClick(OnClickListener click) {
        rlTitleLeft.setOnClickListener(click);
        return this;
    }

//    public ImageView getImageRightView() {
//        return imgRight;
//    }

    public TitleBar setImageRightResource(int resId) {
        imgRight.setImageResource(resId);
        return this;
    }

    public TitleBar setImageRightClick(OnClickListener click) {
        llTitleRight.setOnClickListener(click);
        return this;
    }

    public TitleBar setImageRightHide() {
        imgRight.setVisibility(View.GONE);
        return this;
    }

    public TitleBar setImageRightShow() {
        imgRight.setVisibility(View.VISIBLE);
        return this;
    }
}
