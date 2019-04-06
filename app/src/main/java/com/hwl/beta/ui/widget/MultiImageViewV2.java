package com.hwl.beta.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hwl.beta.R;
import com.hwl.beta.utils.ScreenUtils;

import java.util.List;

public class MultiImageViewV2 extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//
    private int rows;//
    private List<ImageBean> images;
    private int screenWidth;
    private int screenHeight;
    private Context context;
    private IMultiImageListener imageListener;

    public void setImageListener(IMultiImageListener imageListener) {
        this.imageListener = imageListener;
    }

    public MultiImageViewV2(Context context) {
        super(context);
    }

    public MultiImageViewV2(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth = ScreenUtils.getScreenWidth(context) - (120 * 2);
        screenHeight = ScreenUtils.getScreenHeight(context);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView() {

        int count = this.images.size();
        if (count == 1) {
            ImageView iv = generateImageView(0, this.images.get(0), true);
            addView(iv);
        } else {
            for (int i = 0; i < count; i++) {
                ImageView iv = generateImageView(i, this.images.get(i), false);
                addView(iv);
            }
        }



        int childrenCount = images.size();

        int singleWidth = (screenWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;
        if (childrenCount == 1 && showHeight > 0 && showWidth > 0) {
            singleWidth = showWidth;
            singleHeight = showHeight;
        }

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);

        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);
            Glide.with(context).load(images.get(i).path)
                    .placeholder(R.drawable.empty_photo)
                    .error(R.drawable.empty_photo)
                    .into(childrenView);
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;
            childrenView.layout(left, top, right, bottom);
        }
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<ImageBean> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        this.images = lists;
        //初始化布局
        generateChildrenLayout(lists.size());
        layoutChildrenView();
    }

    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private ImageView generateImageView(final int position, final ImageBean image, boolean isCalc) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        if (imageListener != null) {
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageListener.onImageClick(position, image.path);
                }
            });
        }

        if (isCalc && image.width > 0 && image.height > 0) {
            double fixWidth = screenWidth;
            double fixHeight = screenHeight / 3;
            calcSize(fixWidth, fixHeight, image.width, image.height);

            LayoutParams params = new LayoutParams(showWidth, showHeight);
            params.width = showWidth;
            params.height = showHeight;
            iv.setLayoutParams(params);
        }
        return iv;
    }

    private int showWidth;
    private int showHeight;

    private void calcSize(double maxWidth, double maxHeight, int Width, int Height) {
        double w = 0;
        double h = 0;
//        double sw = Width;
//        double sh = Height;
//        double mw = maxWidth;
//        double mh = maxHeight;

        if (Width < maxWidth && Height < maxHeight)//如果maxWidth和maxHeight大于源图像，则缩略图的长和高不变
        {
            w = Width;
            h = Height;
        } else if ((Width / Height) > (maxWidth / maxHeight)) {
            w = maxWidth;
            h = (w * Height) / Width;
        } else {
            h = maxHeight;
            w = (h * Width) / Height;
        }
        showWidth = (int) w;
        showHeight = (int) h;
    }

    public static class ImageBean {
        public ImageBean(int width, int height, String path) {
            this.width = width;
            this.height = height;
            this.path = path;
        }

        public int width;
        public int height;
        public String path;
    }

    public interface IMultiImageListener {
        void onImageClick(int position, String imageUrl);
    }
}
