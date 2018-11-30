package org.wxy.weibo.cosmos.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import org.wxy.weibo.cosmos.MainActivity;

/**
 * Created by wxy on 2018/7/3.
 */

public class User {
     private static User user;
     private String name;
     private String password;
     private String token;
     private String uid;
     public User(){
         UserInfo info=new UserInfo();
         name=info.getStringValue(MainActivity.mainActivity(),"name");
         password=info.getStringValue(MainActivity.mainActivity(),"password");
         token=info.getStringValue(MainActivity.mainActivity(),"token");
         uid=info.getStringValue(MainActivity.mainActivity(),"uid");
     }
     public static User user(){
         if (user==null)
         {
             user=new User();
         }
         return user;
     }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        UserInfo.SharedPreferences(MainActivity.mainActivity(),"token",token);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
        UserInfo.SharedPreferences(MainActivity.mainActivity(),"uid",uid);
    }

    public static class UserInfo {
        private static final String SharedPreferences_Name = "weibo";


        public static void SharedPreferences(Context context, String key, int value) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putInt(key, value);
            editor.commit();
        }

        public static void SharedPreferences(Context context, String key, String value) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putString(key, value);
            editor.commit();
        }

        public static void SharedPreferences(Context context, String key, Long value) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putLong(key, value);
            editor.commit();
        }

        public static void SharedPreferences(Context context, String key, Boolean value) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }

        public static String getStringValue(Context context, String key) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            String Value = sp1.getString(key, "");
            return Value;
        }

        public static int getintValue(Context context, String key) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            int Value = sp1.getInt(key, 0);
            return Value;
        }

        public static Long getLongValue(Context context, String key) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            Long Value = sp1.getLong(key, 0l);
            return Value;
        }

        public static Boolean getBooleanValue(Context context, String key) {
            SharedPreferences sp1 = context.getSharedPreferences(SharedPreferences_Name, Context.MODE_PRIVATE);
            Boolean Value = sp1.getBoolean(key, false);
            return Value;
        }
    }
}