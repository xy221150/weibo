package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.Bean.Tokenbean;
import org.wxy.weibo.cosmos.Bean.Userbean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by wxy on 2018/7/3.
 */

public interface IUser {
    //获取用户信息
    @GET("2/users/show.json")
    Call<Userbean> getuser(@Query("access_token") String access_token,
                           @Query("uid") String uid,
                           @Query("screen_name") String screen_name);


    //获取用户信息
    @GET("2/users/show.json")
    Call<Userbean> getuser1(@QueryMap Map<String,String> map);



}
