package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.chat.standard.ChatUserStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatUserLogic implements ChatUserStandard {
    static int pageSize = 10;

    @Override
    public Friend getChatUserInfo(long userId, String userName, String userHeadImage) {
        if (userId <= 0) return null;
        Friend user = DaoUtils.getFriendManagerInstance().get(userId);
        if (user == null) {
            user = DBFriendAction.convertToFriendInfo(userId, userName, userHeadImage, false);
        }
        return user;
    }

    @Override
    public List<ChatUserMessage> getTopLocalMessages(long userId) {
        List<ChatUserMessage> messages = DaoUtils.getChatUserMessageManagerInstance()
                .getFromUserMessages(UserSP.getUserId
                        (), userId, 0, pageSize);
        sortMessages(messages);
        return messages;
    }

    private void sortMessages(List<ChatUserMessage> messageList) {
        if (messageList == null || messageList.size() <= 0) return;
        Collections.sort(messageList, new Comparator<ChatUserMessage>() {
            public int compare(ChatUserMessage arg0, ChatUserMessage arg1) {
                return arg0.getMsgId().compareTo(arg1.getMsgId());
            }
        });
    }

    @Override
    public void loadLocalMessages(final long userId, final long minMsgId,
                                  final DefaultCallback<List<ChatUserMessage>, String> callback) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {
                List<ChatUserMessage> messages = DaoUtils.getChatUserMessageManagerInstance()
                        .getFromUserMessages(UserSP.getUserId
                                (), userId, minMsgId, pageSize);
                emitter.onNext(messages);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChatUserMessage>>() {
                    @Override
                    public void accept(List<ChatUserMessage> msgs) {
                        callback.success(msgs);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        callback.error(throwable.getMessage());
                    }
                });
    }

    @Override
    public void clearRecordMessageCount(final long userId) {
        if (userId <= 0) return;
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {
                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                        .getUserRecord(UserSP.getUserId(), userId);
                if (recordMessage != null && recordMessage.getUnreadCount() > 0) {
                    DaoUtils.getChatRecordMessageManagerInstance().clearUnreadCount(recordMessage);
                    EventBusUtil.sendChatRecordMessageNoSortEvent(recordMessage);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe();
    }
}
