package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.Bean.Home_timelinebean;
import org.wxy.weibo.cosmos.Bean.ShareBean;
import org.wxy.weibo.cosmos.Bean.ShowBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by wxy on 2018/7/4.
 */

public interface IStatuses {
    @GET("2/statuses/home_timeline.json")
    Call<Home_timelinebean> gethome_timeline(@Query("access_token") String access_token,
                                             @Query("count") int count,
                                             @Query("page") int page,
                                             @Query("feature") int feature);


    @GET("2/statuses/user_timeline.json")
    Call<Home_timelinebean> getuser_timeline(@Query("access_token") String access_token,
                                             @Query("uid")String uid,
                                             @Query("screen_name") String screen_name,
                                             @Query("count") int count,
                                             @Query("page") int page,
                                             @Query("feature") int feature);

    @Multipart
    @Headers({"Content-Type:application/x-www-form-urlencoded"})
    @POST("2/statuses/share.json")
    Call<ShareBean>share(@Query("access_token")String access_token,
                         @Query("status")String status,
                         @Part MultipartBody.Part file);


    @GET("2/statuses/show.json")
    Call<ShowBean>show(@Query("access_token")String access_token,
                       @Query("id")long id);

}
