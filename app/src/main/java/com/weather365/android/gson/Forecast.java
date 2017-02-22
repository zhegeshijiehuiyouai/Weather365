package com.weather365.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 单日天气预报
 * Created by Chen on 2017/2/19.
 */

public class Forecast {
    public String date;

    @SerializedName("cond")
    public More more;

    @SerializedName("tmp")
    public Temperature temperature;

    public class More {
        @SerializedName("txt_d")
        public String info;
    }

    public class Temperature {
        public String max;
        public String min;
    }
}
