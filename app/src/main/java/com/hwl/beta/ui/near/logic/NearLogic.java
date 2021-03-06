package com.hwl.beta.ui.near.logic;

import android.text.TextUtils;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.NetNearCircleMatchInfo;
import com.hwl.beta.net.near.body.AddNearCommentResponse;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoResponse;
import com.hwl.beta.net.near.body.DeleteNearCommentResponse;
import com.hwl.beta.net.near.body.GetNearCircleDetailResponse;
import com.hwl.beta.net.near.body.GetNearCircleInfosResponse;
import com.hwl.beta.net.near.body.GetNearCommentsResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.immsg.IMClientEntry;
import com.hwl.beta.ui.immsg.IMDefaultSendOperateListener;
import com.hwl.beta.ui.near.standard.NearStandard;
import com.hwl.beta.utils.DateUtils;
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NearLogic implements NearStandard {

    final static int PAGE_COUNT = 15;
    final static int COMMENT_PAGE_COUNT = 15;

    @Override
    public Observable<List<NearCircle>> loadLocalInfos() {
        return Observable.fromCallable(new Callable<List<NearCircle>>() {
            @Override
            public List<NearCircle> call() {
                return DaoUtils.getNearCircleManagerInstance().getNearCirclesV2(PAGE_COUNT,
                        COMMENT_PAGE_COUNT);
            }
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<NearCircle>> loadServerInfos(final long minNearCircleId,
                                                        final List<NearCircle> localInfos) {
        // minNearCircleId <=0 and get new data
        // minNearCircleId >0 and get old data
        return NearCircleService.getNearCircleInfos(minNearCircleId, PAGE_COUNT,
                this.getMatchInfos(minNearCircleId, localInfos))
                .map(new Function<GetNearCircleInfosResponse, List<NearCircle>>() {
                    @Override
                    public List<NearCircle> apply(GetNearCircleInfosResponse response) {
                        List<NearCircle> infos =
                                DBNearCircleAction.convertToNearCircleInfos(response.getNearCircleInfos());
                        if (infos == null) infos = new ArrayList<>();
                        return infos;
                    }
                })
                .doOnNext(new Consumer<List<NearCircle>>() {
                    @Override
                    public void accept(List<NearCircle> infos) {
                        boolean isClearLocalInfo = false;
                        if (infos != null && infos.size() >= PAGE_COUNT && localInfos != null && localInfos.size() > 0) {
                            isClearLocalInfo =
                                    (infos.get(infos.size() - 1).getNearCircleId() - localInfos.get(0).getNearCircleId()) > 1;
                        }

                        if (isClearLocalInfo) {
                            DaoUtils.getNearCircleManagerInstance().clearAll();
                        } else {
                            DaoUtils.getNearCircleManagerInstance().deleteAll(infos);
                        }
                        DaoUtils.getNearCircleManagerInstance().saveAll(infos);
                    }
                });
    }

    private List<NetNearCircleMatchInfo> getMatchInfos(long minNearCircleId,
                                                       List<NearCircle> localInfos) {
        List<NetNearCircleMatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < localInfos.size(); i++) {
            if (minNearCircleId <= 0) {
                if (localInfos.get(i).getNearCircleId() <= 0) continue;
            } else {
                if (localInfos.get(i).getNearCircleId() >= minNearCircleId) continue;
            }

            if (TextUtils.isEmpty(localInfos.get(i).getUpdateTime())) continue;

            matchInfos.add(new NetNearCircleMatchInfo(localInfos.get(i).getNearCircleId(),
                    localInfos.get(i).getUpdateTime()));
            if (matchInfos.size() >= PAGE_COUNT) break;
        }
        return matchInfos;
    }

    @Override
    public Observable deleteInfo(final long nearCircleId) {
        return NearCircleService.deleteNearCircleInfo(nearCircleId)
                .map(new Function<DeleteNearCircleInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(DeleteNearCircleInfoResponse response) throws Exception {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            DaoUtils.getNearCircleManagerInstance().deleteAll(nearCircleId);
                        } else {
                            throw new Exception("Delete near circle info failed.");
                        }
                        return true;
                    }
                });
    }

    @Override
    public Observable<NearCircleLike> setLike(final NearCircle info, final boolean isLike) {
        if (info == null || info.getNearCircleId() <= 0) {
            return Observable.error(new Throwable("Near circle id con't be empty."));
        }
        return NearCircleService.setNearLikeInfo(isLike ? 1 : 0, info.getNearCircleId(),
                info.getUpdateTime())
                .map(new Function<SetNearLikeInfoResponse, NearCircleLike>() {
                    @Override
                    public NearCircleLike apply(SetNearLikeInfoResponse response) throws Exception {
                        if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Set user like info failed.");
                        }

                        if (!TextUtils.isEmpty(response.getNearCircleLastUpdateTime())) {
                            DaoUtils.getNearCircleManagerInstance().setUpdateTime(info.getNearCircleId(), response.getNearCircleLastUpdateTime());
                        }

                        NearCircleLike likeInfo = new NearCircleLike();
                        likeInfo.setNearCircleId(info.getNearCircleId());
                        likeInfo.setLikeUserId(UserSP.getUserId());
                        likeInfo.setLikeUserName(UserSP.getUserName());
                        likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                        likeInfo.setLastUpdateTime(response.getNearCircleLastUpdateTime());
                        if (isLike) {
                            DaoUtils.getNearCircleManagerInstance().saveLike(likeInfo);
                        } else {
                            DaoUtils.getNearCircleManagerInstance().deleteLike(info.getNearCircleId(),
                                    UserSP.getUserId());
                        }

                        return likeInfo;
                    }
                })
                .doOnNext(new Consumer<NearCircleLike>() {
                    @Override
                    public void accept(NearCircleLike likeInfo) {
                        if (info.getPublishUserId() == likeInfo.getLikeUserId()) return;
                        //send im message
                        IMClientEntry.sendNearCircleLikeMessage(info.getNearCircleId(), isLike,
                                info.getPublishUserId(),
                                new IMDefaultSendOperateListener("setLike"));
                    }
                });
    }

    @Override
    public Observable<List<NearCircleComment>> getComments(NearCircle info, long lastCommentId) {
        if (info == null || info.getNearCircleId() <= 0) {
            return Observable.error(new Throwable("Near circle id con't be empty."));
        }

        return NearCircleService.getNearComments(info.getNearCircleId(), lastCommentId,
                COMMENT_PAGE_COUNT)
                .map(new Function<GetNearCommentsResponse, List<NearCircleComment>>() {
                    @Override
                    public List<NearCircleComment> apply(GetNearCommentsResponse response) throws Exception {
                        if (response.getNearCircleCommentInfos() == null)
                            return new ArrayList<>();

                        return DBNearCircleAction.convertToNearCircleCommentInfos(response.getNearCircleCommentInfos());
                    }
                })
                .doOnNext(new Consumer<List<NearCircleComment>>() {
                    @Override
                    public void accept(List<NearCircleComment> comments) {
                        DaoUtils.getNearCircleManagerInstance().saveNonExistentComments(comments);
                    }
                });
    }

    @Override
    public Observable<NearCircleComment> addComment(final NearCircle info,
                                                    final String content,
                                                    final long replyUserId) {
        if (info == null || info.getNearCircleId() <= 0) {
            return Observable.error(new Throwable("Near circle id con't be empty."));
        }

        return NearCircleService.addComment(info.getNearCircleId(), content, replyUserId,
                info.getUpdateTime())
                .map(new Function<AddNearCommentResponse, NearCircleComment>() {
                    @Override
                    public NearCircleComment apply(AddNearCommentResponse response) throws Exception {
                        if (response.getNearCircleCommentInfo() == null || response.getNearCircleCommentInfo().getCommentId() <= 0)
                            throw new Exception("Post user comment info failed.");

                        if (!TextUtils.isEmpty(response.getNearCircleLastUpdateTime())) {
                            DaoUtils.getNearCircleManagerInstance().setUpdateTime(info.getNearCircleId(), response.getNearCircleLastUpdateTime());
                        }

                        NearCircleComment commentInfo =
                                DBNearCircleAction.convertToNearCircleCommentInfo(response.getNearCircleCommentInfo());
                        commentInfo.setLastUpdateTime(response.getNearCircleLastUpdateTime());
                        DaoUtils.getNearCircleManagerInstance().saveComment(commentInfo);
                        return commentInfo;
                    }
                })
                .doOnNext(new Consumer<NearCircleComment>() {
                    @Override
                    public void accept(NearCircleComment comment) {
                        if (info.getPublishUserId() == comment.getCommentUserId() && comment.getReplyUserId() <= 0)
                            return;
                        //send im message
                        IMClientEntry.sendNearCircleCommentMessage(info.getNearCircleId(),
                                comment.getCommentId(), content,
                                info.getPublishUserId(), replyUserId,
                                new IMDefaultSendOperateListener("addComment"));
                    }
                });
    }

    @Override
    public Observable<String> deleteComment(final NearCircle info,
                                            final NearCircleComment comment) {
        return NearCircleService.deleteComment(comment.getCommentId(), info.getUpdateTime())
                .map(new Function<DeleteNearCommentResponse, String>() {
                    @Override
                    public String apply(DeleteNearCommentResponse response) throws Exception {
                        if (response.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Delete near circle info failed.");
                        }

                        if (!TextUtils.isEmpty(response.getNearCircleLastUpdateTime())) {
                            DaoUtils.getNearCircleManagerInstance().setUpdateTime(comment.getNearCircleId(), response.getNearCircleLastUpdateTime());
                        }

                        DaoUtils.getNearCircleManagerInstance().deleteComment(comment.getNearCircleId(), comment.getCommentUserId(), comment.getCommentId());
                        return StringUtils.nullStrToEmpty(response.getNearCircleLastUpdateTime());
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String lastUpdateTime) {
                        if (info.getPublishUserId() == comment.getCommentUserId() && comment.getReplyUserId() <= 0)
                            return;
                        //send im message
                        IMClientEntry.sendNearCircleCancelCommentMessage(info.getNearCircleId(),
                                comment.getCommentId(),
                                info.getPublishUserId(),
                                comment.getReplyUserId(),
                                new IMDefaultSendOperateListener("deleteComment"));
                    }
                });
    }

    @Override
    public Observable<NearCircle> loadLocalDetails(final long nearCircleId) {
        if (nearCircleId <= 0) {
            return Observable.error(new Throwable("Near circle id con't be empty."));
        }

        return Observable.fromCallable(new Callable<NearCircle>() {
            @Override
            public NearCircle call() {
                return DaoUtils.getNearCircleManagerInstance().getNearCircle(nearCircleId,
                        COMMENT_PAGE_COUNT);
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<NearCircle> loadServerDetails(final long nearCircleId, String updateTime) {
        if (nearCircleId <= 0) {
            return Observable.error(new Throwable("Near circle id con't be empty."));
        }

        return NearCircleService.getNearCircleDetail(nearCircleId, updateTime)
                .map(new Function<GetNearCircleDetailResponse, NearCircle>() {
                    @Override
                    public NearCircle apply(GetNearCircleDetailResponse response) {
                        if (response != null && response.getNearCircleInfo() != null) {
                            NearCircle info =
                                    DBNearCircleAction.convertToNearCircleInfo(response.getNearCircleInfo());
                            DaoUtils.getNearCircleManagerInstance().deleteAll(nearCircleId);
                            DaoUtils.getNearCircleManagerInstance().save(info);
                            return info;
                        }

                        return null;
                    }
                });
    }

}
