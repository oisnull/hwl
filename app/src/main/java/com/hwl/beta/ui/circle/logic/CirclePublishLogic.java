package com.hwl.beta.ui.circle.logic;

import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.body.AddCircleInfoResponse;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.ui.circle.standard.CirclePublishStandard;
import com.hwl.beta.ui.imgcompress.CompressChatImage;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CirclePublishLogic implements CirclePublishStandard {

    @Override
    public Observable publishInfo(final String content, List<String> imagePaths) {
        if (StringUtils.isBlank(content) && imagePaths.size() <= 0) {
            return Observable.error(new Throwable("发布内容不能为空"));
        }

        Observable<List<NetImageInfo>> observable = null;
        if (imagePaths.size() > 0) {
            observable = publishImages(imagePaths);
        }

        if (observable == null) {
            return publishContent(content, null);
        }

        return observable.flatMap(new Function<List<NetImageInfo>, ObservableSource<Boolean>>() {
            @Override
            public ObservableSource<Boolean> apply(List<NetImageInfo> images) {
                return publishContent(content, images);
            }
        });
    }

    private Observable<List<NetImageInfo>> publishImages(List<String> imagePaths) {
        return Observable.fromIterable(imagePaths)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, File>() {
                    @Override
                    public File apply(String imagePath) throws Exception {
                        File file = CompressChatImage.chatImage(imagePath);
                        if (file == null) {
                            file = new File(imagePath);
                        }
                        return file;
                    }
                })
                .flatMap(new Function<File, ObservableSource<UpResxResponse>>() {
                    @Override
                    public ObservableSource<UpResxResponse> apply(File file) throws Exception {
                        return UploadService.upImage(file, ResxType.FRIENDCIRCLEPOST);
                    }
                })
                .buffer(imagePaths.size())
                .map(new Function<List<UpResxResponse>, List<NetImageInfo>>() {
                    @Override
                    public List<NetImageInfo> apply(List<UpResxResponse> responses) throws Exception {
                        List<NetImageInfo> imgInfos = new ArrayList<>();
                        for (UpResxResponse res : responses) {
                            if (res.isSuccess()) {
                                NetImageInfo imageInfo = new NetImageInfo();
                                imageInfo.setHeight(res.getHeight());
                                imageInfo.setWidth(res.getWidth());
                                if (StringUtils.isBlank(res.getPreviewUrl())) {
                                    imageInfo.setUrl(res.getOriginalUrl());
                                } else {
                                    imageInfo.setUrl(res.getPreviewUrl());
                                }
                                imgInfos.add(imageInfo);
                            }
                        }
                        return imgInfos;
                    }
                });
    }

    private Observable<Boolean> publishContent(String content, List<NetImageInfo> images) {
        return CircleService.addCircleInfo(content, images)
                .map(new Function<AddCircleInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(AddCircleInfoResponse response) throws Exception {
                        if (response == null || response.getCircleId() <= 0) {
                            throw new Exception("发布失败.");
                        }
                        return true;
                    }
                });
    }
}
