package com.weather365.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 联网操作
 * Created by Chen on 2017/2/18.
 */

public class HttpUtil {
    /**
     * 发送Http请求
     *
     * @param address  网络地址
     * @param callback 回调接口
     */
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
