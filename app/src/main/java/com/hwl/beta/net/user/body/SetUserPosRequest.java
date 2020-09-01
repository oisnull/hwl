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
    private double Latitude;
    private double Longitude;
    private String CoorType;
    private int LocationWhere;
    private String LocationType;
    private float Radius;

    public String getCoorType() {
        return CoorType;
    }

    public void setCoorType(String coorType) {
        CoorType = coorType;
    }

    public int getLocationWhere() {
        return LocationWhere;
    }

    public void setLocationWhere(int locationWhere) {
        LocationWhere = locationWhere;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    public float getRadius() {
        return Radius;
    }

    public void setRadius(float radius) {
        Radius = radius;
    }

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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
