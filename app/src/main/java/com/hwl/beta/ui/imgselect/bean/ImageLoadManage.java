package com.hwl.beta.ui.imgselect.bean;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ImageLoadManage {

    private List<ImageDirBean> dirs;
    private ArrayList<ImageBean> allImages;
    private Activity activity;
    private IImageLoadListener imageLoadListener;

    private String[] mimeTypes = new String[]{"image/png", "image/jpeg", "image/jpg"};
    private String[] IMAGE_COLUMN = {     //查询图片需要的数据列
            MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
            MediaStore.Images.Media.DATA,           //图片的真实路径
            // /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
            MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
            MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
            MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
            MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
            MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608

    public ImageLoadManage(Activity activity, IImageLoadListener imageLoadListener) {
        dirs = new ArrayList<>();
        allImages = new ArrayList<>();   //所有图片的集合,不分文件夹

        this.activity = activity;
        this.imageLoadListener = imageLoadListener;
    }

    public void init() {
        Observable.create(new ObservableOnSubscribe() {
            public void subscribe(ObservableEmitter emitter) throws Exception {
                ContentResolver contentResolver = activity.getContentResolver();
                Cursor data = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_COLUMN, String.format("%s = ? or %s = ? or %s = ?", MediaStore
                                .Images.Media
                                .MIME_TYPE, MediaStore
                                .Images.Media
                                .MIME_TYPE, MediaStore
                                .Images.Media
                                .MIME_TYPE), mimeTypes, IMAGE_COLUMN[6] + " DESC");
                emitter.onNext(data);
            }
        })
                .doOnNext(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor data) throws Exception {
                        if (data != null && data.getCount() > 0) {
                            while (data.moveToNext()) {
                                String imageName = data.getString(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[0]));
                                String imagePath = data.getString(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[1]));
                                File file = new File(imagePath);
                                if (!file.exists() || file.length() <= 0) {
                                    continue;
                                }

                                ImageBean imageItemBean = new ImageBean();
                                imageItemBean.setName(imageName);
                                imageItemBean.setPath(imagePath);
                                imageItemBean.setSize(data.getLong(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[2])));
                                imageItemBean.setWidth(data.getInt(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[3])));
                                imageItemBean.setHeight(data.getInt(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[4])));
                                imageItemBean.setMimeType(data.getString(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[5])));
                                imageItemBean.setAddTime(data.getLong(data.getColumnIndexOrThrow
                                        (IMAGE_COLUMN[6])));
                                allImages.add(imageItemBean);

                                File imageParentFile = file.getParentFile();
                                ImageDirBean dirBean = new ImageDirBean();
                                dirBean.setName(imageParentFile.getName());
                                dirBean.setPath(imageParentFile.getAbsolutePath());

                                if (!dirs.contains(dirBean)) {
                                    ArrayList<ImageBean> images = new ArrayList<>();
                                    images.add(imageItemBean);
                                    dirBean.setFirstImage(imageItemBean);
                                    dirBean.setImages(images);
                                    dirs.add(dirBean);
                                } else {
                                    dirs.get(dirs.indexOf(dirBean)).getImages().add
                                            (imageItemBean);
                                }
                            }

                            //构造所有图片的集合
                            if (allImages.size() > 0) {
                                ImageDirBean allImagesFolder = new ImageDirBean();
                                allImagesFolder.setName("所有图片");
                                allImagesFolder.setPath("/");
                                allImagesFolder.setFirstImage(allImages.get(0));
                                allImagesFolder.setImages(allImages);
                                dirs.add(0, allImagesFolder);
                            }
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (imageLoadListener != null)
                            imageLoadListener.onLoadComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (imageLoadListener != null)
                            imageLoadListener.onLoadComplete();
                    }
                });
    }

    public List<ImageDirBean> getDirs() {
        return dirs;
    }

    public ArrayList<ImageBean> getAllImages() {
        return allImages;
    }

    public interface IImageLoadListener {
        void onLoadComplete();
    }
}
