package com.hwl.beta.location;

public class LocationModel {
    public String coorType;
    public int locationWhere;
    public String locationType;
    public float radius;
    public double latitude;
    public double longitude;
    public String country;
    public String province;
    public String city;
    public String district;
    public String town;
    public String street;
    public String addr;
    public String describe;

    @Override
    public String toString() {
        return country + province + city + district + street;
    }
}
