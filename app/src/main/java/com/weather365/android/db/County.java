package com.weather365.android.db;

import org.litepal.crud.DataSupport;

/**
 * 县
 * Created by Chen on 2017/2/17.
 */

public class County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;//记录县所对应的天气id
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

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
