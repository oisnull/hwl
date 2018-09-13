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
import com.hwl.beta.ui.common.rxext.DefaultAction;
import com.hwl.beta.ui.common.rxext.DefaultConsumer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.ui.entry.bean.MainBean;
import com.hwl.beta.ui.entry.standard.MainStandard;

public class MainHandle implements MainStandard {
    private BaiduLocation locationService;

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
    public void getLocation(final DefaultConsumer<String> succCallback, final
    DefaultConsumer<String>
            errorCallback) {
        locationService = new BaiduLocation(new BaiduLocation.OnLocationListener() {
            @Override
            public void onSuccess(BaiduLocation.ResultModel result) {
                //判断本地存储的位置是否与当前定位的位置是否一样，如果一样则不做任何操作
                if (UserPosSP.getLontitude() == result.lontitude && UserPosSP.getLatitude() ==
                        result.latitude) {
                    succCallback.accept(UserPosSP.getNearDesc());
                    return;
                } else {
                    //往本地存储一份定位数据
                    UserPosSP.setUserPos(
                            result.latitude,
                            result.lontitude,
                            result.country,
                            result.province,
                            result.city,
                            result.district,
                            result.street,
                            result.describe);
                }

                setUserPos(result, succCallback, errorCallback);
            }

            @Override
            public void onFaild(BaiduLocation.ResultInfo info) {
//                    binding.tbTitle.setTitle(UserPosSP.getNearDesc());
//                    showLocationDialog();
//                    locationTip.setTitleShow("定位失败");
//                    locationTip.setContentShow(info.status + " : " + info.message);
                errorCallback.accept(info.message);
            }
        });
        locationService.start();
    }

    //设置用户位置信息，并返回当前位置群组信息和组用户信息
    private void setUserPos(BaiduLocation.ResultModel result, final DefaultConsumer<String>
            succCallback,
                            final DefaultConsumer<String>
                                    errorCallback) {
        //调用api将当前位置存储到服务器
        final SetUserPosRequest request = new SetUserPosRequest();
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
        UserService.setUserPos(request)
                .subscribe(new NetDefaultObserver<SetUserPosResponse>() {
                    @Override
                    protected void onSuccess(SetUserPosResponse res) {
                        if (res.getStatus() == NetConstant.RESULT_SUCCESS) {
                            UserPosSP.setUserPos(res.getUserPosId(), res.getUserGroupGuid());
                            succCallback.accept(UserPosSP.getNearDesc());
                            addLocalGroupInfo(res);
                        } else {
                            errorCallback.accept("定位失败");
                        }
                    }
                });
    }

    private void addLocalGroupInfo(SetUserPosResponse res) {
        if (res.getGroupUserInfos() != null && res.getGroupUserInfos
                ().size() > 0) {
            //保存组和组用户数据到本地
            GroupInfo groupInfo = DBGroupAction
                    .convertToNearGroupInfo(res.getUserGroupGuid(), res.getGroupUserInfos().size
                            (), DBGroupAction
                            .convertToGroupUserImages(res.getGroupUserInfos()));
            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
            DaoUtils.getGroupUserInfoManagerInstance()
                    .addListAsync(DBGroupAction.convertToGroupUserInfos(res.getGroupUserInfos()));
//            EventBus.getDefault().post(new EventActionGroup
//                    (EventBusConstant.EB_TYPE_GROUP_IMAGE_UPDATE, groupInfo));
        }
    }
}
