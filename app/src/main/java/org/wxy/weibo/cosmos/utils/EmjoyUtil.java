package org.wxy.weibo.cosmos.utils;

import org.wxy.weibo.cosmos.R;

import java.util.LinkedHashMap;

/**
 * Created by wxy on 2018/7/6.
 */

public class EmjoyUtil {
    public static LinkedHashMap<String,Integer> emjoymap;
    static {
        emjoymap=new LinkedHashMap<>();
        emjoymap.put("[微笑]", R.drawable.d_hehe);
    }
    public static int getImageByName(String name) {
        Integer integer = emjoymap.get(name);
        return integer == null ? -1 : integer;
    }
}
