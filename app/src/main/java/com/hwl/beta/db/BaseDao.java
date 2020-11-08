package com.hwl.beta.db;

import android.util.Log;

import com.hwl.beta.db.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by adminstrator on 2018/6/16.
 */
public class BaseDao<T> {
    private static final String TAG = BaseDao.class.getSimpleName();
    public DaoSession daoSession;

    public BaseDao() {
        daoSession = DaoManager.getInstance().getDaoSession();
    }

    public boolean insertObject(T object) {
        boolean flag = false;
        try {
            flag = daoSession.insert(object) != -1 ? true : false;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return flag;
    }

    public boolean insertMultObject(final List<T> objects) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.insertOrReplace(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    public void updateObject(T object) {

        if (null == object) {
            return;
        }
        try {
            daoSession.update(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void updateMultObject(final List<T> objects, Class clss) {
        if (null == objects || objects.isEmpty()) {
            return;
        }
        try {

            daoSession.getDao(clss).updateInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.update(object);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean deleteAll(Class clss) {
        boolean flag = false;
        try {
            daoSession.deleteAll(clss);
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    public void deleteObject(T object) {
        try {
            daoSession.delete(object);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean deleteMultObject(final List<T> objects, Class clss) {
        boolean flag = false;
        if (null == objects || objects.isEmpty()) {
            return false;
        }
        try {

            daoSession.getDao(clss).deleteInTx(new Runnable() {
                @Override
                public void run() {
                    for (T object : objects) {
                        daoSession.delete(object);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            flag = false;
        }
        return flag;
    }

    public String getTablename(Class object) {
        return daoSession.getDao(object).getTablename();
    }

    public boolean isExitObject(long id, Class object) {
        QueryBuilder<T> qb = (QueryBuilder<T>) daoSession.getDao(object).queryBuilder();
//        qb.where(object.Properties.Id.eq(id));
        long length = qb.buildCount().count();
        return length > 0 ? true : false;
    }

    public T QueryById(long id, Class object) {
        return (T) daoSession.getDao(object).loadByRowId(id);
    }

    public List<T> QueryObject(Class object, String where, String... params) {
        Object obj = null;
        List<T> objects = null;
        try {
            obj = daoSession.getDao(object);
            if (null == obj) {
                return null;
            }
            objects = daoSession.getDao(object).queryRaw(where, params);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return objects;
    }

    public List<T> QueryAll(Class object) {
        List<T> objects = null;
        try {
            objects = (List<T>) daoSession.getDao(object).loadAll();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return objects;
    }
}