package com.hwl.beta.ui.entry.logic;

import com.hwl.beta.BuildConfig;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.location.LocationModel;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.user.UserService;
import com.hwl.beta.net.user.body.SetUserPosRequest;
import com.hwl.beta.net.user.body.SetUserPosResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.CustLog;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.entry.standard.MainStandard;

import java.util.List;

import io.reactivex.Observable;

public class MainLogic implements MainStandard {

    @Override
    public MainBean getMainBean() {
        return new MainBean(MessageCountSP.getChatMessageCount(),
                MessageCountSP.getNearCircleMessageCount(),
                MessageCountSP.getFriendRequestCount(),
                MessageCountSP.getCircleMessageCount(),
                MessageCountSP.getAppVersionCount());
    }

    private void setUsersToDB(SetUserPosResponse response) {
        List<String> userImages = DBGroupAction.getGroupUserImages(response.getGroupUserInfos());
        GroupInfo groupInfo = DBGroupAction.convertToNearGroupInfo(
                response.getUserGroupGuid(),
                UserPosSP.getNearDesc(),
                response.getGroupUserInfos().size(),
                userImages,
                true);
        DaoUtils.getGroupInfoManagerInstance().add(groupInfo);

        List<GroupUserInfo> groupUsers = DBGroupAction.convertToNearGroupUsers(
                response.getUserGroupGuid(),
                response.getGroupUserInfos());
        DaoUtils.getGroupUserInfoManagerInstance().deleteGroupUserInfo(response.getUserGroupGuid());
        DaoUtils.getGroupUserInfoManagerInstance().addGroupUsers(response.getUserGroupGuid(),
                groupUsers);

        List<Friend> friends = DBFriendAction.convertToFriends(response.getGroupUserInfos(), false);
        DaoUtils.getFriendManagerInstance().addFriends(friends);
    }

    @Override
    public Observable<String> setLocation(final LocationModel result) {
        if (result == null) {
            return Observable.error(new Throwable("Location data con't be empty."));
        }

        SetUserPosRequest request = new SetUserPosRequest();
        request.setDistance(BuildConfig.DEBUG);
        request.setUserId(UserSP.getUserId());
        request.setLastGroupGuid(UserPosSP.getGroupGuid());
        request.setLatitude(result.latitude);
        request.setLongitude(result.longitude);
        request.setCountry(result.country);
        request.setProvince(result.province);
        request.setCity(result.city);
        request.setDistrict(result.district);
        request.setStreet(result.street);
        request.setTown(result.town);
        request.setDetails(result.addr);
        request.setCoorType(result.coorType);
        request.setLocationType(result.locationType);
        request.setLocationWhere(result.locationWhere);
        request.setRadius(result.radius);

        return UserService.setUserPos(request)
                .map(res -> {
                    if (res.getStatus() != NetConstant.RESULT_SUCCESS) {
                        throw new Exception(res.getErrorMessage());
                    }
                    CustLog.d("MainLogic", new com.google.gson.Gson().toJson(res));

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
                            result.addr,
                            result.describe);

                    new Thread(() -> setUsersToDB(res)).start();

                    return UserPosSP.getNearDesc();
                });
    }
}
