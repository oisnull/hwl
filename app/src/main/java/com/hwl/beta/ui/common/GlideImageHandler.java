//package com.hwl.beta.ui.common;
//
//import android.graphics.Bitmap;
//import android.widget.ImageView;
//
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.BitmapImageViewTarget;
//
///**
// * Created by Administrator on 2018/3/4.
// */
//
//public class GlideImageHandler extends BitmapImageViewTarget {
//    public GlideImageHandler(ImageView view) {
//        super(view);
//    }
//
//    // 长图，宽图比例阈值
//    public static final int RATIO_OF_LARGE = 3;
//    // 长图截取后的高宽比（宽图截取后的宽高比）
//    public static int HW_RATIO = 3;
//    private int imageHeight;
//    private int imageWidth;
//
//    @Override
//    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//        super.onResourceReady(resolveBitmap(resource), glideAnimation);
//    }
//
//    private Bitmap resolveBitmap(Bitmap resource) {
//        int srcWidth = resource.getWidth();
//        int srcHeight = resource.getHeight();
//        imageWidth=srcWidth;
//        imageHeight=srcHeight;
//
//        if (srcWidth > srcHeight) {
//            float srcWHRatio = (float) srcWidth / srcHeight;
//            // 宽图
//            if (srcWHRatio > RATIO_OF_LARGE) {
//                imageWidth=srcHeight * HW_RATIO;
//                imageHeight=srcHeight;
//                return Bitmap.createBitmap(resource, 0, 0, srcHeight * HW_RATIO, srcHeight);
//            }
//        } else {
//            float srcHWRatio = (float) srcHeight / srcWidth;
//            // 长图
//            if (srcHWRatio > RATIO_OF_LARGE) {
//                imageWidth=srcWidth;
//                imageHeight=srcWidth * HW_RATIO;
//                return Bitmap.createBitmap(resource, 0, 0, srcWidth, srcWidth * HW_RATIO);
//            }
//        }
//        if(resouceListener!=null){
//            resouceListener.process(imageWidth,imageHeight);
//        }
//        return resource;
//    }
//
//    public int getHeight(){
//        return imageHeight;
//    }
//
//    public int getWidth(){
//        return imageWidth;
//    }
//
//    IResouceListener resouceListener;
//    public void setResourceListener(IResouceListener resouceListener){
//        this.resouceListener=resouceListener;
//    }
//
//    public interface IResouceListener{
//        void process(int width, int heigth);
//    }
//}
