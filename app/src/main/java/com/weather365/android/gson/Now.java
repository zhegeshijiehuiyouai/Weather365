package com.weather365.android.gson;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

/**
 * 实况天气
 * Created by Chen on 2017/2/19.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
