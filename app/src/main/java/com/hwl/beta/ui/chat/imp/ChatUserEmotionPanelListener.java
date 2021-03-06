package com.hwl.beta.ui.chat.imp;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.Toast;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.emotion.audio.AudioPlay;
import com.hwl.beta.emotion.audio.AudioRecorderButton;
import com.hwl.beta.net.resx.DownloadService;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.UpVideoChunk;
import com.hwl.beta.net.resx.UploadService;
import com.hwl.beta.net.resx.body.UpResxResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.GetUserRelationInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;
import com.hwl.beta.ui.common.exception.CustomException;
import com.hwl.beta.ui.common.exception.ExceptionCode;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.imgcompress.CompressChatImage;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMConstant;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.utils.FileUtils;
import com.hwl.beta.utils.StorageUtils;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/4/6.
 */

public class ChatUserEmotionPanelListener extends ChatEmotionPanelListener {

    Friend user;
    AudioPlay audioPlay;

    public ChatUserEmotionPanelListener(Activity activity, Friend user) {
        super(activity);
        this.user = user;
    }

    @Override
    public boolean onSendTextClick(String text) {
        if (StringUtils.isBlank(text)) return false;
        this.sendChatMessage(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_TEXT, text, null, text
                .length(), 0);
        return true;
    }

    @Override
    public void onSendSoundClick(float seconds, String filePath) {
        this.sendChatMessage(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VOICE, "[语音]", filePath, 0,
                (int) seconds);
    }

    public void resendMessage(ChatUserMessage message) {
        if (message == null) return;
        message.setSendStatus(IMConstant.CHAT_SEND_SENDING);
        this.sendChatMessage(message);
    }

    public void sendChatUserVideoMessage(String localPath) {
        this.sendChatMessage(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_VIDEO, "[视频]", localPath, 0,
                0);
    }

    public void sendChatUserImageMessage() {
        this.sendChatUserImageMessage(StorageUtils.getTempImageFilePath());
    }

    public void sendChatUserImageMessage(String localPath) {
        File file = CompressChatImage.chatImage(context, localPath);
        if (file == null) {
            file = new File(localPath);
        }
        this.sendChatMessage(IMConstant.CHAT_MESSAGE_CONTENT_TYPE_IMAGE, "[图片]", file
                .getAbsolutePath
                        (), 0, 0);
    }

    private ChatUserMessage getChatMessage(int contentType, String content, String localPath, int
            size, int playTime) {
        ChatUserMessage message = new ChatUserMessage();
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

    private void sendChatMessage(final int contentType, final String content, final String
            localPath, final int size, final int playTime) {
        this.sendChatMessage(getChatMessage(contentType, content, localPath, size, playTime));
    }

    private void sendChatMessage(final ChatUserMessage chatUserMessage) {
        //1, build chat message model and chat record model and storage to local db
        //2, check user relationship and hint current user
        //3, if exists resx and upload it to server
        //4, send chat message
        //5, get result

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
                        //Image: localUrl,originalUrl,previewUrl
                        //Voice: localUrl,originalUrl
                        //Video: localUrl,originalUrl,previewUrl
                        //Text:  None
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
                                throw new CustomException(ExceptionCode
                                        .ChatMessageContentTypeError);
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
                        userMessage.setStatusDesc("");
                        userMessage.setSendStatus(IMConstant.CHAT_SEND_SUCCESS);
                        DaoUtils.getChatUserMessageManagerInstance().save(userMessage);
                        EventBusUtil.sendChatUserMessageEvent(userMessage);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        if (throwable instanceof CustomException) {
                            chatUserMessage.setStatusDesc(throwable.getMessage());
                        }
                        chatUserMessage.setSendStatus(IMConstant.CHAT_SEND_FAILD);
                        DaoUtils.getChatUserMessageManagerInstance().save(chatUserMessage);
                        EventBusUtil.sendChatUserMessageEvent(chatUserMessage);
                    }
                });
    }

    private Observable<ChatUserMessage> validateUser(final ChatUserMessage message) {
        return UserService.getUserRelationInfo(message.getToUserId())
                .map(new Function<GetUserRelationInfoResponse, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(GetUserRelationInfoResponse res) throws Exception {
                        if (res.getIsInBlackList()) {
                            throw new CustomException(ExceptionCode.ChatCodeUserBlack);
                        }
                        return message;
                    }
                });
    }

    private Observable<ChatUserMessage> upImage(final ChatUserMessage message) {
        if (!StringUtils.isEmpty(message.getOriginalUrl())) {
            return Observable.just(message);
        }

        return UploadService.upImage(new File(message.getLocalUrl()), ResxType.CHATIMAGE)
                .map(new Function<UpResxResponse, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(UpResxResponse res) throws Exception {
                        if (res.isSuccess()) {
                            message.setOriginalUrl(res.getOriginalUrl());
                            message.setPreviewUrl(res.getPreviewUrl());
                            message.setImageWidth(res.getWidth());
                            message.setImageHeight(res.getHeight());
                            message.setSize((int) res.getOriginalSize());
                            return message;
                        } else {
                            throw new CustomException(ExceptionCode.ChatImageRescUploadFailure);
                        }
                    }
                });
    }

    private Observable<ChatUserMessage> upVoice(final ChatUserMessage message) {
        if (!StringUtils.isEmpty(message.getOriginalUrl())) {
            return Observable.just(message);
        }

        return UploadService.upVoice(new File(message.getLocalUrl()))
                .map(new Function<UpResxResponse, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(UpResxResponse res) throws Exception {
                        if (res.isSuccess()) {
                            message.setOriginalUrl(res.getOriginalUrl());
                            message.setSize((int) res.getOriginalSize());
                            return message;
                        } else {
                            throw new CustomException(ExceptionCode.ChatVoiceRescUploadFailure);
                        }
                    }
                });
    }

    private Observable<ChatUserMessage> upVideo(final ChatUserMessage message) {
        if (!StringUtils.isEmpty(message.getOriginalUrl())) {
            return Observable.just(message);
        }

        return new UpVideoChunk(message.getLocalUrl())
                .process()
                .map(new Function<UpVideoChunk.VideoModel, ChatUserMessage>() {
                    @Override
                    public ChatUserMessage apply(UpVideoChunk.VideoModel model) throws Exception {
                        if (model.isSuccess) {
                            message.setOriginalUrl(model.originalUrl);
                            message.setPreviewUrl(model.previewUrl);
                            message.setSize(model.originalSize);
                            return message;
                        } else {
                            throw new CustomException(ExceptionCode.ChatVideoRescUploadFailure);
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
                                emitter.onError(new CustomException(ExceptionCode
                                        .ChatSendUserMessageFailure));
                            }

                            @Override
                            public void unconnect() {
                                emitter.onError(new CustomException(ExceptionCode
                                        .IMServiceDisconnect));
                            }
                        };

                IMClientEntry.sendChatUserMessage(userMessage.getToUserId(), userMessage
                                .getContentType(), userMessage.getContent(), userMessage
                                .getOriginalUrl(), userMessage.getPreviewUrl(),
                        userMessage.getImageWidth(), userMessage.getImageHeight(), userMessage
                                .getSize(), userMessage.getPlayTime(), sendOperateListener);
            }
        });
    }

    public void playAudio(final ImageView iv, final ChatUserMessage message) {
        if (message == null) return;
        if (audioPlay != null && audioPlay.getStatus() == AudioPlay.PLAY_PROCESSING) {
            audioPlay.stop();
            return;
        }

        final String showUrl = ChatImageViewBean.getShowUrl(message.getLocalUrl(), message
                .getPreviewUrl(), message.getOriginalUrl());
        if (StringUtils.isBlank(showUrl)) {
            Toast.makeText(context, "语音不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        final int playDirection = (message.getFromUserId() == UserSP.getUserId()) ? AudioPlay
                .PLAY_RIGHT : AudioPlay.PLAY_LEFT;
        if (showUrl.startsWith("http")) {
            final String localFilePath = AudioRecorderButton.getAudioStoreDir(context) + showUrl
                    .substring(showUrl.lastIndexOf("/") + 1, showUrl.length());
            //如果是远程地址就下载文件
            DownloadService.downloadFile(showUrl)
                    .map(new Function<ResponseBody, Boolean>() {
                        @Override
                        public Boolean apply(ResponseBody responseBody) throws Exception {
                            if (responseBody == null || responseBody.contentLength() <= 0)
                                return false;
                            return FileUtils.writeFile(localFilePath, responseBody.byteStream());
                        }
                    })
                    .filter(new Predicate<Boolean>() {
                        @Override
                        public boolean test(Boolean succ) throws Exception {
                            if (succ) {
                                if (DaoUtils.getChatUserMessageManagerInstance().updateLocalPath
                                        (message.getMsgId(), localFilePath)) {
                                    message.setLocalUrl(localFilePath);
                                    return true;
                                }
                            }
                            return false;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean succ) throws Exception {
                            audioPlay = new AudioPlay(iv, playDirection);
                            audioPlay.start(showUrl, message.getPlayTime());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(context, "语音下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            if (!FileUtils.isExists(showUrl)) {
                Toast.makeText(context, "语音播放失败", Toast.LENGTH_SHORT).show();
                return;
            }
            audioPlay = new AudioPlay(iv, playDirection);
            audioPlay.start(showUrl, message.getPlayTime());
        }
    }

    public void stopAudio() {
        if (audioPlay != null) {
            audioPlay.stop();
        }
    }
}
