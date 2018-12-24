package org.wxy.weibo.cosmos.bean;

import org.wxy.weibo.cosmos.bean.basebean.Statusbean;

import java.util.List;

public class CommentsShowBean {


    private boolean hasvisible;
    private int previous_cursor;
    private int next_cursor;
    private int total_number;
    private int since_id;
    private int max_id;
    private Statusbean status;
    private List<CommentsBean> comments;
    private List<?> marks;

    public boolean isHasvisible() {
        return hasvisible;
    }

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public int getSince_id() {
        return since_id;
    }

    public void setSince_id(int since_id) {
        this.since_id = since_id;
    }

    public int getMax_id() {
        return max_id;
    }

    public void setMax_id(int max_id) {
        this.max_id = max_id;
    }


    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public List<?> getMarks() {
        return marks;
    }

    public void setMarks(List<?> marks) {
        this.marks = marks;
    }

    public Statusbean getStatus() {
        return status;
    }

    public void setStatus(Statusbean status) {
        this.status = status;
    }

    public static class CommentsBean {
        /**
         * created_at : Wed Nov 28 15:18:05 +0800 2018
         * id : 4311350341451956
         * rootid : 4311350341451956
         * floor_number : 3
         * text : mkkkk
         * disable_reply : 0
         * user : {"id":6595944977,"idstr":"6595944977","class":1,"screen_name":"测试微博Q","name":"测试微博Q","province":"33","city":"1000","location":"浙江","description":"","url":"","profile_image_url":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_50.gif","profile_url":"u/6595944977","domain":"","weihao":"","gender":"f","followers_count":1,"friends_count":4,"pagefriends_count":0,"statuses_count":6,"video_status_count":0,"favourites_count":0,"created_at":"Sun Jul 08 22:22:21 +0800 2018","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","insecurity":{"sexual_content":false},"ptype":0,"allow_all_comment":true,"avatar_large":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_180.gif","avatar_hd":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_180.gif","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"like":false,"like_me":false,"online_status":0,"bi_followers_count":1,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":2,"story_read_state":-1,"vclub_member":0}
         * mid : 4311350341451956
         * idstr : 4311350341451956
         * status : {"created_at":"Tue Nov 27 17:56:49 +0800 2018","id":4311027904938383,"idstr":"4311027904938383","mid":"4311027904938383","can_edit":false,"show_additional_indication":0,"text":"http://t.cn/RXfIElf","textLength":19,"source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/6JfnwP\" rel=\"nofollow\">未通过审核应用<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"is_paid":false,"mblog_vip_type":0,"user":{"id":6595944977,"idstr":"6595944977","class":1,"screen_name":"测试微博Q","name":"测试微博Q","province":"33","city":"1000","location":"浙江","description":"","url":"","profile_image_url":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_50.gif","profile_url":"u/6595944977","domain":"","weihao":"","gender":"f","followers_count":1,"friends_count":4,"pagefriends_count":0,"statuses_count":6,"video_status_count":0,"favourites_count":0,"created_at":"Sun Jul 08 22:22:21 +0800 2018","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","insecurity":{"sexual_content":false},"ptype":0,"allow_all_comment":true,"avatar_large":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_180.gif","avatar_hd":"http://tvax2.sinaimg.cn/default/images/default_avatar_female_180.gif","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"like":false,"like_me":false,"online_status":0,"bi_followers_count":1,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":2,"story_read_state":-1,"vclub_member":0},"reposts_count":0,"comments_count":0,"attitudes_count":0,"pending_approval_count":0,"isLongText":false,"reward_exhibition_type":0,"hide_flag":0,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"hasActionTypeCard":0,"darwin_tags":[],"hot_weibo_tags":[],"text_tag_tips":[],"mblogtype":0,"userType":0,"more_info_type":0,"positive_recom_flag":0,"content_auth":0,"gif_ids":"","is_show_bulletin":2,"comment_manage_info":{"comment_permission_type":-1,"approval_comment_type":0}}
         */

        private String created_at;
        private long id;
        private long rootid;
        private int floor_number;
        private String text;
        private int disable_reply;
        private Userbean user;
        private String mid;
        private String idstr;
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

        public long getRootid() {
            return rootid;
        }

        public void setRootid(long rootid) {
            this.rootid = rootid;
        }

        public int getFloor_number() {
            return floor_number;
        }

        public void setFloor_number(int floor_number) {
            this.floor_number = floor_number;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getDisable_reply() {
            return disable_reply;
        }

        public void setDisable_reply(int disable_reply) {
            this.disable_reply = disable_reply;
        }



        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getIdstr() {
            return idstr;
        }

        public void setIdstr(String idstr) {
            this.idstr = idstr;
        }

        public Statusbean getStatus() {
            return status;
        }

        public void setStatus(Statusbean status) {
            this.status = status;
        }

        public Userbean getUser() {
            return user;
        }

        public void setUser(Userbean user) {
            this.user = user;
        }
    }
}
