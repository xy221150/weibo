package org.wxy.weibo.cosmos.utils;


import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateFormat;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by wxy on 2018/7/7.
 */

public class TimeUtils {

    public static String convDate(String time) {
        try {
            Context context = Activity.mainActivity();
            Resources res = context.getResources();

            StringBuffer buffer = new StringBuffer();

            Calendar createCal = Calendar.getInstance();

            if (time.length() == 13) {
                try {
                    createCal.setTimeInMillis(Long.parseLong(time));
                } catch (Exception e) {
                    createCal.setTimeInMillis(Date.parse(time));
                }
            }
            else {
                createCal.setTimeInMillis(Date.parse(time));
            }

            Calendar currentcal = Calendar.getInstance();
            currentcal.setTimeInMillis(System.currentTimeMillis());

            long diffTime = (currentcal.getTimeInMillis() - createCal.getTimeInMillis()) / 1000;

            // 同一月
            if (currentcal.get(Calendar.MONTH) == createCal.get(Calendar.MONTH)) {
                // 同一天
                if (currentcal.get(Calendar.DAY_OF_MONTH) == createCal.get(Calendar.DAY_OF_MONTH)) {
                    if (diffTime < 3600 && diffTime >= 60) {
                        buffer.append((diffTime / 60) + res.getString(R.string.msg_few_minutes_ago));
                    } else if (diffTime < 60) {
                        buffer.append(res.getString(R.string.msg_now));
                    } else {
                        buffer.append(res.getString(R.string.msg_today)).append(" ").append(DateFormat.format( "HH:mm",createCal.getTimeInMillis()));
                    }
                }
                // 前一天
                else if (currentcal.get(Calendar.DAY_OF_MONTH) - createCal.get(Calendar.DAY_OF_MONTH) == 1) {
                    buffer.append(res.getString(R.string.msg_yesterday)).append(" ").append(DateFormat.format( "HH:mm",createCal.getTimeInMillis()));
                }
            }

            if (buffer.length() == 0) {
                buffer.append(DateFormat.format("MM-dd HH:mm",createCal.getTimeInMillis()));
            }

            String timeStr = buffer.toString();
            if (currentcal.get(Calendar.YEAR) != createCal.get(Calendar.YEAR)) {
                timeStr = createCal.get(Calendar.YEAR) + " " + timeStr;
            }

            return timeStr;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return time;
    }

    public static String convDate1(String time) {
        try {
            Context context = Activity.mainActivity();
            Resources res = context.getResources();

            StringBuffer buffer = new StringBuffer();

            Calendar createCal = Calendar.getInstance();

            if (time.length() == 13) {
                try {
                    createCal.setTimeInMillis(Long.parseLong(time));
                } catch (Exception e) {
                    createCal.setTimeInMillis(Date.parse(time));
                }
            }
            else {
                createCal.setTimeInMillis(Date.parse(time));
            }

            Calendar currentcal = Calendar.getInstance();
            currentcal.setTimeInMillis(System.currentTimeMillis());


            if (buffer.length() == 0) {
                buffer.append(DateFormat.format("MM-dd HH:mm",createCal.getTimeInMillis()));
            }

            String timeStr = buffer.toString();
            if (currentcal.get(Calendar.YEAR) != createCal.get(Calendar.YEAR)) {
                timeStr = createCal.get(Calendar.YEAR) + " " + timeStr;
            }

            return timeStr;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return time;
    }

}
