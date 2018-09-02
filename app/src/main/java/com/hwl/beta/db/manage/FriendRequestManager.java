package com.hwl.beta.db.manage;

import android.content.Context;

import com.hwl.beta.db.BaseDao;
import com.hwl.beta.db.entity.FriendRequest;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class FriendRequestManager extends BaseDao<FriendRequest> {
    public FriendRequestManager(Context context) {
        super(context);
    }

    public void save(FriendRequest request) {
        if (request == null || request.getFriendId() <= 0) return;

        daoSession.getFriendRequestDao().insertOrReplace(request);
    }

    public FriendRequest get(long friendId) {
        if (friendId <= 0) return null;
        return daoSession.getFriendRequestDao().loadByRowId(friendId);
    }

    public boolean isExists(long friendId){
        FriendRequest request= get(friendId);
        if(request!=null){
            return true;
        }
        return false;
    }

    public List<FriendRequest> getAll() {
        return daoSession.getFriendRequestDao().loadAll();
    }

    public boolean delete(FriendRequest request) {
        if (request != null) {
            daoSession.getFriendRequestDao().delete(request);
            return true;
        }
        return false;
    }

    public void deleteAll() {
        daoSession.deleteAll(FriendRequest.class);

    }
}
