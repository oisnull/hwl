package com.hwl.beta.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hwl.beta.badge.BuildConfig;
import com.hwl.beta.db.dao.DaoMaster;
import com.hwl.beta.db.dao.DaoSession;
import com.hwl.beta.sp.UserSP;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by jamy on 2016/6/16.
 * 进行数据库的管理
 * 1.创建数据库
 * 2.创建数据库表
 * 3.对数据库进行增删查改
 * 4.对数据库进行升级
 */
public class DaoManager {
    private static final String TAG = DaoManager.class.getSimpleName();
    //private static final String DB_NAME = "hwl.db";//数据库名称
    private static DaoManager mDaoManager;//多线程访问
    private static DaoMaster.OpenHelper mHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private static SQLiteDatabase db;
    private Context context;

    private static String getDBName() {
        return "hwl-" + UserSP.getUserId() + ".db";
    }

    /**
     * 使用单例模式获得操作数据库的对象
     *
     * @return
     */
    public static DaoManager getInstance() {
        if (mDaoManager == null) {
            synchronized (DaoManager.class) {
                Log.d(TAG, "create dao manager instance ..." + getDBName());
                mDaoManager = new DaoManager();
            }
        }
        return mDaoManager;
    }

    /**
     * 初始化Context对象
     *
     * @param context
     */
    public void init(Context context) {
        this.context = context;
    }

    /**
     * 判断数据库是否存在，如果不存在则创建
     *
     * @return
     */
    public DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            Log.d(TAG, "getDaoMaster=" + getDBName());
//            if (BuildConfig.DEBUG) {
//                mHelper = new DaoMaster.DevOpenHelper(context, getDBName(), null);
//            } else {
            DaoLog.isLoggable(DaoLog.ERROR);
            mHelper = new DaoMaster.DevOpenHelper(context, getDBName());
//            }
            db = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
        }
        return mDaoMaster;
    }

    /**
     * 完成对数据库的增删查找
     *
     * @return
     */
    public DaoSession getDaoSession() {
        if (null == mDaoSession) {
            mDaoSession = getDaoMaster().newSession();
        }
        return mDaoSession;
    }

    /**
     * 设置debug模式开启或关闭，默认关闭
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        if (mDaoMaster != null) {
            mDaoMaster = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        mDaoManager = null;
    }

//    public void closeDaoSession() {
//        if (null != mDaoSession) {
//            mDaoSession.clear();
//            mDaoSession = null;
//        }
//    }
//
//    public static void closeHelper() {
//        if (mHelper != null) {
//            mHelper.close();
//            mHelper = null;
//        }
//    }
}