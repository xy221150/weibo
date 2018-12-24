package org.wxy.weibo.cosmos.bean.basebean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wxy on 2018/7/4.
 */

public class Statusbean implements Serializable {
    private String created_at;
    private String idstr;
    private String text;
    private String source;
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;
    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;
    private long id;
    private long mid;
    private int reposts_count;
    private int comments_count;
    private int attitudes_count;
    private int mlevel;
    private boolean favorited;
    private boolean truncated;
    private Statusbean retweeted_status;
    private Geobean geo;
    private ArrayList<String> pic_ids = new ArrayList<>();
    private Visiblebean visible;

    public ArrayList<String> getPic_ids() {
        return pic_ids;
    }

    public void setPic_ids(ArrayList<String> pic_ids) {
        this.pic_ids = pic_ids;
    }

    public Visiblebean getVisible() {
        return visible;
    }

    public void setVisible(Visiblebean visible) {
        this.visible = visible;
    }

    public Geobean getGeo() {
        return geo;
    }

    public void setGeo(Geobean geo) {
        this.geo = geo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public Statusbean getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Statusbean retweeted_status) {
        this.retweeted_status = retweeted_status;
    }
}
