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
        emjoymap.put("[爱你]", R.drawable.d_aini);
        emjoymap.put("[奥特曼]", R.drawable.d_aoteman);
        emjoymap.put("[拜拜]", R.drawable.d_baibai);
        emjoymap.put("[悲伤]", R.drawable.d_beishang);
        emjoymap.put("[鄙视]", R.drawable.d_bishi);
        emjoymap.put("[闭嘴]", R.drawable.d_bizui);
        emjoymap.put("[馋嘴]", R.drawable.d_chanzui);
        emjoymap.put("[吃惊]", R.drawable.d_chijing);
        emjoymap.put("[打哈欠]", R.drawable.d_dahaqi);
        emjoymap.put("[打脸]", R.drawable.d_dalian);
        emjoymap.put("[叮]", R.drawable.d_ding);
    }
    public static int getImageByName(String name) {
        Integer integer = emjoymap.get(name);
        return integer == null ? -1 : integer;
    }
}
