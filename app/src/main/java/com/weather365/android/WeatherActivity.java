package com.weather365.android;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.weather365.android.gson.Weather;
import com.weather365.android.util.HttpUtil;
import com.weather365.android.util.ToastUtil;
import com.weather365.android.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView aqiQualityText;
    private TextView comfortText;
    private TextView carWashText;
    private TextView dressText;
    private ImageView bingPicImg;
    public SwipeRefreshLayout swipeRefresh;
    private Button navButton;
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        initWidget();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        final String weatherId;
        if (weatherString != null) {
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            //无缓存时去服务器查询天气
            weatherId = getIntent().getStringExtra("weather_id");//该weatherId从ChooseAreaFragment中来
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
        //下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
        //点击位置图标再次选择地理位置
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    /**
     * 根据天气id查询城市的天气信息
     *
     * @param weatherId 城市ID
     */
    public void requestWeather(String weatherId) {
        String url = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=748e05a68d2a46e287a4617a7dfbcd7d";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            //储存天气信息
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                            ToastUtil.showToast(WeatherActivity.this, "刚刚刷新了天气");
                        } else {
                            ToastUtil.showToast(WeatherActivity.this, "获取天气信息失败");
                            //Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }


            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(WeatherActivity.this, "获取天气信息失败");
                //Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        });
        loadBingPic();
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String url = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(preferences.getString("bing_pic", null)).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 界面显示
     *
     * @param weather weather类的对象
     */
    private void showWeatherInfo(Weather weather) {
        //设置第一块
        titleCity.setText(weather.basic.cityName);
        degreeText.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.more.info);
        //设置第三块
        forecastLayout.removeAllViews();
        for (int i = 0; i < weather.forecastList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            if (i == 0) {
                dateText.setText("今天");
            } else if (i == 1) {
                dateText.setText("明天");
            } else if (i == 2) {
                dateText.setText("后天");
            } else {
                dateText.setText(weather.forecastList.get(i).date);
            }
            infoText.setText(weather.forecastList.get(i).more.info);
            maxText.setText(weather.forecastList.get(i).temperature.max + "℃");
            minText.setText(weather.forecastList.get(i).temperature.min + "℃");
            forecastLayout.addView(view);
        }
        //设置第三块
        if (weather.aqi != null) {
            //if 判断，防止崩溃
            aqiText.setText(weather.aqi.city.aqi);
            aqiQualityText.setText(weather.aqi.city.quality);
        }
        //设置第四块
        comfortText.setText("舒适指数：" + weather.suggestion.comfort.info);
        carWashText.setText("洗车指数：" + weather.suggestion.carWash.info);
        dressText.setText("穿衣指数：" + weather.suggestion.dress.info);
        weatherLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化组件
     */
    private void initWidget() {
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        aqiQualityText = (TextView) findViewById(R.id.aqi_quality_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        dressText = (TextView) findViewById(R.id.dress_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        navButton = (Button) findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }
}
