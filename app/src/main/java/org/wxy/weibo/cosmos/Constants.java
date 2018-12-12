

package org.wxy.weibo.cosmos;


import java.util.regex.Pattern;

public class Constants {

    public static final String APP_KEY      = "4173665367";

//    public static final String APP_KEY      = "2873264506";

//    public static final String APP_KEY      = "656963923";


    public static final String App_SECRET      = "e42b898df9525a23424c8b0ecf2a7a88";

    public static final String REDIRECT_URL = "http://api.weibo.com/oauth2/default.html";

    public static final  String SERIVER="https://api.weibo.com/";

    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
   // public static final String SCOPE = null;

    // #话题#
    public static final String REGEX_TOPIC = "#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#";
    // [表情]
    public static final String REGEX_EMOTION = "\\[(\\S+?)\\]";
    // url
    public static final String REGEX_URL = "http://[a-zA-Z0-9+&@#/%?=~_\\\\-|!:,\\\\.;]*[a-zA-Z0-9+&@#/%=~_|]";
    // @人
    public static final String REGEX_AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}";
    //
    public static final String REGEX_FULL ="...全文： ";

    public static final Pattern PATTERN_TOPIC = Pattern.compile(REGEX_TOPIC);
    public static final Pattern PATTERN_EMOTION = Pattern.compile(REGEX_EMOTION);
    public static final Pattern PATTERN_URL = Pattern.compile(REGEX_URL);
    public static final Pattern PATTERN_AT = Pattern.compile(REGEX_AT);
    public static final Pattern PATTERN_FULL = Pattern.compile(REGEX_FULL);

    public static final String SCHEME_TOPIC = "topic:";
    public static final String SCHEME_URL = "url:";
    public static final String SCHEME_AT = "at:";
    public static final String SCHEME_FULL = "full:";
}
