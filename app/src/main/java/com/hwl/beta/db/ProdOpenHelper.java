package com.hwl.beta.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hwl.beta.db.dao.ChatGroupMessageDao;
import com.hwl.beta.db.dao.ChatRecordMessageDao;
import com.hwl.beta.db.dao.ChatUserMessageDao;
import com.hwl.beta.db.dao.ChatUserSettingDao;
import com.hwl.beta.db.dao.CircleCommentDao;
import com.hwl.beta.db.dao.CircleDao;
import com.hwl.beta.db.dao.CircleImageDao;
import com.hwl.beta.db.dao.CircleLikeDao;
import com.hwl.beta.db.dao.CircleMessageDao;
import com.hwl.beta.db.dao.DaoMaster;
import com.hwl.beta.db.dao.FriendDao;
import com.hwl.beta.db.dao.FriendRequestDao;
import com.hwl.beta.db.dao.GroupInfoDao;
import com.hwl.beta.db.dao.GroupUserInfoDao;
import com.hwl.beta.db.dao.NearCircleCommentDao;
import com.hwl.beta.db.dao.NearCircleDao;
import com.hwl.beta.db.dao.NearCircleImageDao;
import com.hwl.beta.db.dao.NearCircleLikeDao;
import com.hwl.beta.db.dao.NearCircleMessageDao;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Growth on 2020/10/26.
 */
public class ProdOpenHelper extends DaoMaster.OpenHelper {
    public ProdOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }, ChatGroupMessageDao.class,
                ChatRecordMessageDao.class,
                ChatUserMessageDao.class,
                ChatUserSettingDao.class,
                CircleCommentDao.class,
                CircleDao.class,
                CircleImageDao.class,
                CircleLikeDao.class,
                CircleMessageDao.class,
                FriendDao.class,
                FriendRequestDao.class,
                GroupInfoDao.class,
                GroupUserInfoDao.class,
                NearCircleCommentDao.class,
                NearCircleDao.class,
                NearCircleImageDao.class,
                NearCircleLikeDao.class,
                NearCircleMessageDao.class);
    }
}
