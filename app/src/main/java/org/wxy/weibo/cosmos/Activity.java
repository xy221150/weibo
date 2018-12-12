package org.wxy.weibo.cosmos;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;



public class Activity extends StackApplication{
    private static Activity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        WbSdk.install(this,new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));
        mainActivity=this;

    }

    public static Activity mainActivity(){
        return mainActivity;
   }

}
