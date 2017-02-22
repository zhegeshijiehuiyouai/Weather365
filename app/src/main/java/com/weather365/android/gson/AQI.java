package com.weather365.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 空气质量AQI
 * Created by Chen on 2017/2/19.
 */

public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;

        @SerializedName("qlty")
        public String quality;
    }
}
