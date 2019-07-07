package com.hwl.beta.ui.immsg.listen;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.im.imaction.AbstractMessageListenExecutor;
import com.hwl.imcore.improto.ImMessageResponse;
import com.hwl.imcore.improto.ImMessageType;
import com.hwl.imcore.improto.ImCircleOperateMessageContent;
import com.hwl.imcore.improto.ImCircleOperateMessageResponse;

import java.util.Date;

public class CircleOperateMessageListen extends
        AbstractMessageListenExecutor<ImCircleOperateMessageResponse> {

    private ImCircleOperateMessageResponse response;
    private ImCircleOperateMessageContent messageContent;

    @Override
    public void executeCore(ImMessageType messageType,
                            ImCircleOperateMessageResponse response) {
        super.executeCore(messageType, response);
        this.response = response;
        messageContent = response.getCircleOperateMessageContent();

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
    public ImCircleOperateMessageResponse getResponse(ImMessageResponse response) {
        return response.getCircleOperateMessageResponse();
    }

    private void createMessage(int type) {
        CircleMessage model = new CircleMessage();
        model.setCircleId(messageContent.getCircleId());
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
        DaoUtils.getCircleMessageManagerInstance().save(model);
    }

    private void addComment() {
        if (messageContent.getCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0 ||
                messageContent.getCommentId() <= 0)
            return;

        CircleComment commentInfo =
				DaoUtils.getCircleManagerInstance().getComment(messageContent.getCircleId(),
                messageContent.getPostUser().getUserId(),
                messageContent.getCommentId());
        if (commentInfo != null) return;

        commentInfo = new CircleComment(messageContent.getCommentId(),
                messageContent.getCircleId(),
                messageContent.getPostUser().getUserId(),
                messageContent.getPostUser().getUserName(),
                messageContent.getPostUser().getUserImage(),
                messageContent.getReplyUser().getUserId(),
                messageContent.getReplyUser().getUserName(),
                messageContent.getReplyUser().getUserImage(),
                messageContent.getCommentCont(),
                new Date(response.getBuildTime()));
        DaoUtils.getCircleManagerInstance().saveComment(commentInfo);

        createMessage(DBConstant.CIRCLE_TYPE_COMMENT);

        //hint to ui
        MessageCountSP.setCircleMessageCountIncrease();
        EventBusUtil.sendCircleMessageUpdateEvent();
    }

    private void cancelComment() {
        if (messageContent.getCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0 ||
                messageContent.getCommentId() <= 0)
            return;

        DaoUtils.getCircleManagerInstance().deleteComment(messageContent.getCircleId(),
				messageContent.getPostUser().getUserId(), messageContent.getCommentId());

        DaoUtils.getCircleMessageManagerInstance().updateStatus(messageContent.getCircleId(),
				DBConstant.CIRCLE_TYPE_COMMENT, messageContent.getPostUser().getUserId(),
				messageContent.getCommentId());

        MessageCountSP.setCircleMessageCountReduce();
    }

    private void addLike() {
        if (messageContent.getCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0)
            return;

        //check local db exists
        CircleLike likeInfo =
				DaoUtils.getCircleManagerInstance().getLike(messageContent.getCircleId(),
						messageContent.getPostUser().getUserId());
        if (likeInfo != null) return;

        likeInfo = new CircleLike();
        likeInfo.setCircleId(messageContent.getCircleId());
        likeInfo.setLikeUserId(messageContent.getPostUser().getUserId());
        likeInfo.setLikeUserName(messageContent.getPostUser().getUserName());
        likeInfo.setLikeUserImage(messageContent.getPostUser().getUserImage());
        likeInfo.setLikeTime(new Date(response.getBuildTime()));
        DaoUtils.getCircleManagerInstance().saveLike(likeInfo);

        createMessage(DBConstant.CIRCLE_TYPE_LIKE);

        //hint to ui
        MessageCountSP.setCircleMessageCountIncrease();
        EventBusUtil.sendCircleMessageUpdateEvent();
    }

    private void cancelLike() {
        if (messageContent.getCircleId() <= 0 ||
                messageContent.getPostUser() == null ||
                messageContent.getPostUser().getUserId() <= 0)
            return;

        DaoUtils.getCircleManagerInstance().deleteLike(messageContent.getCircleId(), messageContent.getPostUser().getUserId());

        DaoUtils.getCircleMessageManagerInstance().updateStatus(messageContent.getCircleId(), DBConstant.CIRCLE_TYPE_LIKE, messageContent.getPostUser().getUserId(), 0);

        MessageCountSP.setCircleMessageCountReduce();
    }
}