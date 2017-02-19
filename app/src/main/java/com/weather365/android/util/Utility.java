package com.weather365.android.util;

import android.text.TextUtils;

import com.weather365.android.db.City;
import com.weather365.android.db.County;
import com.weather365.android.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Chen on 2017/2/18.
 */

public class Utility {
    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param response 服务器返回的json数据
     * @return 操作成功返回true，失败返回false
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    /**
                     *返回的JSON格式是[{"id":1,"name":"北京"}]
                     */
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    /**
                     * 将数据写入到数据库中
                     */
                    province.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response   服务器返回的json数据
     * @param provinceId 省份id
     * @return 操作成功返回true，失败返回false
     */
    public static boolean handleCityResponse(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    /**
                     * 返回的JSON格式是[{"id":113,"name":"南京"}]
                     */
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param response 服务器返回的json数据
     * @param cityId   城市id
     * @return 操作成功返回true，失败返回false
     */
    public static boolean handleCountyResponse(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    /**
                     * 返回的JSON格式是[{"id":973,"name":"苏州","weather_id":"CN101190401"}]
                     */
                    county.setCountyName(countyObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.save();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
