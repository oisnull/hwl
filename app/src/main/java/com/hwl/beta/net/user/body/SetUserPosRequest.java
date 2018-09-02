package com.hwl.beta.net.user.body;

/**
 * Created by Administrator on 2018/1/20.
 */

public class SetUserPosRequest {
    private long UserId;
    private String LastGroupGuid;
    private String Country;
    private String Province;
    private String City;
    private String District;
    private String Street;
    private String Details;
    private String Latitude;
    private String Longitude;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getLastGroupGuid() {
        return LastGroupGuid;
    }

    public void setLastGroupGuid(String lastGroupGuid) {
        LastGroupGuid = lastGroupGuid;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
