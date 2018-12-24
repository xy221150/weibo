package org.wxy.weibo.cosmos.bean.basebean;

/**
 * Created by wxy on 2018/7/4.
 */

public class Url_shortbean {
    private String url_short;
    private String url_long;
    private int type;
    private boolean result;

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
