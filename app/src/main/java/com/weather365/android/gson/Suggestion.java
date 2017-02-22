package com.weather365.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 生活指数
 * Created by Chen on 2017/2/19.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    @SerializedName("drsg")
    public Dress dress;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    public class Dress {
        @SerializedName("txt")
        public String info;
    }
}
