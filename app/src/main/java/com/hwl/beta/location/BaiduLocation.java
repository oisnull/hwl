package com.hwl.beta.location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.StringUtils;

public class BaiduLocation {
    public final static int POSITIONING = 0x1;
    public final static int COMPLETE_SUCCESS = 0x2;
    public final static int COMPLETE_FAILD = 0x3;
    public final static int NOT_START = 0x4;

    public interface OnLocationListener {
        void onSuccess(ResultModel result);

        void onFaild(ResultInfo info);
    }

    public class ResultInfo {
        public int status;
        public String message;
    }

    public class ResultModel {
        public float radius;//定位精度
        public float latitude;
        public float lontitude;
        public String country;
        public String province;
        public String city;
        public String district;
        public String street;
        public String addr;
        public String describe;

        public String getNearDesc() {
            if (!StringUtils.isBlank(this.street)) {
                return street + "附近";
            }
            if (!StringUtils.isBlank(describe)) {
                return describe + "附近";
            }
            return "我的附近";
        }

        @Override
        public String toString() {
            return country + province + city + district + street;
        }
    }

    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private static Object objLock = new Object();
    private int currentLocationStatus = NOT_START;

    public BaiduLocation(final OnLocationListener onLocationListener) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(HWLApp.getContext());
                client.setLocOption(getDefaultLocationClientOption());
                client.registerLocationListener(new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation location) {
//                        Log.d("当前定位信息：", new com.google.gson.Gson().toJson(location));
                        if (location != null && BDLocation.TypeNetWorkLocation == location
                                .getLocType() && location.getLatitude() > 0 && location
                                .getLongitude() > 0) {
                            currentLocationStatus = COMPLETE_SUCCESS;
                            ResultModel model = new ResultModel();
                            model.radius = location.getRadius();
                            model.latitude = (float) location.getLatitude();
                            model.lontitude = (float) location.getLongitude();
                            model.country = location.getCountry();
                            model.province = location.getProvince();
                            model.city = location.getCity();
                            model.district = location.getDistrict();
                            model.street = location.getStreet();
                            model.addr = location.getAddrStr();
                            model.describe = location.getLocationDescribe();
                            onLocationListener.onSuccess(model);
                        } else {
                            //模拟器的时候使用
                            currentLocationStatus = COMPLETE_SUCCESS;
                            ResultModel model = new ResultModel();
                            model.radius = 40.0f;
                            model.latitude = (float) 31.07344;
                            model.lontitude = (float) 121.507054;
                            model.country = "中国";
                            model.province = "上海市";
                            model.city = "上海市";
                            model.district = "闵行区";
                            model.street = "浦晓南路(ERROR)";
                            model.addr = "Location failed,here is a fix value";
                            model.describe = "在世博家园十三街坊附近";
                            onLocationListener.onSuccess(model);

//                            currentLocationStatus = LocationService.COMPLETE_FAILD;
//                            ResultInfo info = new ResultInfo();
//                            info.status = location.getLocType();
//                            info.message = location.getLocTypeDescription();
//                            onLocationListener.onFaild(info);
                        }
                        client.stop();
                    }
                });
            }
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option) {
        boolean isSuccess = false;
        if (option != null) {
            if (client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    /***
     *
     * @return DefaultLocationClientOption  默认O设置
     */
    public LocationClientOption getDefaultLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            //mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            //mOption.setScanSpan(3000);//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);
            //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
            // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation
            // .getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setOpenGps(true);//可选，默认false，设置是否开启Gps定位
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用

        }
        return mOption;
    }

    /**
     * @return DIYOption 自定义Option设置
     */
    public LocationClientOption getOption() {
        if (DIYoption == null) {
            DIYoption = new LocationClientOption();
        }
        return DIYoption;
    }

    public int getCurrentLocationStatus() {
        return this.currentLocationStatus;
    }

    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                client.start();
                currentLocationStatus = POSITIONING;
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                client.stop();
            }
        }
    }

    public boolean isStart() {
        return client.isStarted();
    }

    public boolean requestHotSpotState() {
        return client.requestHotSpotState();
    }

//    /***
//     *
//     * @param listener
//     * @return
//     */
//
//    public boolean registerListener(BDAbstractLocationListener listener) {
//        boolean isSuccess = false;
//        if (listener != null) {
//            client.registerLocationListener(listener);
//            isSuccess = true;
//        }
//        return isSuccess;
//    }
//
//    public void unregisterListener(BDAbstractLocationListener listener) {
//        if (listener != null) {
//            client.unRegisterLocationListener(listener);
//        }
//    }
}
