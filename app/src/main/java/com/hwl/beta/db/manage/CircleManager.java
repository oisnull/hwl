package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.dao.CircleCommentDao;
import com.hwl.beta.db.dao.CircleDao;
import com.hwl.beta.db.dao.CircleImageDao;
import com.hwl.beta.db.dao.CircleLikeDao;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.ext.CircleExt;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

public class CircleManager extends BaseDao<Circle> {
    public CircleManager(Context context) {
        super(context);
    }

    public Circle getCircle(long circleId) {
        if (circleId <= 0) return null;
        return daoSession.getCircleDao().load(circleId);
    }

    public boolean isExists(long circleId) {
        if (circleId <= 0) return false;
        if (daoSession.getCircleDao().load(circleId) != null)
            return true;
        return false;
    }

    public void delete(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + CircleDao.TABLENAME + " where " + CircleDao.Properties.CircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public long save(Circle model) {
        if (model == null) return 0;
        return daoSession.getCircleDao().insertOrReplace(model);
    }

    public long save(Circle model, List<CircleImage> images) {
        if (model == null) return 0;
        long id = daoSession.getCircleDao().insertOrReplace(model);
        if (id > 0 && images != null && images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                images.get(i).setCircleId(id);
            }
            daoSession.getCircleImageDao().saveInTx(images);
        }
        return id;
    }

    public List<CircleImage> getImages(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleImageDao().queryBuilder()
                .where(CircleImageDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void saveImages(long CircleId, List<CircleImage> images) {
        if (CircleId > 0 && images != null && images.size() > 0) {
            daoSession.getCircleImageDao().saveInTx(images);
        }
    }

    public void deleteImages(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleImageDao.TABLENAME + " where " + CircleImageDao.Properties.CircleId.columnName + "=" + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComments(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleCommentDao.TABLENAME + " where " + CircleCommentDao.Properties.CircleId.columnName + "=" + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComment(long circleId, long userId, int commentId) {
        if (circleId > 0) {
            String deleteSql = "delete from " + CircleCommentDao.TABLENAME + " where " +
                    CircleCommentDao.Properties.CircleId.columnName + "=" + circleId + " and " +
                    CircleCommentDao.Properties.CommentId.columnName + "=" + commentId + " and " +
                    CircleCommentDao.Properties.CommentUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public CircleComment getComment(long circleId, long userId, int commentId) {
        if (circleId <= 0) return null;
        return daoSession.getCircleCommentDao().queryBuilder()
                .where(CircleCommentDao.Properties.CircleId.eq(circleId))
                .where(CircleCommentDao.Properties.CommentUserId.eq(userId))
                .where(CircleCommentDao.Properties.CommentId.eq(commentId))
                .unique();
    }

    public List<CircleComment> getComments(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleCommentDao().queryBuilder()
                .where(CircleCommentDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void saveComment(long circleId, CircleComment comment) {
        if (circleId > 0 && comment != null) {
            daoSession.getCircleCommentDao().save(comment);
        }
    }

    public void saveComments(long CircleId, List<CircleComment> comments) {
        if (CircleId > 0 && comments != null && comments.size() > 0) {
            daoSession.getCircleCommentDao().saveInTx(comments);
        }
    }

    public CircleLike getLike(long circleId, long userId) {
        if (circleId <= 0) return null;
        return daoSession.getCircleLikeDao().queryBuilder()
                .where(CircleLikeDao.Properties.CircleId.eq(circleId))
                .where(CircleLikeDao.Properties.LikeUserId.eq(userId))
                .unique();
    }

    public List<CircleLike> getLikes(long CircleId) {
        if (CircleId <= 0) return null;
        return daoSession.getCircleLikeDao().queryBuilder()
                .where(CircleLikeDao.Properties.CircleId.eq(CircleId))
                .list();
    }

    public void deleteLikes(long CircleId) {
        if (CircleId > 0) {
            String deleteSql = "delete from " + CircleLikeDao.TABLENAME + " where " + CircleLikeDao.Properties.CircleId.columnName + " = " + CircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteLike(long circleId, long userId) {
        if (circleId > 0) {
            String deleteSql = "delete from " + CircleLikeDao.TABLENAME + " where " +
                    CircleLikeDao.Properties.CircleId.columnName + " = " + circleId + " and " +
                    CircleLikeDao.Properties.LikeUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void saveLikes(long CircleId, List<CircleLike> likes) {
        if (CircleId > 0 && likes != null && likes.size() > 0) {
            daoSession.getCircleLikeDao().saveInTx(likes);
        }
    }

    public void saveLike(long circleId, CircleLike likeInfo) {
        if (circleId > 0 && likeInfo != null) {
            daoSession.getCircleLikeDao().save(likeInfo);
        }
    }

    public List<CircleExt> getCircles(int pageCount) {
        List<Circle> infos = daoSession.getCircleDao().queryBuilder()
                .orderDesc(CircleDao.Properties.CircleId)
                .limit(pageCount)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<CircleExt> exts = new ArrayList<>(infos.size());
        CircleExt ext;
        for (int i = 0; i < infos.size(); i++) {
            ext = new CircleExt(CircleExt.CircleIndexItem);
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getCircleId()));
            ext.setComments(getComments(infos.get(i).getCircleId()));
            ext.setLikes(getLikes(infos.get(i).getCircleId()));
            exts.add(ext);
        }

        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getCircleInfoUserIds(exts));
        setCircleFriendInfo(exts, friends);
        return exts;
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

    private void setCircleFriendInfo(List<CircleExt> exts, List<Friend> friends) {
        setCircleFriendInfo(exts, friends, null);
    }

    private void setCircleFriendInfo(List<CircleExt> exts, List<Friend> friends, Function func) {
        if (exts == null || exts.size() <= 0) return;
        if (friends == null || friends.size() <= 0) return;

        for (int i = 0; i < exts.size(); i++) {
            if (exts.get(i).getInfo() == null) continue;
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

    private List<Long> getCircleInfoUserIds(List<CircleExt> exts) {
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

    public List<CircleExt> getUserCircles(long userId) {
        List<Circle> infos = daoSession.getCircleDao().queryBuilder()
                .where(CircleDao.Properties.PublishUserId.eq(userId))
                .orderDesc(CircleDao.Properties.CircleId)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<CircleExt> exts = new ArrayList<>(infos.size());
        CircleExt ext;
        for (int i = 0; i < infos.size(); i++) {
            ext = new CircleExt(CircleExt.CircleIndexItem);
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getCircleId()));
            ext.setComments(getComments(infos.get(i).getCircleId()));
            ext.setLikes(getLikes(infos.get(i).getCircleId()));
            exts.add(ext);
        }
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getCircleInfoUserIds(exts));
        setCircleFriendInfo(exts, friends);
        return exts;
    }

    public CircleExt get(long CircleId) {
        if (CircleId <= 0) return null;
        Circle model = daoSession.getCircleDao().load(CircleId);
        if (model == null) return null;
        CircleExt info = new CircleExt(
                model,
                getImages(model.getCircleId()),
                getComments(model.getCircleId()),
                getLikes(model.getCircleId())
        );

        List<CircleExt> exts = new ArrayList<>();
        exts.add(info);
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(getCircleInfoUserIds(exts));
        setCircleFriendInfo(exts, friends);
        return info;
    }

    public List<CircleExt> getTop3Circles(long userId) {
        if (userId <= 0) return null;
        List<Circle> infos = daoSession.getCircleDao().queryBuilder()
                .where(CircleDao.Properties.PublishUserId.eq(userId))
                .orderDesc(CircleDao.Properties.CircleId)
                .limit(3)
                .list();
        if (infos == null || infos.size() <= 0) return null;
        List<CircleExt> exts = new ArrayList<>(infos.size());
        CircleExt ext;
        for (int i = 0; i < infos.size(); i++) {
            ext = new CircleExt(CircleExt.CircleIndexItem);
            ext.setInfo(infos.get(i));
            ext.setImages(getImages(infos.get(i).getCircleId()));
            exts.add(ext);
        }
        return exts;
    }

    public void updateCircleFriendList(List<CircleExt> exts, long friendId, Function func) {
        if (friendId <= 0) return;
        if (exts == null || exts.size() <= 0) return;
        List<Long> fids = new ArrayList<>();
        fids.add(friendId);
        List<Friend> friends = DaoUtils.getFriendManagerInstance().getList(fids);
        setCircleFriendInfo(exts, friends, func);
    }
}