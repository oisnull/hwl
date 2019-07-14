package com.hwl.beta.ui.chat.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.ui.chat.standard.ChatGroupStandard;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.utils.StringUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ChatGroupLogic implements ChatGroupStandard {
    static int pageSize = 10;

    @Override
    public GroupInfo getChatGroupInfo(String groupGuid) {
        GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(groupGuid);
        if (groupInfo == null && UserPosSP.getGroupGuid().equals(groupGuid)) {
            groupInfo = new GroupInfo();
            groupInfo.setGroupGuid(groupGuid);
            groupInfo.setGroupName(UserPosSP.getNearDesc());
            groupInfo.setIsSystem(true);
            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
        }
        return groupInfo;
    }

    @Override
    public List<ChatGroupMessage> getTopLocalMessages(String groupGuid) {
        List<ChatGroupMessage> messages = DaoUtils.getChatGroupMessageManagerInstance()
                .getGroupMessages(groupGuid, 0, pageSize);
        sortMessages(messages);
        return messages;
    }

    private void sortMessages(List<ChatGroupMessage> messageList) {
        if (messageList == null || messageList.size() <= 0) return;
        Collections.sort(messageList, new Comparator<ChatGroupMessage>() {
            public int compare(ChatGroupMessage arg0, ChatGroupMessage arg1) {
                return arg0.getMsgId().compareTo(arg1.getMsgId());
            }
        });
    }

    @Override
    public void loadLocalMessages(final String groupGuid, final long minMsgId,
                                  final DefaultCallback<List<ChatGroupMessage>, String> callback) {
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {
                List<ChatGroupMessage> messages = DaoUtils.getChatGroupMessageManagerInstance()
                        .getGroupMessages(groupGuid, minMsgId, pageSize);
                emitter.onNext(messages);
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChatGroupMessage>>() {
                    @Override
                    public void accept(List<ChatGroupMessage> msgs) {
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
    public void clearRecordMessageCount(final String groupGuid) {
        if (StringUtils.isEmpty(groupGuid)) return;
        Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) {
                ChatRecordMessage recordMessage = DaoUtils.getChatRecordMessageManagerInstance()
                        .getGroupRecord(groupGuid);
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
