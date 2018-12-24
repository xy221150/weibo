package org.wxy.weibo.cosmos.bean.basebean;

import java.io.Serializable;

/**
 * Created by wxy on 2018/7/8.
 */

public class PicUrlsBean  implements Serializable {
    private String thumbnail_pic;
    private String original_pic;
    private String bmiddle_pic;

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }
}
