package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.location.LocationModel;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.NetSecretUserInfo;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserPosRequest;
import com.hwl.beta.net.user.body.SetUserPosResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.entry.standard.MainStandard;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainLogic implements MainStandard {

    @Override
    public MainBean getMainBean() {
        return new MainBean(MessageCountSP.getChatMessageCount(), MessageCountSP
                .getNearCircleMessageCount(), MessageCountSP.getFriendRequestCount(),
                MessageCountSP.getCircleMessageCount(), 0);
    }

    @Override
    public Observable<String> setLocation(final LocationModel result) {
        if (result == null) {
            return Observable.error(new Throwable("Location data con't be empty."));
        }

        SetUserPosRequest request = new SetUserPosRequest();
        request.setUserId(UserSP.getUserId());
        request.setLastGroupGuid(UserPosSP.getGroupGuid());
        request.setLatitude(result.latitude);
        request.setLongitude(result.longitude);
        request.setCountry(result.country);
        request.setProvince(result.province);
        request.setCity(result.city);
        request.setDistrict(result.district);
        request.setStreet(result.street);
        request.setDetails(result.addr);

        return UserService.setUserPos(request)
                .doOnNext(new Consumer<SetUserPosResponse>() {
                    @Override
                    public void accept(SetUserPosResponse res) throws Exception {
                        if (res.getStatus() != NetConstant.RESULT_SUCCESS) {
                            throw new Exception("Set user position failed.");
                        }

                        if (res.getGroupUserInfos() == null || res.getGroupUserInfos().size() <= 0) {
                            //add current user info
                            List<NetSecretUserInfo> userInfos = new ArrayList<>(1);
                            NetSecretUserInfo userInfo = new NetSecretUserInfo();
                            userInfo.setUserId(UserSP.getUserId());
                            userInfo.setUserName(UserSP.getUserName());
                            userInfo.setUserImage(UserSP.getUserHeadImage());
                            userInfos.add(userInfo);
                            res.setGroupUserInfos(userInfos);
                        }

                        GroupInfo groupInfo = DBGroupAction.convertToNearGroupInfo(
                                res.getUserGroupGuid(),
                                res.getGroupUserInfos().size(),
                                DBGroupAction.getGroupUserImages(res.getGroupUserInfos()), true);
                        List<GroupUserInfo> groupUserInfos =
                                DBGroupAction.convertToGroupUserInfos2(res.getUserGroupGuid(),
                                        res.getGroupUserInfos());
                        DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
                        DaoUtils.getGroupUserInfoManagerInstance().addList(groupUserInfos);
                        DaoUtils.getFriendManagerInstance().addListAsync(DBFriendAction
                                .convertSecretUserToFriendInfos(res.getGroupUserInfos()));
                    }
                })
                .map(new Function<SetUserPosResponse, String>() {
                    @Override
                    public String apply(SetUserPosResponse res) {

                        UserPosSP.setUserPos(
                                res.getUserPosId(),
                                res.getUserGroupGuid(),
                                result.latitude,
                                result.longitude,
                                result.country,
                                result.province,
                                result.city,
                                result.district,
                                result.street,
                                result.describe);

                        return UserPosSP.getNearDesc();
                    }
                });
    }
}
