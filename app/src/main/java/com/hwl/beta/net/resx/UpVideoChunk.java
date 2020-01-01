package com.hwl.beta.net.resx;

import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.utils.FileUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class UpVideoChunk {
    private int chunkIndex;
    private int chunkCount;
    private String tempFileUrl;

    private String localPath;
    private UpProcessListener processListener;

    public UpVideoChunk(String localPath) {
        this(localPath, null);
    }

    public UpVideoChunk(String localPath, UpProcessListener processListener) {
        this.localPath = localPath;
        this.processListener = processListener;
        this.initParams();
    }

    private void initParams() {
        chunkIndex = 1;
        chunkCount = 1;
        tempFileUrl = "";
    }

    public Observable<VideoModel> process() {
        File file = new File(this.localPath);
        chunkCount = (int) Math.ceil(file.length() / (float) UploadService.CHUNKED_LENGTH);
        if (chunkCount <= 0) {
            return Observable.just(new VideoModel());
        }
        if (this.processListener != null)
            this.processListener.chunkStart(chunkCount);
        return upload(file);
    }

    private byte[] getUploadBlock(File file) {
        return FileUtils.getBlock((chunkIndex - 1) * UploadService.CHUNKED_LENGTH, file,
                UploadService.CHUNKED_LENGTH);
    }

    private Observable<VideoModel> upload(final File file) {
        final byte[] mBlock = this.getUploadBlock(file);
        return UploadService.upVideo(mBlock, file.getName(), chunkIndex, chunkCount, tempFileUrl)
                .concatMap(new Function<UpResxResponse,
                        ObservableSource<VideoModel>>() {
                    @Override
                    public ObservableSource<VideoModel> apply(UpResxResponse response) throws
                            Exception {
                        if (response != null && response.isSuccess()) {
                            if (chunkIndex >= chunkCount) {//last chunk data
                                initParams();
                                if (processListener != null)
                                    processListener.chunkEnd(chunkCount, chunkIndex, mBlock
                                                    .length, response.getOriginalUrl(), response
                                                    .getPreviewUrl(),
                                            response.getOriginalSize());
                                VideoModel model = new VideoModel();
                                model.isSuccess = true;
                                model.originalUrl = response.getOriginalUrl();
                                model.previewUrl = response.getPreviewUrl();
                                model.originalSize = (int) response.getOriginalSize();
                                return Observable.just(model);
                            } else {
                                tempFileUrl = response.getOriginalUrl();
                                if (processListener != null)
                                    processListener.chunkProcess(chunkCount, chunkIndex, mBlock
                                            .length, response.getPreviewUrl());
                                chunkIndex++;
                                return upload(file);
                            }
                        } else {
                            if (processListener != null)
                                processListener.error(chunkCount, chunkIndex, "Upload video " +
                                        "failure");
                            return Observable.just(new VideoModel());
                        }
                    }
                });
    }

    public class VideoModel {
        public Boolean isSuccess = false;
        public String originalUrl;
        public int originalSize;
        public String previewUrl;
    }

    public interface UpProcessListener {
        void chunkStart(int chunkCount);

        void chunkProcess(int chunkCount, int currentChunkIndex, int chunkBlockSize, String
                currentChunkUrl);

        void chunkEnd(int chunkCount, int currentChunkIndex, int chunkBlockSize, String
                originalUrl, String previewUrl, long originalSize);

        void error(int chunkCount, int currentChunkIndex, String errorMessage);
    }
}