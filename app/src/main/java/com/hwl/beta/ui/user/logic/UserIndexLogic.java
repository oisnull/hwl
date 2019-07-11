package com.hwl.beta.ui.user.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.net.user.UserDetailsInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.DeleteFriendResponse;
import com.hwl.beta.net.user.body.GetUserDetailsResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.common.rxext.RXDefaultObserver;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.ebus.EventBusUtil;
import com.hwl.beta.ui.user.bean.UserIndexBean;
import com.hwl.beta.ui.user.standard.UserIndexStandard;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class UserIndexLogic implements UserIndexStandard {

    final static int CIRCLE_PAGE_COUNT = 5;
	
    @Override
    public UserIndexBean getUserInfo(long userId, String userName, String userImage) {
        if (userId <= 0) return null;
        UserIndexBean userBean = new UserIndexBean();
        if (userId == UserSP.getUserId()) {
            NetUserInfo netUserInfo = UserSP.getUserInfo();
            userBean.setMe(true);
            userBean.setUserId(netUserInfo.getId());
            userBean.setUserImage(netUserInfo.getHeadImage());
            userBean.setSymbol(netUserInfo.getSymbol());
            userBean.setUserName(netUserInfo.getName());
            userBean.setUserLifeNotes(netUserInfo.getLifeNotes());
            return userBean;
        }

        Friend friend = DaoUtils.getFriendManagerInstance().get(userId);
        if (friend == null) {
            friend = DBFriendAction.convertToFriendInfo(userId, userName, userImage);
        }
        userBean.setFriend(friend.getIsFriend());
        userBean.setUserId(friend.getId());
        userBean.setUserName(friend.getName());
        userBean.setUserImage(friend.getHeadImage());
        userBean.setRemark(friend.getRemark());
        userBean.setSymbol(friend.getSymbol());
        userBean.setUserLifeNotes(friend.getLifeNotes());
        userBean.setUpdateTime(friend.getUpdateTime());
        return userBean;
    }

    @Override
    public Observable<Friend> loadServerUserInfo(long userId, final String updateTime) {
        if (userId == UserSP.getUserId()){
			return Observable.empty();
		}

       return UserService.getUserDetails(userId)
				.filter(new Predicate<GetUserDetailsResponse>() {
					@Override
					public boolean test(GetUserDetailsResponse response) {
						return response.getUserDetailsInfo() != null;
					}
				})
                .map(new Function<GetUserDetailsResponse, Friend>() {
                    @Override
                    public Friend apply(GetUserDetailsResponse response) {
                        Friend newInfo = DBFriendAction.convertToFriendInfo(response.getUserDetailsInfo());
						if(TextUtils.isEmpty(updateTime)||!updateTime.equals(response.getUserDetailsInfo().getUpdateTime())){
                            DaoUtils.getFriendManagerInstance().save(newInfo);
                        }
						return newInfo;
                    }
                });
    }

    @Override
    public Observable deleteFriend(final long userId) {
        return UserService.deleteFriend(userId)
                .map(new Function<DeleteFriendResponse, Boolean>() {
                    @Override
                    public Boolean apply(DeleteFriendResponse response) {
                        if (response.getStatus() == NetConstant.RESULT_FAILED) {
							throw new Exception("删除失败");
                        } 

                        DaoUtils.getFriendManagerInstance().deleteFriend(userId);
                        UserSP.deleteOneFriendCount();
                        EventBusUtil.sendFriendDeleteEvent(userId);
                        return true;
                    }
                });
    }

    @Override
    public Observable<List<Circle>> loadLocalCircleInfos(final long userId) {
        return Observable.fromCallable(new Callable<List<Circle>>() {
            @Override
            public List<Circle> call() {
                List<Circle> circles =
                        DaoUtils.getCircleManagerInstance().getUserCircles(userId);
                if (circles == null)
                    circles = new ArrayList<>();

                return circles;
            }
        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Circle>> loadServerCircleInfos(long userId,final List<Circle> localInfos) {
        return CircleService.getUserCircleInfos(userId, 0, CIRCLE_PAGE_COUNT,this.getMatchInfos(localInfos))
                .map(new Function<GetUserCircleInfosResponse, List<Circle>>() {
                    @Override
                    public List<Circle> apply(GetUserCircleInfosResponse response) {
                        List<Circle> infos = DBCircleAction.convertToCircleInfos(response.getCircleInfos());
                        if (infos == null) return new ArrayList<>();
                        return infos;
                    }
                })
                .doOnNext(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> infos) {
                        DaoUtils.getCircleManagerInstance().deleteAll(infos);
                        DaoUtils.getCircleManagerInstance().saveAll(infos);
                    }
                });
    }

    private List<NetCircleMatchInfo> getMatchInfos(List<Circle> localInfos) {
		if(localInfos==null||localInfos.size()<=0) return null;

        List<NetCircleMatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < localInfos.size(); i++) {
            if (TextUtils.isEmpty(localInfos.get(i).getUpdateTime())) continue;
            matchInfos.add(new NetCircleMatchInfo(localInfos.get(i).getCircleId(),localInfos.get(i).getUpdateTime()));
        }
        return matchInfos;
    }
}
