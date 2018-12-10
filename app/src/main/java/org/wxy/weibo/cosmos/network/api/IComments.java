package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.Bean.ByMeBean;
import org.wxy.weibo.cosmos.Bean.CommentsShowBean;
import org.wxy.weibo.cosmos.Bean.CreateBean;
import org.wxy.weibo.cosmos.Bean.ToMeBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IComments {
    //评论
    @POST("2/comments/create.json")
    Call<CreateBean> create(@Query("access_token")String access_token,
                            @Query("comment")String comment,
                            @Query("id")long id);

    //获取评论
    @GET("2/comments/show.json")
    Call<CommentsShowBean> show(@Query("access_token")String access_token,
                                @Query("id")long id,
                                @Query("page") int page);


    //发出评论
    @GET("2/comments/by_me.json")
    Call<ByMeBean> byme(@Query("access_token")String token,
                        @Query("page")int page);

    //发出评论
    @GET("2/comments/to_me.json")
    Call<ToMeBean> tome(@Query("access_token")String token,
                        @Query("page")int page);

    //@我的评论
    @GET("2/comments/mentions.json")
    Call<String> mentions(@Query("access_token")String token,
                          @Query("page")int page);
}
