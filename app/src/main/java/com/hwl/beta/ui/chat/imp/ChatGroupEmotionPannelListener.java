//package com.hwl.beta.ui.chat.imp;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.hwl.beta.db.DaoUtils;
//import com.hwl.beta.db.entity.ChatGroupMessage;
//import com.hwl.beta.db.entity.ChatRecordMessage;
//import com.hwl.beta.db.entity.GroupInfo;
//import com.hwl.beta.emotion.IEmotionPanelListener;
//import com.hwl.beta.emotion.audio.AudioPlay;
//import com.hwl.beta.emotion.audio.AudioRecorderButton;
//import com.hwl.beta.mq.MQConstant;
//import com.hwl.beta.mq.send.ChatMessageSend;
//import com.hwl.beta.net.ResponseBase;
//import com.hwl.beta.net.resx.DownloadService;
//import com.hwl.beta.net.resx.ResxType;
//import com.hwl.beta.net.resx.UploadService;
//import com.hwl.beta.net.resx.body.UpResxResponse;
//import com.hwl.beta.sp.UserSP;
//import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
//import com.hwl.beta.ui.common.UITransfer;
//import com.hwl.beta.ui.common.rxext.MQDefaultObserver;
//import com.hwl.beta.ui.imgcompress.CompressChatImage;
//import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
//import com.hwl.beta.utils.FileUtils;
//import com.hwl.beta.utils.StorageUtils;
//import com.hwl.beta.utils.StringUtils;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.File;
//import java.util.Date;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableSource;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.functions.Consumer;
//import io.reactivex.functions.Function;
//import io.reactivex.functions.Predicate;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.ResponseBody;
//
///**
// * Created by Administrator on 2018/4/6.
// */
//
//public class ChatGroupEmotionPannelListener implements IEmotionPanelListener {
//
//    Activity activity;
//    GroupInfo groupInfo;
////    File cameraTempFile;
//    AudioPlay audioPlay;
//
//    public ChatGroupEmotionPannelListener(Activity activity, GroupInfo groupInfo) {
//        this.activity = activity;
//        this.groupInfo = groupInfo;
//    }
//
//    public void setGroupInfo(GroupInfo groupInfo) {
//        this.groupInfo = groupInfo;
//    }
//
//    @Override
//    public boolean onSendMessageClick(String text) {
//        if (StringUtils.isBlank(text)) return false;
//        this.sendChatMessage(0, MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD, text, null, text.length(), 0);
//        return true;
//    }
//
//    @Override
//    public void onSendSoundClick(float seconds, String filePath) {
//        //Toast.makeText(activity, filePath + "  " + seconds + " s", Toast.LENGTH_LONG).show();
//        this.sendChatMessage(0, MQConstant.CHAT_MESSAGE_CONTENT_TYPE_SOUND, "语音", filePath, 0, (long) seconds);
//    }
//
//    @Override
//    public boolean onSendExtendsClick() {
//        return false;
//    }
//
//    @Override
//    public void onSelectImageClick() {
//        UITransfer.toImageSelectActivity(activity, ImageSelectType.CHAT_PUBLISH, 6, 1);
//    }
//
//    @Override
//    public void onSelectVideoClick() {
//        UITransfer.toVideoSelectActivity(activity, 3);
//    }
//
//    @Override
//    public void onCameraClick() {
////        cameraTempFile = new File(ActivityImageSelect.getTempFileName());
////        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraTempFile));
////        activity.startActivityForResult(cameraIntent, 2);
//        UITransfer.toSystemCamera(activity, 2);
//    }
//
//    @Override
//    public void onPositionClick() {
//        Toast.makeText(activity, "发送位置信息功能稍后开放", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onFunctionPop(int popHeigth) {
//
//    }
//
//    public void resendMessage(ChatGroupMessage message) {
//        if (message == null) return;
//        this.sendChatMessage(message.getMsgId(), message.getContentType(), message.getContent(), message.getLocalUrl(), message.getContent().length(), message.getPlayTime());
//    }
//
//    public void sendChatGroupVideoMessage(String localPath) {
//        this.sendChatMessage(0, MQConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO, "视频", localPath, 0, 0);
//    }
//
//    public void sendChatGroupImageMessage() {
//        sendChatGroupImageMessage(StorageUtils.getTempImageFilePath());
////        if (cameraTempFile != null && cameraTempFile.exists()) {
////            sendChatGroupImageMessage(cameraTempFile.getPath());
////        }
////        cameraTempFile = null;
//    }
//
//    public void sendChatGroupImageMessage(String localPath) {
//        File file = CompressChatImage.chatImage(activity, localPath);
//        if (file == null) {//说明压缩失败
//            file = new File(localPath);//直接发原图
//        }
//        sendChatMessage(0, MQConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE, "图片", file.getAbsolutePath(), 0, 0);
//    }
//
//    private boolean checkGroupDismiss() {
//        if (groupInfo == null || groupInfo.getIsDismiss()) {
//            new AlertDialog.Builder(activity)
//                    .setMessage("已经被解散群组不能发送消息!")
//                    .setPositiveButton("返回", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .show();
//            return true;
//        }
//        return false;
//    }
//
//    public void sendChatMessage(long messageId, int contentType, String content, String localPath, long size, long playTime) {
//        if (checkGroupDismiss()) return;
//
//        final ChatGroupMessage message = new ChatGroupMessage();
//        if (messageId > 0) {
//            message.setMsgId(messageId);
//        }
//        message.setGroupGuid(groupInfo.getGroupGuid());
//        message.setGroupName(groupInfo.getGroupName());
//        message.setGroupImage("");
//        message.setFromUserId(UserSP.getUserId());
//        message.setFromUserName((StringUtils.isBlank(groupInfo.getMyUserName()) ? UserSP.getUserShowName() : groupInfo.getMyUserName()));
//        message.setFromUserHeadImage(UserSP.getUserHeadImage());
//        message.setContentType(contentType);
//        message.setContent(content);
//        message.setLocalUrl(localPath);
////        message.setPreviewUrl(previewUrl);
////        message.setOriginalUrl(originalUrl);
//        message.setPlayTime(playTime);
//        message.setSize(size);
//        message.setSendTime(new Date());
//        message.setSendStatus(MQConstant.CHAT_SEND_SENDING);
//
//        Observable.just(message)
//                .subscribeOn(Schedulers.io())
//                .filter(new Predicate<ChatGroupMessage>() {
//                    @Override
//                    public boolean test(ChatGroupMessage groupMessage) throws Exception {
//                        long msgId = DaoUtils.getChatGroupMessageManagerInstance().save(groupMessage);
//                        if (msgId <= 0) {
//                            return false;
//                        }
//                        groupMessage.setMsgId(msgId);
//                        EventBus.getDefault().post(groupMessage);
//                        return true;
//                    }
//                })
////                .delay(2, TimeUnit.SECONDS)
//                .map(new Function<ChatGroupMessage, ChatRecordMessage>() {
//                    @Override
//                    public ChatRecordMessage apply(ChatGroupMessage groupMessage) throws Exception {
//                        ChatRecordMessage record = new ChatRecordMessage();
//                        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
//                        record.setGruopGuid(message.getGroupGuid());
//                        record.setGroupName(message.getGroupName());
//                        record.setRecordImage(message.getGroupImage());
//                        record.setFromUserId(message.getFromUserId());
//                        record.setFromUserName(message.getFromUserName());
//                        record.setFromUserHeadImage(message.getFromUserHeadImage());
//                        record.setTitle(message.getGroupName());
//                        record.setContentType(message.getContentType());
//                        record.setContent(message.getFromUserName() + " : " + StringUtils.cutString(message.getContent(), 25));
//                        record.setSendTime(message.getSendTime());
//                        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);
//                        EventBus.getDefault().post(record);
//                        return record;
//                    }
//                })
//                .flatMap(new Function<ChatRecordMessage, ObservableSource<Boolean>>() {
//                    @Override
//                    public ObservableSource<Boolean> apply(ChatRecordMessage recordMessage) throws Exception {
//                        switch (recordMessage.getContentType()) {
//                            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
//                                return upAndSendImage(message);
//                            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_SOUND:
//                                return upAndSendAudio(message);
//                            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
//                                return upAndSendVideo(message);
//                            case MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD:
//                                return ChatMessageSend.sendChatGroupMessage(message.getGroupGuid(), message.getGroupName(), message.getContentType(), message.getContent());
//                        }
//                        return Observable.just(false);
//                    }
//                })
//                .doOnNext(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean succ) throws Exception {
//                        if (succ) {
//                            message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
//                        } else {
//                            message.setSendStatus(MQConstant.CHAT_SEND_FAILD);
//                        }
//                        DaoUtils.getChatGroupMessageManagerInstance().save(message);
//                        EventBus.getDefault().post(message);
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MQDefaultObserver(false) {
//                    @Override
//                    protected void onSuccess() {
//                    }
//
//                    @Override
//                    protected void onError(String resultMessage) {
//                        super.onError(resultMessage);
//                        message.setSendStatus(MQConstant.CHAT_SEND_FAILD);
//                        DaoUtils.getChatGroupMessageManagerInstance().save(message);
//                        EventBus.getDefault().post(message);
//                    }
//                });
//    }
//
//    private int chunkIndex = 1;
//    private int chunkCount = 1;
//    private String tempFileUrl = "";
//
//    private Observable<Boolean> upAndSendVideo(final ChatGroupMessage message) {
//        File file = new File(message.getLocalUrl());
//        chunkCount = (int) Math.ceil(file.length() / (float) UploadService.CHUNKED_LENGTH);
//        if (chunkCount <= 0) {
//            return Observable.just(false);
//        }
//        return upVideoProcess(file, message);
//    }
//
//    private Observable<Boolean> upVideoProcess(final File file, final ChatGroupMessage message) {
//        byte[] mBlock = FileUtils.getBlock((chunkIndex - 1) * UploadService.CHUNKED_LENGTH, file, UploadService.CHUNKED_LENGTH);
//        return UploadService.upVideo(mBlock, file.getName(), chunkIndex, chunkCount, tempFileUrl)
//                .concatMap(new Function<ResponseBase<UpResxResponse>, ObservableSource<? extends Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(ResponseBase<UpResxResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
//                            UpResxResponse res = response.getResponseBody();
//                            if (chunkIndex >= chunkCount) {
//                                //这里表示的是发送的是最后一块数据
//                                chunkIndex = 1;
//                                chunkCount = 1;
//                                tempFileUrl = "";
//                                message.setPreviewUrl(res.getPreviewUrl());
//                                message.setOriginalUrl(res.getOriginalUrl());
//                                message.setSize(res.getOriginalSize());
//                                return Observable.just(true);
//                            } else {
//                                chunkIndex++;
//                                tempFileUrl = res.getOriginalUrl();
//                                return upVideoProcess(file, message);
//                            }
//                        } else {
//                            return Observable.just(false);
//                        }
//                    }
//                });
//    }
//
//    private Observable<Boolean> upAndSendAudio(final ChatGroupMessage message) {
//        return UploadService.upVoice(new File(message.getLocalUrl()))
//                .map(new Function<ResponseBase<UpResxResponse>, Boolean>() {
//                    @Override
//                    public Boolean apply(ResponseBase<UpResxResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
//                            UpResxResponse res = response.getResponseBody();
//                            if (StringUtils.isBlank(res.getPreviewUrl())) {
//                                message.setPreviewUrl(res.getOriginalUrl());
//                            } else {
//                                message.setPreviewUrl(res.getPreviewUrl());
//                            }
//                            message.setSize(res.getOriginalSize());
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                })
//                .concatMap(new Function<Boolean, ObservableSource<? extends Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(Boolean succ) throws Exception {
//                        if (succ) {
//                            return ChatMessageSend.sendChatGroupMessage(
//                                    message.getGroupGuid(),
//                                    message.getGroupName(),
//                                    message.getContentType(),
//                                    message.getContent(),
//                                    message.getPreviewUrl(),
//                                    message.getOriginalUrl(),
//                                    message.getImageWidth(),
//                                    message.getImageHeight(),
//                                    message.getSize(),
//                                    message.getPlayTime());
//                        }
//                        return Observable.just(false);
//                    }
//                });
//    }
//
//    private Observable<Boolean> upAndSendImage(final ChatGroupMessage message) {
//        return UploadService.upImage(new File(message.getLocalUrl()), ResxType.CHATIMAGE)
//                .map(new Function<ResponseBase<UpResxResponse>, Boolean>() {
//                    @Override
//                    public Boolean apply(ResponseBase<UpResxResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null && response.getResponseBody().isSuccess()) {
//                            UpResxResponse res = response.getResponseBody();
//                            //上传成功,更新发送状态,并发送消息
//                            if (StringUtils.isBlank(res.getPreviewUrl())) {
//                                message.setPreviewUrl(res.getOriginalUrl());
//                            } else {
//                                message.setPreviewUrl(res.getPreviewUrl());
//                            }
//                            message.setOriginalUrl(res.getOriginalUrl());
//                            message.setImageWidth(res.getWidth());
//                            message.setImageHeight(res.getHeight());
//                            message.setSize(res.getOriginalSize());
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    }
//                })
//                .concatMap(new Function<Boolean, ObservableSource<? extends Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(Boolean succ) throws Exception {
//                        if (succ) {
//                            return ChatMessageSend.sendChatGroupMessage(
//                                    message.getGroupGuid(),
//                                    message.getGroupName(),
//                                    message.getContentType(),
//                                    message.getContent(),
//                                    message.getPreviewUrl(),
//                                    message.getOriginalUrl(),
//                                    message.getImageWidth(),
//                                    message.getImageHeight(),
//                                    message.getSize(),
//                                    message.getPlayTime());
//                        }
//                        return Observable.just(false);
//                    }
//                });
//    }
//
//    public void playAudio(final ImageView iv, final ChatGroupMessage message) {
//        if (message == null) return;
//        if (audioPlay != null && audioPlay.getStatus() == AudioPlay.PLAY_PROCESSING) {
//            audioPlay.stop();
//            return;
//        }
//
//        final String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message.getPreviewUrl(), message.getOriginalUrl());
//        if (StringUtils.isBlank(showUrl)) {
//            Toast.makeText(activity, "语音不存在", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final int playDirection = (message.getFromUserId() == UserSP.getUserId()) ? AudioPlay.PLAY_RIGHT : AudioPlay.PLAY_LEFT;
//        if (showUrl.startsWith("http")) {
//            final String localFilePath = AudioRecorderButton.getAudioStoreDir() + showUrl.substring(showUrl.lastIndexOf("/") + 1, showUrl.length());
//            //如果是远程地址就下载文件
//            DownloadService.downloadFile(showUrl)
//                    .map(new Function<ResponseBody, Boolean>() {
//                        @Override
//                        public Boolean apply(ResponseBody responseBody) throws Exception {
//                            if (responseBody == null || responseBody.contentLength() <= 0)
//                                return false;
//                            return FileUtils.writeFile(localFilePath, responseBody.byteStream());
//                        }
//                    })
//                    .filter(new Predicate<Boolean>() {
//                        @Override
//                        public boolean test(Boolean succ) throws Exception {
//                            if (succ) {
//                                if (DaoUtils.getChatGroupMessageManagerInstance().updateLocalPath(message.getMsgId(), localFilePath)) {
//                                    message.setLocalUrl(localFilePath);
//                                    EventBus.getDefault().post(message);
//                                    return true;
//                                }
//                            }
//                            return false;
//                        }
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Boolean>() {
//                        @Override
//                        public void accept(Boolean succ) throws Exception {
//                            audioPlay = new AudioPlay(iv, playDirection);
//                            audioPlay.start(showUrl, message.getPlayTime());
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            Toast.makeText(activity, "语音下载失败", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            if (!FileUtils.isExists(showUrl)) {
//                Toast.makeText(activity, "语音播放失败", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            audioPlay = new AudioPlay(iv, playDirection);
//            audioPlay.start(showUrl, message.getPlayTime());
//        }
//    }
//}
