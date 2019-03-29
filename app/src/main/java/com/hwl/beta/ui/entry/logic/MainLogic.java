package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.location.BaiduLocation;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserPosRequest;
import com.hwl.beta.net.user.body.SetUserPosResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.DefaultCallback;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.im.common.DefaultConsumer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.entry.standard.MainStandard;

public class MainLogic implements MainStandard {
    private BaiduLocation locationService;

	private void startLocation(ObservableEmitter emitter){
		locationService = new BaiduLocation(new BaiduLocation.OnLocationListener() {
			@Override
			public void onSuccess(BaiduLocation.ResultModel result) {
				if (UserPosSP.getLontitude() == result.lontitude && UserPosSP.getLatitude() ==
						result.latitude) {
					emitter.onComplete();
				} else {
					emitter.onNext(result);
				}
			}

			@Override
			public void onFailed(BaiduLocation.ResultInfo info) {
				emitter.onError(new Throwable(info.message));
			}
		});
		locationService.start();
	}

    @Override
    public MainBean getMainBean() {
        return new MainBean(MessageCountSP.getChatMessageCount(), MessageCountSP
                .getNearCircleMessageCount(), MessageCountSP.getFriendRequestCount(),
                MessageCountSP.getCircleMessageCount(), 0);
    }

    @Override
    public int getLocationStatus() {
        return locationService.getCurrentLocationStatus();
    }

    @Override
    public Observable<String> getLocation() {
		return Observable.create(new ObservableOnSubscribe() {
					public void subscribe(ObservableEmitter emitter) throws Exception {
						startLocation(emitter);
					}
				})
				.map(new Function<BaiduLocation.ResultModel,Observable<SetUserPosResponse>>(){
					@Override
                    public Observable<SetUserPosResponse> apply(BaiduLocation.ResultModel result) throws Exception {
                        SetUserPosRequest request = new SetUserPosRequest();
						request.setUserId(UserSP.getUserId());
						request.setLastGroupGuid(UserPosSP.getGroupGuid());
						request.setLatitude(result.latitude + "");
						request.setLongitude(result.lontitude + "");
						request.setCountry(result.country);
						request.setProvince(result.province);
						request.setCity(result.city);
						request.setDistrict(result.district);
						request.setStreet(result.street);
						request.setDetails(result.addr);
						return UserService.setUserPos(request);
                    }
				})
				.map(new Function<SetUserPosResponse,String>(){
					@Override
                    public String apply(SetUserPosResponse res) throws Exception {
						if (res.getStatus() == NetConstant.RESULT_SUCCESS) {
							UserPosSP.setUserPos(
								res.getUserPosId(), 
								res.getUserGroupGuid(), 
								result.latitude,
								result.lontitude,
								result.country,
								result.province,
								result.city,
								result.district,
								result.street,
								result.describe);

								addLocalGroupInfo(res);
                        }

						return UserPosSP.getNearDesc();
                    }
				}).subscribeOn(Schedulers.io());
    }

    private void addLocalGroupInfo(SetUserPosResponse res) {
        if (res.getGroupUserInfos() != null && res.getGroupUserInfos().size() > 0) {
            GroupInfo groupInfo = DBGroupAction.convertToNearGroupInfo(
                    res.getUserGroupGuid(),
                    res.getGroupUserInfos().size(),
                    DBGroupAction.convertToGroupUserImages(res.getGroupUserInfos()));
            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
            DaoUtils.getGroupUserInfoManagerInstance()
                    .addListAsync(DBGroupAction.convertToGroupUserInfos(res.getGroupUserInfos()));
            DaoUtils.getFriendManagerInstance().addListAsync(DBFriendAction
                    .convertGroupUserToFriendInfos(res.getGroupUserInfos()));
        }
    }
}
