package com.hwl.beta.net.resx;

public class UpVideoChunk{
    private int chunkIndex;
    private int chunkCount;
    private String tempFileUrl;

    private String localPath;
    private UpProcessListener processLisener;

    public UpVideoChunk(String localPath){
        this(localPath,null);
    }

    public UpVideoChunk(String localPath,UpProcessListener processLisener){
        this.localPath=localPath;
        this.processLisener=processLisener;
        this.initParams();
    }

    private void initParams(){
        chunkIndex = 1;
        chunkCount = 1;
        tempFileUrl = "";
    }

    public Observable<Model> process() {
        File file = new File(this.localPath);
        chunkCount = (int) Math.ceil(file.length() / (float) UploadService.CHUNKED_LENGTH);
        if (chunkCount <= 0) {
            return Observable.just(new Model());
        }
        if(this.processLisener!=null)
            this.processLisener.chunkStart(chunkCount);
        return upload(file);
    }

    private byte[] getUploadBlock(File file){
        return FileUtils.getBlock((chunkIndex - 1) * UploadService.CHUNKED_LENGTH, file, UploadService.CHUNKED_LENGTH);
    }

    private Observable<Model> upload(final File file) {
        byte[] mBlock = this.getUploadBlock(file);
        return UploadService.upVideo(mBlock, file.getName(), chunkIndex, chunkCount, tempFileUrl)
                .map(new Function<ResponseBase<UpResxResponse>, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBase<UpResxResponse> response) throws Exception {
                        if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
                            UpResxResponse res = response.getResponseBody();
                            if (chunkIndex >= chunkCount) {//last chunk data
                                initParams();
                                if(this.processLisener!=null)
                                    this.processLisener.chunkEnd(chunkCount,chunkIndex,mBlock.length,res.getOriginalUrl(),res.getPreviewUrl(),res.getOriginalSize());
                                Model model=new Model();
                                model.isSuccess=true;
                                model.originalUrl=res.getOriginalUrl();
                                model.previewUrl=res.getPreviewUrl();
                                model.originalSize=res.getOriginalSize();
                                return Observable.just(model);
                            } else {
                                tempFileUrl = res.getOriginalUrl();
                                if(this.processLisener!=null)
                                    this.processLisener.chunkProcess(chunkCount,chunkIndex,mBlock.length,res.getPreviewUrl());
                                chunkIndex++;
                                return upload(file);
                            }
                        }else{
                            if(this.processLisener!=null)
                                this.processLisener.error(chunkCount,chunkIndex,"Upload video failure");
                        }
                    }
                });
    }

    public class Model{
        public Boolean isSuccess = false;
        public String originalUrl;
        public long originalSize;
        public String previewUrl;
    }

    public interface UpProcessListener{
        void chunkStart(int chunkCount);
        void chunkProcess(int chunkCount,int currentChunkIndex,int chunkBlockSize,String currentChunkUrl);
        void chunkEnd(int chunkCount,int currentChunkIndex,int chunkBlockSize,String originalUrl,String previewUrl,long originalSize);
        void error(int chunkCount,int currentChunkIndex,String errorMessage);
    }
}