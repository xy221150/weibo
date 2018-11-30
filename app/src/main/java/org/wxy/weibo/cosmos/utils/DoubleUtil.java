package org.wxy.weibo.cosmos.utils;

/**
 * Created by wxy on 2018/7/10.
 */

public class DoubleUtil {
    private static float count;
    private static String Stringcount;
    public static String count(int i)
    {
        if (i/10000>=1)
        {
            count=i/10000;
            Stringcount=String.format("%.1f",count)+"万";
        }
        else
        {
            Stringcount=i+"";
        }
        return Stringcount;
    }
}
