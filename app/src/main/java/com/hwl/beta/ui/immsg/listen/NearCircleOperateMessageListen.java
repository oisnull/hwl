package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImNearCircleOperateMessageContent;
import com.hwl.imcore.improto.ImNearCircleOperateMessageResponse;

import java.util.Date;

public class NearCircleOperateMessageListen extends
        AbstractMessageListenExecutor<ImNearCircleOperateMessageResponse> {

    private ImNearCircleOperateMessageResponse response;
    private ImNearCircleOperateMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType,
                            ImNearCircleOperateMessageResponse response) {
        super.executeCore(messageType, response);
        this.response = response;
        messageContent = response.getNearCircleOperateMessageContent();

        switch (messageContent.getOperateType()) {
            case AddLike:
                addLike();
                break;
            case CancelLike:
                cancelLike();
                break;
            case PostComment:
                addComment();
                break;
            case CancelComment:
                cancelComment();
                break;
        }
    }

    @Override
    public ImNearCircleOperateMessageResponse getResponse(ImMessageResponse response) {
        return response.getNearCircleOperateMessageResponse();
    }

    private void createMessage(int type) {
        NearCircleMessage model = new NearCircleMessage();
        model.setNearCircleId(messageContent.getNearCircleId());
        model.setUserId(messageContent.getPostUser().getUserId());
        model.setUserName(messageContent.getPostUser().getUserName());
        model.setUserImage(messageContent.getPostUser().getUserImage());
        model.setContent("");
        model.setType(type);
        model.setStatus(DBConstant.STATUS_UNREAD);
        model.setComment(messageContent.getCommentCont());
        model.setCommentId(messageContent.getCommentId());
        model.setReplyUserId(messageContent.getReplyUser().getUserId());
        model.setReplyUserName(messageContent.getReplyUser().getUserName());
        model.setActionTime(new Date(response.getBuildTime()));
        DaoUtils.getNearCircleMessageManagerInstance().save(model);
    }

    private void addComment() {
        if (messageContent.getNearCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0 ||
                messageContent.getCommentId() <= 0)
            return;

        NearCircleComment commentInfo =
				DaoUtils.getNearCircleManagerInstance().getComment(messageContent.getNearCircleId(),
                messageContent.getPostUser().getUserId(),
                messageContent.getCommentId());
        if (commentInfo != null) return;

        commentInfo = new NearCircleComment(messageContent.getCommentId(),
                messageContent.getNearCircleId(),
                messageContent.getPostUser().getUserId(),
                messageContent.getPostUser().getUserName(),
                messageContent.getPostUser().getUserImage(),
                messageContent.getReplyUser().getUserId(),
                messageContent.getReplyUser().getUserName(),
                messageContent.getReplyUser().getUserImage(),
                messageContent.getCommentCont(),
                new Date(response.getBuildTime()));
        DaoUtils.getNearCircleManagerInstance().saveComment(commentInfo);

        createMessage(DBConstant.CIRCLE_TYPE_COMMENT);

        //hint to ui
        MessageCountSP.setNearCircleMessageCountIncrease();
        EventBusUtil.sendNearMessageUpdateEvent();
    }

    private void cancelComment() {
        if (messageContent.getNearCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0 ||
                messageContent.getCommentId() <= 0)
            return;

        DaoUtils.getNearCircleManagerInstance().deleteComment(messageContent.getNearCircleId(),
				messageContent.getPostUser().getUserId(), messageContent.getCommentId());

        DaoUtils.getNearCircleMessageManagerInstance().updateStatus(messageContent.getNearCircleId(), DBConstant.CIRCLE_TYPE_COMMENT, messageContent.getPostUser().getUserId(), messageContent.getCommentId());

        MessageCountSP.setNearCircleMessageCountReduce();
    }

    private void addLike() {
        if (messageContent.getNearCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0)
            return;

        //check local db exists
        NearCircleLike likeInfo =
				DaoUtils.getNearCircleManagerInstance().getLike(messageContent.getNearCircleId(),
						messageContent.getPostUser().getUserId());
        if (likeInfo != null) return;

        likeInfo = new NearCircleLike();
        likeInfo.setNearCircleId(messageContent.getNearCircleId());
        likeInfo.setLikeUserId(messageContent.getPostUser().getUserId());
        likeInfo.setLikeUserName(messageContent.getPostUser().getUserName());
        likeInfo.setLikeUserImage(messageContent.getPostUser().getUserImage());
        likeInfo.setLikeTime(new Date(response.getBuildTime()));
        DaoUtils.getNearCircleManagerInstance().saveLike(likeInfo);

        createMessage(DBConstant.CIRCLE_TYPE_LIKE);

        //hint to ui
        MessageCountSP.setNearCircleMessageCountIncrease();
        EventBusUtil.sendNearMessageUpdateEvent();
    }

    private void cancelLike() {
        if (messageContent.getNearCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0)
            return;

        DaoUtils.getNearCircleManagerInstance().deleteLike(messageContent.getNearCircleId(), messageContent.getPostUser().getUserId());

        DaoUtils.getNearCircleMessageManagerInstance().updateStatus(messageContent.getNearCircleId(), DBConstant.CIRCLE_TYPE_LIKE, messageContent.getPostUser().getUserId(), 0);

        MessageCountSP.setNearCircleMessageCountReduce();
    }
}