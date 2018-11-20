package com.hwl.beta.ui.chat.imp;

import android.app.Activity;
import android.widget.Toast;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.emotion.EmotionControlPanelV2;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UpVideoChunk;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.GetUserRelationInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.imgcompress.CompressChatImage;
import com.hwl.beta.ui.imgselect.bean.ImageSelectType;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.utils.StorageUtils;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/6.
 */

public class ChatUserEmotionPanelListener implements EmotionControlPanelV2.PanelListener {

    Activity activity;
    Friend user;
//    AudioPlay audioPlay;

    public ChatUserEmotionPanelListener(Activity activity, Friend user) {
        this.activity = activity;
        this.user = user;
    }

    @Override
    public boolean onSendMessageClick(String text) {
        if (StringUtils.isBlank(text)) return false;
        this.sendChatMessage(0, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT, text, null, text
                .length(), 0);
        return true;
    }

    @Override
    public void onSendSoundClick(float seconds, String filePath) {
        //Toast.makeText(activity, filePath + "  " + seconds + " s", Toast.LENGTH_LONG).show();
//        this.sendChatMessage(0, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE, "语音", filePath, 0,
//                (long) seconds);
    }

    @Override
    public boolean onSendExtendsClick() {
        return false;
    }

    @Override
    public void onSelectImageClick() {
        UITransfer.toImageSelectActivity(activity, ImageSelectType.CHAT_PUBLISH, 6, 1);
    }

    @Override
    public void onSelectVideoClick() {
//        UITransfer.toVideoSelectActivity(activity, 3);
    }

    @Override
    public void onSelectFavoriteClick() {

    }

    @Override
    public void onCameraClick() {
        UITransfer.toSystemCamera(activity, 2);
    }

    @Override
    public void onPositionClick() {
        Toast.makeText(activity, "发送位置信息功能稍后开放", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFunctionPop(int popHeigth) {

    }

    public void resendMessage(ChatUserMessage message) {
        if (message == null) return;
        this.sendChatMessage(message.getMsgId(), message.getContentType(), message.getContent(),
                message.getLocalUrl(), message.getContent().length(), message.getPlayTime());
    }

    public void sendChatUserVideoMessage(String localPath) {
        this.sendChatMessage(0, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO, "[视频]", localPath, 0,
                0);
    }

    public void sendChatUserImageMessage() {
        this.sendChatUserImageMessage(StorageUtils.getTempImageFilePath());
    }

    public void sendChatUserImageMessage(String localPath) {
        File file = CompressChatImage.chatImage(activity, localPath);
        if (file == null) {
            file = new File(localPath);
        }
        this.sendChatMessage(0, IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE, "[图片]", file
                .getAbsolutePath
                        (), 0, 0);
    }

    private ChatUserMessage getChatMessage(long messageId, int contentType, String content, String
            localPath, int size, int playTime) {
        ChatUserMessage message = new ChatUserMessage();
        if (messageId > 0) {
            message.setMsgId(messageId);
        }
        message.setFromUserId(UserSP.getUserId());
        message.setFromUserName(UserSP.getUserShowName());
        message.setFromUserHeadImage(UserSP.getUserHeadImage());
        message.setContentType(contentType);
        message.setContent(content);
        message.setLocalUrl(localPath);
//        message.setPreviewUrl(previewUrl);
//        message.setOriginalUrl(originalUrl);
        message.setPlayTime(playTime);
        message.setSize(size);
        message.setSendTime(new Date());
        message.setToUserId(user.getId());
        message.setSendStatus(IMConstant.CHAT_SEND_SENDING);
        return message;
    }

    private ChatRecordMessage getChatRecordMessage(ChatUserMessage userMessage) {
        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_USER);
//      record.setRecordImage(user.getHeadImage());
        record.setFromUserId(UserSP.getUserId());
        record.setFromUserName(UserSP.getUserShowName());
        record.setFromUserHeadImage(UserSP.getUserHeadImage());
        record.setToUserId(user.getId());
        record.setToUserName(user.getName());
        record.setToUserHeadImage(user.getHeadImage());
        record.setTitle(record.getToUserName());
        record.setContentType(userMessage.getContentType());
        record.setContent(StringUtils.cutString(userMessage.getContent(), 25));
        record.setSendTime(userMessage.getSendTime());
        return record;
    }

    private void sendChatMessage(final long messageId, final int contentType, final String
            content, final String localPath, final int size, final int playTime) {
        //1, build chat message model and chat record model and storage to local db
        //2, check user relationship and hint current user
        //3, if exists resx and upload it to server
        //4, send chat message
        //5, get result

        final ChatUserMessage chatUserMessage = getChatMessage(messageId, contentType, content,
                localPath, size, playTime);
        Observable.create(new ObservableOnSubscribe() {
            public void subscribe(ObservableEmitter emitter) throws Exception {
                long msgId = DaoUtils.getChatUserMessageManagerInstance().save(chatUserMessage);
                if (msgId > 0) {
                    chatUserMessage.setMsgId(msgId);
                    ChatRecordMessage recordMessage = DaoUtils
                            .getChatRecordMessageManagerInstance().save(getChatRecordMessage
                                    (chatUserMessage));
                    EventBusUtil.sendChatUserMessageEvent(chatUserMessage);
                    EventBusUtil.sendChatRecordMessageSortEvent(recordMessage);
                    emitter.onNext(chatUserMessage);
                } else {
                    emitter.onComplete();
                }
            }
        })
                .flatMap(new Function<ChatUserMessage, Observable<ChatUserMessage>>() {
                    @Override
                    public Observable<ChatUserMessage> apply(ChatUserMessage userMessage)
                            throws Exception {
                        return validateUser(userMessage);
                    }
                })
                .flatMap(new Function<ChatUserMessage, Observable<ChatUserMessage>>() {
                    @Override
                    public Observable<ChatUserMessage> apply(ChatUserMessage userMessage)
                            throws Exception {
                        switch (userMessage.getContentType()) {
                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
                                return upImage(userMessage);
                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE:
                                return upVoice(userMessage);
                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
                                return upVideo(userMessage);
                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT:
                                return Observable.just(userMessage);
                            default:
                                throw new Exception("Chat message content type error");
                        }
                    }
                })
                .flatMap(new Function<ChatUserMessage, Observable<ChatUserMessage>>() {
                    @Override
                    public Observable<ChatUserMessage> apply(ChatUserMessage userMessage)
                            throws Exception {
                        return sendChatUserMessage(userMessage);
                    }
                })
                .subscribeOn(Schedulers.io())
                // .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChatUserMessage>() {
                    @Override
                    public void accept(ChatUserMessage userMessage) {
                        userMessage.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
                        DaoUtils.getChatUserMessageManagerInstance().save(userMessage);
                        EventBusUtil.sendChatUserMessageEvent(userMessage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        chatUserMessage.setSendStatus(IMConstant.CHAT_SEND_FAILD);
                        DaoUtils.getChatUserMessageManagerInstance().save(chatUserMessage);
                        EventBusUtil.sendChatUserMessageEvent(chatUserMessage);
                    }
                });
    }

    private Observable<ChatUserMessage> validateUser(final ChatUserMessage message) {
        return UserService.getUserRelationInfo(message
                .getToUserId())
                .map(new Function<ResponseBase<GetUserRelationInfoResponse>, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(ResponseBase<GetUserRelationInfoResponse>
                                                         response) throws Exception {
                        if (response != null && response.getResponseBody() != null) {
                            GetUserRelationInfoResponse res = response.getResponseBody();
                            if (res.getIsInBlackList()) {
                                throw new Exception("User is in black list");
                            }
                            return message;
                        } else {
                            throw new Exception("Validate chat user failure");
                        }
                    }
                });
//        observable.subscribe();
//        return Observable.just(message);
    }

    private Observable<ChatUserMessage> upImage(final ChatUserMessage message) {
        return UploadService.upImage(new File(message.getLocalUrl()), ResxType.CHATIMAGE)
                .map(new Function<ResponseBase<UpResxResponse>, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(ResponseBase<UpResxResponse> response) throws
                            Exception {
                        if (response != null && response.getResponseBody() != null && response
                                .getResponseBody().isSuccess()) {
                            UpResxResponse res = response.getResponseBody();
                            message.setOriginalUrl(res.getOriginalUrl());
                            message.setPreviewUrl(StringUtils.isBlank(res.getPreviewUrl()) ? res
                                    .getOriginalUrl() : res.getPreviewUrl());
                            message.setImageWidth(res.getWidth());
                            message.setImageHeight(res.getHeight());
                            message.setSize((int) res.getOriginalSize());
                            return message;
                        } else {
                            throw new Exception("Chat image resx upload failure");
                        }
                    }
                });
    }

    private Observable<ChatUserMessage> upVoice(final ChatUserMessage message) {
        return UploadService.upVoice(new File(message.getLocalUrl()))
                .map(new Function<ResponseBase<UpResxResponse>, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(ResponseBase<UpResxResponse> response) throws
                            Exception {
                        if (response != null && response.getResponseBody() != null && response
                                .getResponseBody().isSuccess()) {
                            UpResxResponse res = response.getResponseBody();
                            message.setOriginalUrl(StringUtils.isBlank(res.getPreviewUrl()) ? res
                                    .getOriginalUrl() : res.getPreviewUrl());
                            message.setSize((int) res.getOriginalSize());
                            return message;
                        } else {
                            throw new Exception("Chat voice resx upload failure");
                        }
                    }
                });
    }

    private Observable<ChatUserMessage> upVideo(final ChatUserMessage message) {
        return new UpVideoChunk(message.getLocalUrl())
                .process()
                .map(new Function<UpVideoChunk.VideoModel, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(UpVideoChunk.VideoModel model) throws Exception {
                        if (model.isSuccess) {
                            message.setPreviewUrl(model.previewUrl);
                            message.setOriginalUrl(model.originalUrl);
                            message.setSize(model.originalSize);
                            return message;
                        } else {
                            throw new Exception("Chat video resx upload failure");
                        }
                    }
                });
    }

    private Observable<ChatUserMessage> sendChatUserMessage(final ChatUserMessage userMessage) {
        return Observable.create(new ObservableOnSubscribe() {
            public void subscribe(final ObservableEmitter emitter) throws Exception {

                IMDefaultSendOperateListener sendOperateListener = new
                        IMDefaultSendOperateListener("ChatUser") {
                            @Override
                            public void success1() {
                                emitter.onNext(userMessage);
                            }

                            @Override
                            public void failed1() {
                                emitter.onError(new Exception("Send chat user message failure"));
                            }
                        };

                switch (userMessage.getContentType()) {
                    case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
                        IMClientEntry.sendChatUserImageMessage(userMessage.getToUserId(),
                                userMessage
                                        .getPreviewUrl(), userMessage.getImageWidth(), userMessage
                                        .getImageHeight
                                                (), userMessage.getSize(), sendOperateListener);
                        break;
                    case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE:
                        IMClientEntry.sendChatUserVoiceMessage(userMessage.getToUserId(),
                                userMessage
                                        .getPreviewUrl(), userMessage.getSize(), userMessage
                                        .getPlayTime(),
                                sendOperateListener);
                        break;
                    case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
                        IMClientEntry.sendChatUserVideoMessage(userMessage.getToUserId(),
                                userMessage
                                        .getPreviewUrl(), userMessage.getImageWidth(), userMessage
                                        .getImageHeight
                                                (), userMessage.getSize(), userMessage
                                        .getPlayTime(),
                                sendOperateListener);
                        break;
                    case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT:
                        IMClientEntry.sendChatUserTextMessage(userMessage.getToUserId(), userMessage
                                .getContent(), true, sendOperateListener);
                        break;
                }

            }
        });
    }

//    private void sendChatMessage(final long messageId, int contentType, String content, String
//            localPath, long size, long playTime) {
//        final ChatUserMessage message = new ChatUserMessage();
//        if (messageId > 0) {
//            message.setMsgId(messageId);
//        }
//        message.setFromUserId(UserSP.getUserId());
//        message.setFromUserName(UserSP.getUserShowName());
//        message.setFromUserHeadImage(UserSP.getUserHeadImage());
//        message.setContentType(contentType);
//        message.setContent(content);
//        message.setLocalUrl(localPath);
////        message.setPreviewUrl(previewUrl);
////        message.setOriginalUrl(originalUrl);
//        message.setPlayTime(playTime);
//        message.setSize(size);
//        message.setSendTime(new Date());
//        message.setToUserId(user.getId());
//        message.setSendStatus(IMConstant.CHAT_SEND_SENDING);
//
//        Observable.just(message)
//                .subscribeOn(Schedulers.io())
//                .filter(new Predicate<ChatUserMessage>() {
//                    @Override
//                    public boolean test(ChatUserMessage userMessage) throws Exception {
//                        long msgId = DaoUtils.getChatUserMessageManagerInstance().save
// (userMessage);
//                        if (msgId <= 0) {
//                            return false;
//                        }
//                        userMessage.setMsgId(msgId);
//                        EventBus.getDefault().post(userMessage);
//                        return true;
//                    }
//                })
////                .delay(2, TimeUnit.SECONDS)
//                .map(new Function<ChatUserMessage, ChatRecordMessage>() {
//                    @Override
//                    public ChatRecordMessage apply(ChatUserMessage userMessage) throws Exception {
//                        //Log.d("ChatUserRecord", user.getHeadImage());
//                        ChatRecordMessage record = new ChatRecordMessage();
//                        record.setRecordType(IMConstant.CHAT_RECORD_TYPE_USER);
////                        record.setRecordImage(user.getHeadImage());
//                        record.setFromUserId(userMessage.getFromUserId());
//                        record.setFromUserName(userMessage.getFromUserName());
//                        record.setFromUserHeadImage(userMessage.getFromUserHeadImage());
//                        record.setToUserId(user.getId());
//                        record.setToUserName(DaoUtils.getFriendManagerInstance().getFriendName
//                                (user));
//                        record.setToUserHeadImage(user.getHeadImage());
//                        record.setTitle(record.getToUserName());
//                        record.setContentType(userMessage.getContentType());
//                        record.setContent(StringUtils.cutString(message.getContent(), 25));
//                        record.setSendTime(userMessage.getSendTime());
//                        record = DaoUtils.getChatRecordMessageManagerInstance().save(record);
//                        EventBus.getDefault().post(record);
//                        return record;
//                    }
//                })
//                .flatMap(new Function<ChatRecordMessage, ObservableSource<Boolean>>() {
//                    @Override
//                    public ObservableSource<Boolean> apply(ChatRecordMessage recordMessage)
//                            throws Exception {
//                        switch (recordMessage.getContentType()) {
//                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE:
//                                return upAndSendImage(message);
//                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE:
//                                return upAndSendAudio(message);
//                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO:
//                                return upAndSendVideo(message);
//                            case IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT:
//                                return null;//ChatMessageSend.sendChatUserMessage(message
//                            // .getMsgId(), recordMessage.getToUserId(), recordMessage
//                            // .getContentType(), recordMessage.getContent());
//                        }
//                        return Observable.just(false);
//                    }
//                })
//                .doOnNext(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean succ) throws Exception {
//                        if (succ) {
//                            message.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
//                        } else {
//                            message.setSendStatus(IMConstant.CHAT_SEND_FAILD);
//                        }
//                        DaoUtils.getChatUserMessageManagerInstance().save(message);
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
//                        message.setSendStatus(IMConstant.CHAT_SEND_FAILD);
//                        DaoUtils.getChatUserMessageManagerInstance().save(message);
//                        EventBus.getDefault().post(message);
//                    }
//                });
//    }

//    private int chunkIndex = 1;
//    private int chunkCount = 1;
//    private String tempFileUrl = "";
//
//    private Observable<Boolean> upAndSendVideo(final ChatUserMessage message) {
//        File file = new File(message.getLocalUrl());
//        chunkCount = (int) Math.ceil(file.length() / (float) UploadService.CHUNKED_LENGTH);
//        if (chunkCount <= 0) {
//            return Observable.just(false);
//        }
//        return upVideoProcess(file, message);
//    }
//
//    private Observable<Boolean> upVideoProcess(final File file, final ChatUserMessage message) {
//        byte[] mBlock = FileUtils.getBlock((chunkIndex - 1) * UploadService.CHUNKED_LENGTH, file,
//                UploadService.CHUNKED_LENGTH);
//        return UploadService.upVideo(mBlock, file.getName(), chunkIndex, chunkCount, tempFileUrl)
//                .concatMap(new Function<ResponseBase<UpResxResponse>, ObservableSource<? extends
//                        Boolean>>() {
//                    @Override
//                    public ObservableSource<? extends Boolean> apply(ResponseBase<UpResxResponse>
//                                                                             response) throws
//                            Exception {
//                        if (response != null && response.getResponseBody() != null && response
//                                .getResponseBody().isSuccess()) {
//                            UpResxResponse res = response.getResponseBody();
//                            if (chunkIndex >= chunkCount) {
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
//    private Observable<Boolean> upAndSendAudio(final ChatUserMessage message) {
//        return UploadService.upVoice(new File(message.getLocalUrl()))
//                .map(new Function<ResponseBase<UpResxResponse>, Boolean>() {
//                    @Override
//                    public Boolean apply(ResponseBase<UpResxResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null && response
//                                .getResponseBody().isSuccess()) {
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
//                });
//    }
//
//    private Observable<Boolean> upAndSendImage(final ChatUserMessage message) {
//        return UploadService.upImage(new File(message.getLocalUrl()), ResxType.CHATIMAGE)
//                .map(new Function<ResponseBase<UpResxResponse>, Boolean>() {
//                    @Override
//                    public Boolean apply(ResponseBase<UpResxResponse> response) throws Exception {
//                        if (response != null && response.getResponseBody() != null && response
//                                .getResponseBody().isSuccess()) {
//                            UpResxResponse res = response.getResponseBody();
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
//                });
//    }
//
//    public void playAudio(final ImageView iv, final ChatUserMessage message) {
//        if (message == null) return;
//        if (audioPlay != null && audioPlay.getStatus() == AudioPlay.PLAY_PROCESSING) {
//            audioPlay.stop();
//            return;
//        }
//
//        final String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message
//                .getPreviewUrl(), message.getOriginalUrl());
//        if (StringUtils.isBlank(showUrl)) {
//            Toast.makeText(activity, "语音不存在", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        final int playDirection = (message.getFromUserId() == UserSP.getUserId()) ? AudioPlay
//                .PLAY_RIGHT : AudioPlay.PLAY_LEFT;
//        if (showUrl.startsWith("http")) {
//            final String localFilePath = AudioRecorderButton.getAudioStoreDir() + showUrl
//                    .substring(showUrl.lastIndexOf("/") + 1, showUrl.length());
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
//                                if (DaoUtils.getChatUserMessageManagerInstance().updateLocalPath
//                                        (message.getMsgId(), localFilePath)) {
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
}
