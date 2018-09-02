package com.hwl.beta.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/3/30.
 */

public class TipNumberView extends AppCompatTextView {
    private Paint mBgPaint;
    //抗锯齿
    PaintFlagsDrawFilter pfd;

    public TipNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.RED);
        mBgPaint.setAntiAlias(true);
        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    public TipNumberView(Context context) {
        this(context, null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();//获取view的宽度
        int measuredHeight = getMeasuredHeight();//获取view的高度
        //取高度和宽度中的最大值，然后在onDraw();方法中根据较大的那个值作为直径画圆。
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max, max);
    }

    @Override
    public void setBackgroundColor(int color) {
        mBgPaint.setColor(color);
    }
    //设置通知个数显示
    public void setNotifiText(int text) {
        setText(text + "");
    }

    public void setNotifiText(String text) {
        setText(text);
    }

    //draw方法比onDraw方法绘制 的东西更多一些。在这里用draw方法来绘制背景。
    @Override
    public void draw(Canvas canvas) {
        //设置绘图无锯齿
        canvas.setDrawFilter(pfd);
        //下面三个参数：1.参数一是中心点的x轴，参数二是中心点的y轴，参数三是半径，参数四是paint对象；
        //画圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
        super.draw(canvas);
    }
}
