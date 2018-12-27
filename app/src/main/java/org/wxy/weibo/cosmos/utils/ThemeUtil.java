package org.wxy.weibo.cosmos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import org.wxy.weibo.cosmos.Constants;
import org.wxy.weibo.cosmos.ui.activity.ThemeActivity;


public class ThemeUtil {

    //得到主题
    public static int getTheme(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("theme_select", 0);
    }

    //设置主题
    public static void setTheme(Context context, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.THEME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("theme_select", position).commit();
    }
}
