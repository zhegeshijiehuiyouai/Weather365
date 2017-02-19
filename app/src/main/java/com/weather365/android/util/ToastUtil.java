package com.weather365.android.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast的优化版本，防止连续点击按钮时弹出多个Toast
 * 在一个Toast没有显示完时，继续点击按钮不会弹出Toast
 * Created by Chen on 2017/2/18.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
