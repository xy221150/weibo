package org.wxy.weibo.cosmos;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.wxy.weibo.cosmos.view.SlidingLayout;


public class Activity extends StackApplication{
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
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
