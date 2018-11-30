package org.wxy.weibo.cosmos;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;


public class MainActivity extends StackApplication{
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private static MainActivity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        WbSdk.install(this,new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE));
        mainActivity=this;
    }

    public static MainActivity mainActivity(){
        return mainActivity;
   }
}
