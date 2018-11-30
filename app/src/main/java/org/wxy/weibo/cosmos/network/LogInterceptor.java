package org.wxy.weibo.cosmos.network;

import android.text.TextUtils;
;

import org.wxy.weibo.cosmos.sharepreferences.User;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by wxy on 2018/5/11.
 */

public class LogInterceptor implements Interceptor {
    private final static String TAG="LogInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request= chain.request();
        Request newRequset;
        Request.Builder builder=request.newBuilder();
        String token= User.user().getToken();
        if (!TextUtils.isEmpty(token))
        {
            token="Bearer "+token;
            builder.header("Authorization",token);
        }
        newRequset=builder.header("Content-Type","application/x-www-form-urlencoded")
                .header("Accepe","application/x-www-form-urlencoded").build();
        Response response=chain.proceed(newRequset);
        return response.newBuilder().build();
    }

}
