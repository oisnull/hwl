package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.NearCircleCommentDao;
import com.hwl.beta.db.dao.NearCircleDao;
import com.hwl.beta.db.dao.NearCircleImageDao;
import com.hwl.beta.db.dao.NearCircleLikeDao;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/3/10.
 */

public class NearCircleManager extends BaseDao<NearCircle> {
    public NearCircleManager(Context context) {
        super(context);
    }

    public long save(NearCircle model) {
        if (model == null) return 0;
        return daoSession.getNearCircleDao().insertOrReplace(model);
    }

    public NearCircle getNearCircle(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleDao().load(nearCircleId);
    }

    public long save(NearCircle model, List<NearCircleImage> images) {
        if (model == null) return 0;
        long id = daoSession.getNearCircleDao().insertOrReplace(model);
        if (id > 0 && images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setNearCircleId(id);
            }
            daoSession.getNearCircleImageDao().saveInTx(images);
        }
        return id;
    }

    public void delete(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleDao.TABLENAME + " where " + NearCircleDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public List<NearCircleImage> getImages(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleImageDao().queryBuilder()
                .where(NearCircleImageDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void saveImages(long nearCircleId, List<NearCircleImage> images) {
        if (nearCircleId > 0 && images != null && images.size() > 0) {
            daoSession.getNearCircleImageDao().saveInTx(images);
        }
    }

    public void deleteImages(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleImageDao.TABLENAME + " where " + NearCircleImageDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComment(long nearCircleId, long userId, int commentId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleCommentDao.TABLENAME + " where " +
                    NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId + " and " +
                    NearCircleCommentDao.Properties.CommentId.columnName + "=" + commentId + " and " +
                    NearCircleCommentDao.Properties.CommentUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComments(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleCommentDao.TABLENAME + " where " + NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public NearCircleComment getComment(long nearCircleId, long userId, int commentId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleCommentDao.Properties.CommentUserId.eq(userId))
                .where(NearCircleCommentDao.Properties.CommentId.eq(commentId))
                .unique();
    }

    public List<NearCircleComment> getComments(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void saveComment(long nearCircleId, NearCircleComment comment) {
        if (nearCircleId > 0 && comment != null) {
            daoSession.getNearCircleCommentDao().save(comment);
        }
    }

    public void saveComments(long nearCircleId, List<NearCircleComment> comments) {
        if (nearCircleId > 0 && comments != null && comments.size() > 0) {
            daoSession.getNearCircleCommentDao().saveInTx(comments);
        }
    }

    public NearCircleLike getLike(long nearCircleId, long userId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleLikeDao().queryBuilder()
                .where(NearCircleLikeDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleLikeDao.Properties.LikeUserId.eq(userId))
                .unique();
    }

    public List<NearCircleLike> getLikes(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleLikeDao().queryBuilder()
                .where(NearCircleLikeDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void deleteLikes(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleLikeDao.TABLENAME + " where " + NearCircleLikeDao.Properties.NearCircleId.columnName + " = " + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteLike(long nearCircleId, long userId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleLikeDao.TABLENAME + " where " +
                    NearCircleLikeDao.Properties.NearCircleId.columnName + " = " + nearCircleId + " and " +
                    NearCircleLikeDao.Properties.LikeUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void saveLikes(long nearCircleId, List<NearCircleLike> likes) {
        if (nearCircleId > 0 && likes != null && likes.size() > 0) {
            daoSession.getNearCircleLikeDao().saveInTx(likes);
        }
    }

    public void saveLike(long nearCircleId, NearCircleLike likeInfo) {
        if (nearCircleId > 0 && likeInfo != null) {
            daoSession.getNearCircleLikeDao().save(likeInfo);
        }
    }

    public List<NearCircleExt> getNearCircles(int pageCount) {
        List<NearCircle> infos = daoSession.getNearCircleDao().queryBuilder()
                .orderDesc(NearCircleDao.Properties.NearCircleId)
                .limit(pageCount)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<NearCircleExt> exts = new ArrayList<>(infos.size());
        NearCircleExt ext = null;
        for (int i = 0; i < infos.size(); i++) {
            ext = new NearCircleExt();
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getNearCircleId()));
            ext.setComments(getComments(infos.get(i).getNearCircleId()));
            ext.setLikes(getLikes(infos.get(i).getNearCircleId()));
            exts.add(ext);
        }

        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getNearCircleInfoUserIds(exts));
        setNearCircleFriendInfo(exts, friends);
        return exts;
    }

    public void updateNearCircleFriendList(List<NearCircleExt> exts, long friendId, Function func) {
        if (friendId <= 0) return;
        if (exts == null || exts.size() <= 0) return;
        List<Long> fids = new ArrayList<>();
        fids.add(friendId);
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(fids);
        setNearCircleFriendInfo(exts, friends, func);
    }

    public NearCircleExt get(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        NearCircle model = daoSession.getNearCircleDao().load(nearCircleId);
        if (model == null) return null;
        NearCircleExt info = new NearCircleExt(
                model,
                getImages(model.getNearCircleId()),
                getComments(model.getNearCircleId()),
                getLikes(model.getNearCircleId())
        );

        List<NearCircleExt> exts = new ArrayList<>();
        exts.add(info);
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getNearCircleInfoUserIds(exts));
        setNearCircleFriendInfo(exts, friends);
        return info;
    }

    private Friend getFriend(List<Friend> friends, long userId) {
        if (userId <= 0) return null;
        for (int i = 0; i < friends.size(); i++) {
            if (userId == friends.get(i).getId()) {
                return friends.get(i);
            }
        }
        return null;
    }

    private void setNearCircleFriendInfo(List<NearCircleExt> exts, List<Friend> friends) {
        setNearCircleFriendInfo(exts, friends, null);
    }

    private void setNearCircleFriendInfo(List<NearCircleExt> exts, List<Friend> friends, Function func) {
        if (exts == null || exts.size() <= 0) return;
        if (friends == null || friends.size() <= 0) return;

        for (int i = 0; i < exts.size(); i++) {
            Friend friend = getFriend(friends, exts.get(i).getInfo().getPublishUserId());
            if (friend == null) continue;
            exts.get(i).getInfo().setPublishUserName(friend.getShowName());
            exts.get(i).getInfo().setPublishUserImage(friend.getHeadImage());

            if (exts.get(i).getLikes() != null && exts.get(i).getLikes().size() > 0) {
                for (int j = 0; j < exts.get(i).getLikes().size(); j++) {
                    Friend friend2 = getFriend(friends, exts.get(i).getLikes().get(j).getLikeUserId());
                    if (friend2 == null) continue;
                    exts.get(i).getLikes().get(j).setLikeUserName(friend2.getShowName());
                    exts.get(i).getLikes().get(j).setLikeUserImage(friend2.getHeadImage());
                }
            }

            if (exts.get(i).getComments() != null && exts.get(i).getComments().size() > 0) {
                for (int j = 0; j < exts.get(i).getComments().size(); j++) {
                    Friend friend3 = getFriend(friends, exts.get(i).getComments().get(j).getCommentUserId());
                    if (friend3 != null) {
                        exts.get(i).getComments().get(j).setCommentUserName(friend3.getShowName());
                        exts.get(i).getComments().get(j).setCommentUserImage(friend3.getHeadImage());
                    }
                    Friend friend4 = getFriend(friends, exts.get(i).getComments().get(j).getReplyUserId());
                    if (friend4 != null) {
                        exts.get(i).getComments().get(j).setReplyUserName(friend4.getShowName());
                        exts.get(i).getComments().get(j).setReplyUserImage(friend4.getHeadImage());
                    }
                }
            }

            if (func != null) {
                try {
                    func.apply(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Long> getNearCircleInfoUserIds(List<NearCircleExt> exts) {
        if (exts == null || exts.size() <= 0) return null;

        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < exts.size(); i++) {
            if (!userIds.contains(exts.get(i).getInfo().getPublishUserId())) {
                userIds.add(exts.get(i).getInfo().getPublishUserId());
            }
            if (exts.get(i).getLikes() != null && exts.get(i).getLikes().size() > 0) {
                for (int j = 0; j < exts.get(i).getLikes().size(); j++) {
                    if (!userIds.contains(exts.get(i).getLikes().get(j).getLikeUserId())) {
                        userIds.add(exts.get(i).getLikes().get(j).getLikeUserId());
                    }
                }
            }
            if (exts.get(i).getComments() != null && exts.get(i).getComments().size() > 0) {
                for (int j = 0; j < exts.get(i).getComments().size(); j++) {
                    if (!userIds.contains(exts.get(i).getComments().get(j).getCommentUserId())) {
                        userIds.add(exts.get(i).getComments().get(j).getCommentUserId());
                    }
                    if (!userIds.contains(exts.get(i).getComments().get(j).getReplyUserId())) {
                        userIds.add(exts.get(i).getComments().get(j).getReplyUserId());
                    }
                }
            }
        }

        return userIds;
    }
}
