package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.bean.Home_timelinebean;
import org.wxy.weibo.cosmos.bean.MentionswbBean;
import org.wxy.weibo.cosmos.bean.ShareBean;
import org.wxy.weibo.cosmos.bean.ShowBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by wxy on 2018/7/4.
 */

public interface IStatuses {

    //获取当前登录用户及其所关注用户的最新微博
    @GET("2/statuses/home_timeline.json")
    Call<Home_timelinebean> gethome_timeline(@Query("access_token") String access_token,
                                             @Query("count") int count,
                                             @Query("page") int page,
                                             @Query("feature") int feature);



    //获取用户发布的微博
    @GET("2/statuses/user_timeline.json")
    Call<Home_timelinebean> getuser_timeline(@Query("access_token") String access_token,
                                             @Query("uid")String uid,
                                             @Query("screen_name") String screen_name,
                                             @Query("count") int count,
                                             @Query("page") int page,
                                             @Query("feature") int feature);

    //发送微博
    @Multipart
    @Headers({"Content-Type:application/x-www-form-urlencoded"})
    @POST("2/statuses/share.json")
    Call<ShareBean>share(@Query("access_token")String access_token,
                         @Query("status")String status,
                         @Part MultipartBody.Part file);


    //根据ID获取单条微博信息
    @GET("2/statuses/show.json")
    Call<ShowBean>show(@Query("access_token")String access_token,
                       @Query("id")long id);

    //@我的微博
    @GET("2/statuses/mentions.json")
    Call<MentionswbBean> mentions(@Query("access_token")String token,
                                  @Query("page")int page);

}
