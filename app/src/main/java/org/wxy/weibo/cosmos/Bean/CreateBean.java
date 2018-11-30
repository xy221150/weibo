package org.wxy.weibo.cosmos.Bean;

import org.wxy.weibo.cosmos.Bean.basebean.Statusbean;

import java.util.List;

public class CreateBean {

    /**
     * created_at : Wed Jun 01 00:50:25 +0800 2011
     * id : 12438492184
     * text : love your work.......
     * mid : 202110601896455629
     * user : {"id":1404376560,"screen_name":"zaku","name":"zaku","province":"11","city":"5","location":"北京 朝阳区","description":"人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。","url":"http://blog.sina.com.cn/zaku","profile_image_url":"http://tp1.sinaimg.cn/1404376560/50/0/1","domain":"zaku","gender":"m","followers_count":1204,"friends_count":447,"statuses_count":2908,"favourites_count":0,"created_at":"Fri Aug 28 00:00:00 +0800 2009","following":false,"allow_all_act_msg":false,"remark":"","geo_enabled":true,"verified":false,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1404376560/180/0/1","verified_reason":"","follow_me":false,"online_status":0,"bi_followers_count":215}
     * status : {"created_at":"Tue May 31 17:46:55 +0800 2011","id":11488058246,"text":"求关注。","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":null,"mid":"5612814510546515491","reposts_count":8,"comments_count":9,"annotations":[],"user":{"id":1404376560,"screen_name":"zaku","name":"zaku","province":"11","city":"5","location":"北京 朝阳区","description":"人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。","url":"http://blog.sina.com.cn/zaku","profile_image_url":"http://tp1.sinaimg.cn/1404376560/50/0/1","domain":"zaku","gender":"m","followers_count":1204,"friends_count":447,"statuses_count":2908,"favourites_count":0,"created_at":"Fri Aug 28 00:00:00 +0800 2009","following":false,"allow_all_act_msg":false,"remark":"","geo_enabled":true,"verified":false,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1404376560/180/0/1","verified_reason":"","follow_me":false,"online_status":0,"bi_followers_count":215}}
     */

    private String created_at;
    private long id;
    private String text;
    private String mid;
    private Userbean user;
    private Statusbean status;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public Userbean getUser() {
        return user;
    }

    public void setUser(Userbean user) {
        this.user = user;
    }

    public Statusbean getStatus() {
        return status;
    }

    public void setStatus(Statusbean status) {
        this.status = status;
    }
}
