package com.hwl.beta.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hwl.beta.db.entity.NearCircleLike;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEAR_CIRCLE_LIKE".
*/
public class NearCircleLikeDao extends AbstractDao<NearCircleLike, Void> {

    public static final String TABLENAME = "NEAR_CIRCLE_LIKE";

    /**
     * Properties of entity NearCircleLike.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property NearCircleId = new Property(0, long.class, "nearCircleId", false, "NEAR_CIRCLE_ID");
        public final static Property LikeUserId = new Property(1, long.class, "likeUserId", false, "LIKE_USER_ID");
        public final static Property LikeUserName = new Property(2, String.class, "likeUserName", false, "LIKE_USER_NAME");
        public final static Property LikeUserImage = new Property(3, String.class, "likeUserImage", false, "LIKE_USER_IMAGE");
        public final static Property LikeTime = new Property(4, java.util.Date.class, "likeTime", false, "LIKE_TIME");
    }


    public NearCircleLikeDao(DaoConfig config) {
        super(config);
    }
    
    public NearCircleLikeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEAR_CIRCLE_LIKE\" (" + //
                "\"NEAR_CIRCLE_ID\" INTEGER NOT NULL ," + // 0: nearCircleId
                "\"LIKE_USER_ID\" INTEGER NOT NULL ," + // 1: likeUserId
                "\"LIKE_USER_NAME\" TEXT," + // 2: likeUserName
                "\"LIKE_USER_IMAGE\" TEXT," + // 3: likeUserImage
                "\"LIKE_TIME\" INTEGER);"); // 4: likeTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEAR_CIRCLE_LIKE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NearCircleLike entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getNearCircleId());
        stmt.bindLong(2, entity.getLikeUserId());
 
        String likeUserName = entity.getLikeUserName();
        if (likeUserName != null) {
            stmt.bindString(3, likeUserName);
        }
 
        String likeUserImage = entity.getLikeUserImage();
        if (likeUserImage != null) {
            stmt.bindString(4, likeUserImage);
        }
 
        java.util.Date likeTime = entity.getLikeTime();
        if (likeTime != null) {
            stmt.bindLong(5, likeTime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NearCircleLike entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getNearCircleId());
        stmt.bindLong(2, entity.getLikeUserId());
 
        String likeUserName = entity.getLikeUserName();
        if (likeUserName != null) {
            stmt.bindString(3, likeUserName);
        }
 
        String likeUserImage = entity.getLikeUserImage();
        if (likeUserImage != null) {
            stmt.bindString(4, likeUserImage);
        }
 
        java.util.Date likeTime = entity.getLikeTime();
        if (likeTime != null) {
            stmt.bindLong(5, likeTime.getTime());
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public NearCircleLike readEntity(Cursor cursor, int offset) {
        NearCircleLike entity = new NearCircleLike( //
            cursor.getLong(offset + 0), // nearCircleId
            cursor.getLong(offset + 1), // likeUserId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // likeUserName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // likeUserImage
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)) // likeTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, NearCircleLike entity, int offset) {
        entity.setNearCircleId(cursor.getLong(offset + 0));
        entity.setLikeUserId(cursor.getLong(offset + 1));
        entity.setLikeUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setLikeUserImage(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLikeTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(NearCircleLike entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(NearCircleLike entity) {
        return null;
    }

    @Override
    public boolean hasKey(NearCircleLike entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
