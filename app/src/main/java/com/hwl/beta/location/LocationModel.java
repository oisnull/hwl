package com.hwl.beta.location;

import com.hwl.beta.utils.StringUtils;

public class LocationModel {
    public float radius;
    public float latitude;
    public float longitude;
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
