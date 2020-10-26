package com.hwl.beta.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
