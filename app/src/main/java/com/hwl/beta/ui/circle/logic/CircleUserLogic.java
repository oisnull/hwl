package com.hwl.beta.ui.circle.logic;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.NetCircleMatchInfo;
import com.hwl.beta.net.circle.body.DeleteCircleInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.standard.CircleStandard;
import com.hwl.beta.ui.convert.DBCircleAction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CircleUserLogic implements CircleUserStandard {

    final static int PAGE_COUNT = 15;
    final static int COMMENT_PAGE_COUNT = 10;
	Friend currentUser;

	public CircleUserLogic(long viewUserId,String viewUserName,String viewUserImage){
		if (viewUserId<=0||viewUserId == UserSP.getUserId()){
			currentUser = DBFriendAction.convertToFriendInfo(UserSP.getUserInfo())
		}else{
			currentUser = DaoUtils.getFriendManagerInstance().get(viewUserId);
		}
		if(currentUser==null){
			currentUser=new Friend();
			currentUser.setId(viewUserId);
			currentUser.setName(viewUserName);
			currentUser.setHeadImage(viewUserImage);
		}
	}

    @Override
    public Observable<List<Circle>> loadLocalInfos() {
        return Observable.fromCallable(new Callable<List<Circle>>() {
            @Override
            public List<Circle> call() {
                List<Circle> circles = DaoUtils.getCircleManagerInstance().getUserCirclesV2(currentUser.getUserId());
                if (circles == null)
                    circles = new ArrayList<>();

                return circles;
            }
        })
                .doOnNext(new Consumer<List<Circle>>() {
                    @Override
                    public void accept(List<Circle> circles) throws Exception {
                        if (circles.size() <= 0) {
                            circles.add(new Circle(DBConstant.CIRCLE_ITEM_NULL));
                        }
                        circles.add(0, new Circle(DBConstant.CIRCLE_ITEM_HEAD,
														currentUser.getUserId(),
														currentUser.getShowName(),
														currentUser.getHeadImage().
														currentUser.getCircleBackImage(),
														currentUser.getLifeNotes()));
                    }
                })
                .subscribeOn(Schedulers.io());
    }

	 @Override
    public Observable<List<Circle>> loadServerInfos(final long minCircleId,
                                                    final List<Circle> localInfos) {
        // minCircleId <=0 and get new data
        // minCircleId >0 and get old data
        return CircleService.getUserCircleInfos(currentUser.getUserId(), minCircleId, PAGE_COUNT,
                this.getMatchInfos(minCircleId, localInfos))
                .map(new Function<GetUserCircleInfosResponse, List<Circle>>() {
                    @Override
                    public List<Circle> apply(GetUserCircleInfosResponse response) {
                        List<Circle> infos = DBCircleAction.convertToCircleInfos(response.getCircleInfos());
                        if (infos == null) infos = new ArrayList<>();
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

    private List<NetCircleMatchInfo> getMatchInfos(long minCircleId, List<Circle> localInfos) {
        List<NetCircleMatchInfo> matchInfos = new ArrayList<>();
        for (int i = 0; i < localInfos.size(); i++) {
            if (minCircleId <= 0) {
                if (localInfos.get(i).getCircleId() <= 0) continue;
            } else {
                if (localInfos.get(i).getCircleId() >= minCircleId) continue;
            }

            matchInfos.add(new NetCircleMatchInfo(localInfos.get(i).getCircleId(),
                    localInfos.get(i).getUpdateTime()));
            if (matchInfos.size() >= PAGE_COUNT) break;
        }
        return matchInfos;
    }

    @Override
    public Observable deleteInfo(final long circleId) {
        return CircleService.deleteCircleInfo(circleId)
                .map(new Function<DeleteCircleInfoResponse, Boolean>() {
                    @Override
                    public Boolean apply(DeleteCircleInfoResponse response) throws Exception {
                        if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                            DaoUtils.getCircleManagerInstance().deleteAll(circleId);
                        } else {
                            throw new Exception("Delete circle info failed.");
                        }
                        return true;
                    }
                });
    }

    @Override
    public Friend getLocalUserInfo(){
		return currentUser;
	}

    @Override
    public Observable loadServerUserInfo(){
		if (currentUser.getUserId() == UserSP.getUserId()) return Observable.empty();

       return UserService.getUserDetails(currentUser.getUserId())
                .map(new Function<GetUserDetailsResponse, Friend>() {
                    @Override
                    public Friend apply(GetUserDetailsResponse response) throws Exception {
                        if (response.getUserDetailsInfo() != null) {
                            Friend newInfo = DBFriendAction.convertToFriendInfo(response.getUserDetailsInfo());
                            DaoUtils.getFriendManagerInstance().save(newInfo);
							return newInfo;
                        }
						return null;
                    }
                });
	}
}
