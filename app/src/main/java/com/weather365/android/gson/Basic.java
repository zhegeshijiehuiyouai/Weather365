package com.weather365.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 城市、更新时间
 * Created by Chen on 2017/2/19.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;
}
