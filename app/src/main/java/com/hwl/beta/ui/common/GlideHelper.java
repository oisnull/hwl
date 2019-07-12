//package com.hwl.beta.ui.common;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.data.DataFetcher;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by Administrator on 2019/07/12.
// */
//public class GlideHelper {
//
//    /**
//     * Fill an ImageView with a picture from the resources using Glide.
//     *
//     * @param context       the Context for where to load
//     * @param imageView     the ImageView to fill with an image
//     * @param resDrawableId the resource drawable id
//     */
//    public static void resDrawableToImageView(Context context, ImageView imageView, int resDrawableId) {
//        if (context == null || imageView == null) {
//            return;
//        }
//        Glide.with(context)
//                .load(resDrawableId)
//                .thumbnail(0.1f)
//                .into(imageView);
//    }
//
//    /**
//     * Fill an ImageView with a picture from an http link using Glide.
//     *
//     * @param context                  the Context for where to load
//     * @param imageView                the ImageView to fill with an image
//     * @param imageUrl                 the image url from which Glide should download and cache the image
//     * @param placeholderDrawableResId the resource id of the image that should be used as a placeholder image
//     */
//    public static void urlToImageView(Context context, ImageView imageView, @NonNull String imageUrl,
//                                      int placeholderDrawableResId) {
//        urlToImageView(context, imageView, imageUrl, placeholderDrawableResId, false);
//    }
//
//    /**
//     * Fill an ImageView with a picture from an Http link using Glide.
//     *
//     * @param context                  the Context for where to load
//     * @param imageView                the ImageView to fill with an image
//     * @param imageUrl                 the image url from which Glide should download and cache the image
//     * @param placeholderDrawableResId the resource id of the image that should be used as a placeholder image
//     * @param useCacheOnly             whether to only use the cache to load the pictures or allow downloading the
//     *                                 picture if the picture is not found in the cache.
//     */
//    public static void urlToImageView(Context context, ImageView imageView, @NonNull String imageUrl,
//                                      int placeholderDrawableResId, boolean useCacheOnly) {
//        if (context == null || imageView == null) {
//            return;
//        }
//        if (useCacheOnly) {
//            Glide.with(context)
//                    .using(cacheOnlyStreamLoader)
//                    .load(imageUrl)
//                    .placeholder(placeholderDrawableResId)
//                    .thumbnail(0.1f)
//                    .into(imageView);
//            return;
//        }
//        Glide.with(context)
//                .load(imageUrl)
//                .placeholder(placeholderDrawableResId)
//                .thumbnail(0.1f)
//                .into(imageView);
//    }
//
//
//    private static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
//        @Override
//        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
//            return new DataFetcher<InputStream>() {
//                @Override
//                public InputStream loadData(Priority priority) throws Exception {
//                    throw new IOException();
//                }
//
//                @Override
//                public void cleanup() {
//
//                }
//
//                @Override
//                public String getId() {
//                    return model;
//                }
//
//                @Override
//                public void cancel() {
//
//                }
//            };
//        }
//    };
//}