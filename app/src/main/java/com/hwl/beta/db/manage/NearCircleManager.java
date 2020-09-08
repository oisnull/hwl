package com.hwl.beta.db.manage;

import android.database.Cursor;

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
import com.hwl.beta.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Created by Administrator on 2018/3/10.
 */

public class NearCircleManager extends BaseDao<NearCircle> {

    public long save(NearCircle info) {
        if (info == null || info.getNearCircleId() <= 0) return 0;

        if (info.getImages() != null && info.getImages().size() > 0)
            saveImages(info.getImages());

        if (info.getComments() != null && info.getComments().size() > 0)
            saveComments(info.getComments());

        if (info.getLikes() != null && info.getLikes().size() > 0)
            saveLikes(info.getLikes());

        return daoSession.getNearCircleDao().insertOrReplace(info);
    }

    public NearCircle getNearCircle(long nearCircleId) {
        return getNearCircle(nearCircleId, 0);
    }

    public NearCircle getNearCircle(long nearCircleId, int commentPageCount) {
        if (nearCircleId <= 0) return null;
        NearCircle info = daoSession.getNearCircleDao().load(nearCircleId);
        if (info == null) return null;

        if (commentPageCount <= 0) return info;

        info.setImages(getImages(nearCircleId));
        info.setComments(getComments(nearCircleId, commentPageCount));
        info.setLikes(getLikes(nearCircleId));

        return info;
    }

    public String getLastUpdateTime(long nearCircleId) {
        if (nearCircleId <= 0) return null;

        String sql =
                "select " + NearCircleDao.Properties.UpdateTime.columnName + " from " + NearCircleDao.TABLENAME + " where " + NearCircleDao.Properties.NearCircleId.columnName + " = " + nearCircleId;

        Cursor cursor = daoSession.getDatabase().rawQuery(sql, null);
        if (cursor != null) {
            return cursor.getString(0);
        }
        return null;
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

    public void saveAll(List<NearCircle> infos) {
        if (infos == null || infos.size() <= 0) return;
        for (NearCircle info : infos) {
            save(info);
//            saveImages(info.getImages());
//            saveComments(info.getComments());
//            saveLikes(info.getLikes());
        }
    }

    public void clearAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(NearCircleDao.TABLENAME);
        sb.append(";");
        sb.append("DELETE FROM ");
        sb.append(NearCircleImageDao.TABLENAME);
        sb.append(";");
        sb.append("DELETE FROM ");
        sb.append(NearCircleCommentDao.TABLENAME);
        sb.append(";");
        sb.append("DELETE FROM ");
        sb.append(NearCircleLikeDao.TABLENAME);
        sb.append(";");

        daoSession.getDatabase().execSQL(sb.toString());
    }

    public void delete(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql =
                    "delete from " + NearCircleDao.TABLENAME + " where " + NearCircleDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteAll(List<NearCircle> infos) {
        if (infos == null || infos.size() <= 0) return;
        for (NearCircle info : infos) {
            deleteAll(info.getNearCircleId());
        }
    }

    public void deleteAll(long nearCircleId) {
        if (nearCircleId > 0) {
            StringBuilder sb = new StringBuilder();
            //delete info sql
            sb.append("delete from ");
            sb.append(NearCircleDao.TABLENAME);
            sb.append(" where ");
            sb.append(NearCircleDao.Properties.NearCircleId.columnName);
            sb.append(" = ");
            sb.append(nearCircleId);
            sb.append(";");
            daoSession.getDatabase().execSQL(sb.toString());

            sb = new StringBuilder();
            //delete images sql
            sb.append("delete from ");
            sb.append(NearCircleImageDao.TABLENAME);
            sb.append(" where ");
            sb.append(NearCircleImageDao.Properties.NearCircleId.columnName);
            sb.append(" = ");
            sb.append(nearCircleId);
            sb.append(";");
            daoSession.getDatabase().execSQL(sb.toString());

            sb = new StringBuilder();
            //delete comments sql
            sb.append("delete from ");
            sb.append(NearCircleCommentDao.TABLENAME);
            sb.append(" where ");
            sb.append(NearCircleCommentDao.Properties.NearCircleId.columnName);
            sb.append(" = ");
            sb.append(nearCircleId);
            sb.append(";");
            daoSession.getDatabase().execSQL(sb.toString());

            sb = new StringBuilder();
            //delete likes sql
            sb.append("delete from ");
            sb.append(NearCircleLikeDao.TABLENAME);
            sb.append(" where ");
            sb.append(NearCircleLikeDao.Properties.NearCircleId.columnName);
            sb.append(" = ");
            sb.append(nearCircleId);
            sb.append(";");

            daoSession.getDatabase().execSQL(sb.toString());
        }
    }

    public List<NearCircleImage> getImages(long nearCircleId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleImageDao().queryBuilder()
                .where(NearCircleImageDao.Properties.NearCircleId.eq(nearCircleId))
                .list();
    }

    public void saveImages(List<NearCircleImage> images) {
        if (images != null && images.size() > 0) {
            daoSession.getNearCircleImageDao().saveInTx(images);
        }
    }

    public void deleteImages(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql =
                    "delete from " + NearCircleImageDao.TABLENAME + " where " + NearCircleImageDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComment(long nearCircleId, long userId, long commentId) {
        if (nearCircleId > 0) {
            String deleteSql = "delete from " + NearCircleCommentDao.TABLENAME + " where " +
                    NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId + " and " +
                    NearCircleCommentDao.Properties.CommentId.columnName + "=" + commentId + " " +
                    "and " +
                    NearCircleCommentDao.Properties.CommentUserId.columnName + " = " + userId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public void deleteComments(long nearCircleId) {
        if (nearCircleId > 0) {
            String deleteSql =
                    "delete from " + NearCircleCommentDao.TABLENAME + " where " + NearCircleCommentDao.Properties.NearCircleId.columnName + "=" + nearCircleId;
            daoSession.getDatabase().execSQL(deleteSql);
        }
    }

    public NearCircleComment getComment(long nearCircleId, long userId, long commentId) {
        if (nearCircleId <= 0) return null;
        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .where(NearCircleCommentDao.Properties.CommentUserId.eq(userId))
                .where(NearCircleCommentDao.Properties.CommentId.eq(commentId))
                .unique();
    }

    public List<NearCircleComment> getComments(long nearCircleId) {
        return getComments(nearCircleId, 0);
    }

    public List<NearCircleComment> getComments(long nearCircleId, int pageCount) {
        if (nearCircleId <= 0) return null;

        if (pageCount <= 0) {
            return daoSession.getNearCircleCommentDao().queryBuilder()
                    .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                    .list();
        }

        return daoSession.getNearCircleCommentDao().queryBuilder()
                .where(NearCircleCommentDao.Properties.NearCircleId.eq(nearCircleId))
                .limit(pageCount)
                .list();
    }

    public void saveComment(NearCircleComment comment) {
        if (comment == null) return;
        daoSession.getNearCircleCommentDao().save(comment);
    }

    public void saveComments(List<NearCircleComment> comments) {
        if (comments == null || comments.size() <= 0) return;

        daoSession.getNearCircleCommentDao().saveInTx(comments);
    }

    public void saveNonExistentComments(List<NearCircleComment> comments) {
        if (comments == null || comments.size() <= 0) return;

        List<NearCircleComment> nonExistentComments = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            NearCircleComment com = getComment(comments.get(i).getNearCircleId(),
                    comments.get(i).getCommentUserId(), comments.get(i).getCommentId());
            if (com == null) {
                nonExistentComments.add(comments.get(i));
            }
        }
        daoSession.getNearCircleCommentDao().saveInTx(nonExistentComments);
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
            String deleteSql =
                    "delete from " + NearCircleLikeDao.TABLENAME + " where " + NearCircleLikeDao.Properties.NearCircleId.columnName + " = " + nearCircleId;
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

    public void saveLikes(List<NearCircleLike> likes) {
        if (likes != null && likes.size() > 0) {
            daoSession.getNearCircleLikeDao().saveInTx(likes);
        }
    }

    public void saveLike(NearCircleLike likeInfo) {
        if (likeInfo != null && likeInfo.getNearCircleId() > 0 && likeInfo.getLikeUserId() > 0) {
            daoSession.getNearCircleLikeDao().save(likeInfo);
        }
    }

    public List<NearCircle> getNearCirclesV2(int pageCount, int commentPageCount) {
        List<NearCircle> infos = daoSession.getNearCircleDao().queryBuilder()
                .orderDesc(NearCircleDao.Properties.NearCircleId)
                .limit(pageCount)
                .list();
        if (infos == null || infos.size() <= 0) return infos;

        long circleId = 0;
        for (int i = 0; i < infos.size(); i++) {
            circleId = infos.get(i).getNearCircleId();

            infos.get(i).setImages(getImages(circleId));
            infos.get(i).setComments(getComments(circleId, commentPageCount));
            infos.get(i).setLikes(getLikes(circleId));
        }

        return infos;
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

        List<Friend> friends =
                DaoUtils.getFriendManagerInstance().getList(getNearCircleInfoUserIds(exts));
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
        List<Friend> friends =
                DaoUtils.getFriendManagerInstance().getList(getNearCircleInfoUserIds(exts));
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

    private void setNearCircleFriendInfo(List<NearCircleExt> exts, List<Friend> friends,
                                         Function func) {
        if (exts == null || exts.size() <= 0) return;
        if (friends == null || friends.size() <= 0) return;

        for (int i = 0; i < exts.size(); i++) {
            Friend friend = getFriend(friends, exts.get(i).getInfo().getPublishUserId());
            if (friend == null) continue;
            exts.get(i).getInfo().setPublishUserName(friend.getShowName());
            exts.get(i).getInfo().setPublishUserImage(friend.getHeadImage());

            if (exts.get(i).getLikes() != null && exts.get(i).getLikes().size() > 0) {
                for (int j = 0; j < exts.get(i).getLikes().size(); j++) {
                    Friend friend2 = getFriend(friends,
                            exts.get(i).getLikes().get(j).getLikeUserId());
                    if (friend2 == null) continue;
                    exts.get(i).getLikes().get(j).setLikeUserName(friend2.getShowName());
                    exts.get(i).getLikes().get(j).setLikeUserImage(friend2.getHeadImage());
                }
            }

            if (exts.get(i).getComments() != null && exts.get(i).getComments().size() > 0) {
                for (int j = 0; j < exts.get(i).getComments().size(); j++) {
                    Friend friend3 = getFriend(friends,
                            exts.get(i).getComments().get(j).getCommentUserId());
                    if (friend3 != null) {
                        exts.get(i).getComments().get(j).setCommentUserName(friend3.getShowName());
                        exts.get(i).getComments().get(j).setCommentUserImage(friend3.getHeadImage());
                    }
                    Friend friend4 = getFriend(friends,
                            exts.get(i).getComments().get(j).getReplyUserId());
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

    public void setUpdateTime(long nearCircleId, String updateTime) {
        if (nearCircleId <= 0 || StringUtils.isBlank(updateTime)) return;
        String updateSql = String.format("update %s set %s='%s' where %s=%d",
                NearCircleDao.TABLENAME, NearCircleDao.Properties.UpdateTime.columnName,
                updateTime, NearCircleDao.Properties.NearCircleId.columnName, nearCircleId);
        daoSession.getDatabase().execSQL(updateSql);
    }
}
