package com.weather365.android.db;

/**
 * 县
 * Created by Chen on 2017/2/17.
 */

public class County {
    private int id;
    private String countyName;
    private int weatherId;//记录县所对应的天气id
    private int cityId;//记录当前县所属的市

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
