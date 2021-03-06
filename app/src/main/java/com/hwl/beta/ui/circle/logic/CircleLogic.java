package com.hwl.beta.ui.circle.logic;

import android.text.TextUtils;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.NetCircleMatchInfo;
import com.hwl.beta.net.circle.body.AddCircleCommentInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCircleInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCommentInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleCommentsResponse;
import com.hwl.beta.net.circle.body.GetCircleDetailResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.net.resx.ResxService;
import com.hwl.beta.net.resx.ResxType;
import com.hwl.beta.net.resx.body.ImageUploadResponse;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserCircleBackImageResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.convert.DBCircleAction;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CircleLogic implements CircleStandard {

    final static int PAGE_COUNT = 15;
    final static int COMMENT_PAGE_COUNT = 15;

    @Override
    public Observable<List<Circle>> loadLocalInfos() {
        return Observable.fromCallable(new Callable<List<Circle>>() {
            @Override
            public List<Circle> call() {
                List<Circle> circles = DaoUtils.getCircleManagerInstance().getCircles(PAGE_COUNT,
                        COMMENT_PAGE_COUNT);
                if (circles == null)
                    circles = new ArrayList<>();

                return circles;
            }
        })
                .doOnNext(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> circles) throws Exception {
                        if (circles.size() <= 0) {
                            circles.add(new Circle(DBConstant.CIRCLE_ITEM_NULL));
                        }
                        circles.add(0, new Circle(DBConstant.CIRCLE_ITEM_HEAD));
                        circles.add(1, new Circle(DBConstant.CIRCLE_ITEM_MSGCOUNT));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    /*
     * 首先获取本地已经存在的第一页的数据id和updatetime
     * 根据id和updatetime去服务器请求数据，如果服务器端获取的数据中有存在的，就匹配是否需要更新，如果要更新则返回到客户端，否则返回null
     * 客户端同样也要再次进行判断，如果更新时间不匹配，则进行存储，否则不存储到本地
     * */
    @Override
    public Observable<List<Circle>> loadServerInfos(final long minCircleId,
                                                    final List<Circle> localInfos) {
        // minCircleId <=0 and get new data
        // minCircleId >0 and get old data
        return CircleService.getCircleInfos(UserSP.getUserId(), minCircleId, PAGE_COUNT,
                this.getMatchInfos(minCircleId, localInfos))
                .map(new Function<GetCircleInfosResponse, List<Circle>>() {
                    @Override
                    public List<Circle> apply(GetCircleInfosResponse response) {
                        List<Circle> infos =
                                DBCircleAction.convertToCircleInfos(response.getCircleInfos());
                        if (infos == null) infos = new ArrayList<>();
                        return infos;
                    }
                })
                .doOnNext(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        boolean isClearLocalInfo = false;
                        if (infos != null && infos.size() >= PAGE_COUNT && localInfos != null && localInfos.size() > 0) {
                            isClearLocalInfo =
                                    (infos.get(infos.size() - 1).getCircleId() - localInfos.get(0).getCircleId()) > 1;
                        }

                        if (isClearLocalInfo) {
                            DaoUtils.getCircleManagerInstance().clearAll();
                        } else {
                            DaoUtils.getCircleManagerInstance().deleteAll(infos);
                        }
                        DaoUtils.getCircleManagerInstance().saveAll(infos);
                    }
                });
    }

    private List<NetCircleMatchInfo> getMatchInfos(long minCircleId, List<Circle> localInfos) {
        List<NetCircleMatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < localInfos.size(); i++) {
            if (minCircleId <= 0) {
                if (localInfos.get(i).getCircleId() <= 0) continue;
            } else {
                if (localInfos.get(i).getCircleId() >= minCircleId) continue;
            }

            if (TextUtils.isEmpty(localInfos.get(i).getUpdateTime())) continue;

            matchInfos.add(new NetCircleMatchInfo(localInfos.get(i).getCircleId(),
                    localInfos.get(i).getUpdateTime()));
            if (matchInfos.size() >= PAGE_COUNT) break;
        }
        return matchInfos;
    }

    @Override
    public Observable deleteInfo(final long circleId) {
        return CircleService.deleteCircleInfo(circleId)
                .map(new Function<DeleteCircleInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(DeleteCircleInfoResponse response) throws Exception {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            DaoUtils.getCircleManagerInstance().deleteAll(circleId);
                        } else {
                            throw new Exception("Delete circle info failed.");
                        }
                        return true;
                    }
                });
    }

    @Override
    public Observable<CircleLike> setLike(final Circle info, final boolean isLike) {
        if (info == null || info.getCircleId() <= 0) {
            return Observable.error(new Throwable("Circle id con't be empty."));
        }
        return CircleService.setLikeInfo(isLike ? 1 : 0, info.getCircleId(), info.getUpdateTime())
                .map(new Function<SetLikeInfoResponse, CircleLike>() {
                    @Override
                    public CircleLike apply(SetLikeInfoResponse response) throws Exception {
                        if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Set user like info failed.");
                        }

                        if (!TextUtils.isEmpty(response.getCircleLastUpdateTime())) {
                            DaoUtils.getCircleManagerInstance().setUpdateTime(info.getCircleId(),
                                    response.getCircleLastUpdateTime());
                        }

                        CircleLike likeInfo = new CircleLike();
                        likeInfo.setCircleId(info.getCircleId());
                        likeInfo.setLikeUserId(UserSP.getUserId());
                        likeInfo.setLikeUserName(UserSP.getUserName());
                        likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                        likeInfo.setLastUpdateTime(response.getCircleLastUpdateTime());
                        if (isLike) {
                            DaoUtils.getCircleManagerInstance().saveLike(likeInfo);
                        } else {
                            DaoUtils.getCircleManagerInstance().deleteLike(info.getCircleId(),
                                    UserSP.getUserId());
                        }

                        return likeInfo;
                    }
                })
                .doOnNext(new Consumer<CircleLike>() {
                    @Override
                    public void accept(CircleLike likeInfo) {
                        if (info.getPublishUserId() == likeInfo.getLikeUserId()) return;
                        //send im message
                        IMClientEntry.sendCircleLikeMessage(info.getCircleId(), isLike,
                                info.getPublishUserId(),
                                new IMDefaultSendOperateListener("setLike"));
                    }
                });
    }

    @Override
    public Observable updateCircleBackImage(String localPath) {
        if (StringUtils.isBlank(localPath)) {
            return Observable.error(new Throwable("Up load data can't be emtpy."));
        }

        return ResxService.imageUpload(new File(localPath), ResxType.NEARCIRCLEPOST)
                .flatMap(new Function<ImageUploadResponse,
                        Observable<SetUserCircleBackImageResponse>>() {
                    @Override
                    public Observable<SetUserCircleBackImageResponse> apply(ImageUploadResponse res)
                            throws Exception {
                        if (!res.getResxImageResult().getSuccess()) {
                            throw new Exception("Up load image failed.");
                        }

                        return UserService.setUserCircleBackImage(res.getResxImageResult().getResxAccessUrl());
                    }
                })
                .map(new Function<SetUserCircleBackImageResponse, String>() {
                    @Override
                    public String apply(SetUserCircleBackImageResponse res) throws Exception {
                        if (res.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Update circle back image failed.");
                        }

                        UserSP.setUserCirclebackimage(res.getCircleBackImageUrl());
                        return res.getCircleBackImageUrl();
                    }
                });
    }

    @Override
    public Observable<Circle> loadLocalDetails(final long circleId) {
        if (circleId <= 0) {
            return Observable.error(new Throwable("Circle id con't be empty."));
        }

        return Observable.fromCallable(new Callable<Circle>() {
            @Override
            public Circle call() {
                return DaoUtils.getCircleManagerInstance().getCircleDetails(circleId,
                        COMMENT_PAGE_COUNT);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Circle> loadServerDetails(final long circleId, String updateTime) {
        if (circleId <= 0) {
            return Observable.error(new Throwable("Circle id con't be empty."));
        }

        return CircleService.getCircleDetail(circleId, updateTime)
                .map(new Function<GetCircleDetailResponse, Circle>() {
                    @Override
                    public Circle apply(GetCircleDetailResponse response) {
                        if (response != null && response.getCircleInfo() != null) {
                            Circle info =
                                    DBCircleAction.convertToCircleInfo(response.getCircleInfo());
                            DaoUtils.getCircleManagerInstance().deleteAll(info.getCircleId());
                            DaoUtils.getCircleManagerInstance().save(info);
                            return info;
                        }

                        return null;
                    }
                });
    }

    @Override
    public Observable<List<CircleComment>> getComments(Circle info, long lastCommentId) {
        if (info == null || info.getCircleId() <= 0) {
            return Observable.error(new Throwable("Circle id con't be empty."));
        }

        return CircleService.getCircleComments(info.getCircleId(), lastCommentId,
                COMMENT_PAGE_COUNT)
                .map(new Function<GetCircleCommentsResponse, List<CircleComment>>() {
                    @Override
                    public List<CircleComment> apply(GetCircleCommentsResponse response) {
                        if (response.getCircleCommentInfos() == null)
                            return new ArrayList<>();

                        return DBCircleAction.convertToCircleCommentInfos(response.getCircleCommentInfos());
                    }
                })
                .doOnNext(new Consumer<List<CircleComment>>() {
                    @Override
                    public void accept(List<CircleComment> comments) {
                        DaoUtils.getCircleManagerInstance().saveNonExistentComments(comments);
                    }
                });
    }

    @Override
    public Observable<CircleComment> addComment(final Circle info,
                                                final String content,
                                                final long replyUserId) {
        if (info == null || info.getCircleId() <= 0) {
            return Observable.error(new Throwable("Circle id con't be empty."));
        }

        return CircleService.addComment(info.getCircleId(), content, replyUserId,
                info.getUpdateTime())
                .map(new Function<AddCircleCommentInfoResponse, CircleComment>() {
                    @Override
                    public CircleComment apply(AddCircleCommentInfoResponse response) throws Exception {
                        if (response.getCommentInfo() == null || response.getCommentInfo().getCommentId() <= 0)
                            throw new Exception("Post user comment info failed.");

                        if (!TextUtils.isEmpty(response.getCircleLastUpdateTime())) {
                            DaoUtils.getCircleManagerInstance().setUpdateTime(info.getCircleId(),
                                    response.getCircleLastUpdateTime());
                        }

                        CircleComment commentInfo =
                                DBCircleAction.convertToCircleCommentInfo(response.getCommentInfo());
                        commentInfo.setLastUpdateTime(response.getCircleLastUpdateTime());
                        DaoUtils.getCircleManagerInstance().saveComment(commentInfo);
                        return commentInfo;
                    }
                })
                .doOnNext(new Consumer<CircleComment>() {
                    @Override
                    public void accept(CircleComment comment) {
                        if (info.getPublishUserId() == comment.getCommentUserId() && comment.getReplyUserId() <= 0)
                            return;
                        //send im message
                        IMClientEntry.sendCircleCommentMessage(info.getCircleId(),
                                comment.getCommentId(),
                                comment.getContent(),
                                info.getPublishUserId(),
                                comment.getReplyUserId(),
                                new IMDefaultSendOperateListener("addComment"));
                    }
                });
    }

    @Override
    public Observable<String> deleteComment(final Circle info, final CircleComment comment) {
        return CircleService.deleteCommentInfo(comment.getCommentId(), info.getUpdateTime())
                .map(new Function<DeleteCommentInfoResponse, String>() {
                    @Override
                    public String apply(DeleteCommentInfoResponse response) throws Exception {
                        if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Delete circle info failed.");
                        }

                        if (!TextUtils.isEmpty(response.getCircleLastUpdateTime())) {
                            DaoUtils.getCircleManagerInstance().setUpdateTime(comment.getCircleId(), response.getCircleLastUpdateTime());
                        }

                        DaoUtils.getCircleManagerInstance().deleteComment(comment.getCircleId(),
                                comment.getCommentUserId(), comment.getCommentId());
                        return response.getCircleLastUpdateTime();
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String lastUpdateTime) {
                        if (info.getPublishUserId() == comment.getCommentUserId() && comment.getReplyUserId() <= 0)
                            return;

                        //send im message
                        IMClientEntry.sendCircleCancelCommentMessage(info.getCircleId(),
                                comment.getCommentId(),
                                info.getPublishUserId(),
                                comment.getReplyUserId(),
                                new IMDefaultSendOperateListener("deleteComment"));
                    }
                });
    }
}
