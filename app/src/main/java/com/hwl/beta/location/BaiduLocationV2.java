package com.hwl.beta.location;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hwl.beta.HWLApp;
import com.hwl.beta.utils.StringUtils;

public class BaiduLocationV2 {
    public final static int NOT_START = 0;
    public final static int POSITIONING = 1;
    public final static int COMPLETE_SUCCESS = 2;
    public final static int COMPLETE_FAILD = 3;

    private LocationClient client = null;
    private LocationClientOption mOption, DIYoption;
    private int currentStatus = NOT_START;
	private String errorMessage;

    public BaiduLocationV2(final IHWLLoactionListener locationListener) {
        client = new LocationClient(HWLApp.getContext());
        client.setLocOption(getDefaultLocationClientOption());
        client.registerLocationListener(new BDAbstractLocationListener() {

			private bool isSuccess(BDLocation location){
				return location != null && 
				location.getLatitude() > 0 && 
				location.getLongitude() > 0 &&
				(location.getLocType() == BDLocation.TypeGpsLocation || 
				location.getLocType() == BDLocation.TypeNetWorkLocation ||
				location.getLocType() == BDLocation.TypeOffLineLocation);
			}

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (isSuccess(location)) {
					LocationModel model = new LocationModel();
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
					currentStatus = COMPLETE_SUCCESS;
					errorMessage = null;
					locationListener.onSuccess(model);
				} else {					
					currentStatus = COMPLETE_FAILD;
					errorMessage = location.getLocTypeDescription();
					locationListener.onFailure(errorMessage);
				}

				stop();
			}
		});
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
			//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
			//mOption.setOpenAutoNotifyMode();
			//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
			//mOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT); 

        }
        return mOption;
    }

    public LocationClientOption getOption() {
        if (DIYoption == null) {
            DIYoption = new LocationClientOption();
        }
        return DIYoption;
    }

	public String getErrorMessage(){
		return this.errorMessage;
	}

    public int getcurrentStatus() {
        return this.currentStatus;
    }

    public void start() {
        if (client != null && !client.isStarted()) {
            client.start();
            currentStatus = POSITIONING;
        }
    }

    public void stop() {
        if (client != null && client.isStarted()) {
            client.stop();
        }
    }

    public boolean isStart() {
        return client.isStarted();
    }

    public boolean requestHotSpotState() {
        return client.requestHotSpotState();
    }
}
