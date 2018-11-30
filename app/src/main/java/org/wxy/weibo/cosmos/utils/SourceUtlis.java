package org.wxy.weibo.cosmos.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by wxy on 2018/7/7.
 */

public class SourceUtlis {
    private String Source;
   public String SourceUtlis(String str){
       Pattern p = Pattern.compile("<a[^>]*>([^<]*)</a>");
       Matcher m = p.matcher(str);
       while(m.find()) {
           Source=m.group(1);
       }
       return Source;
   }
}
