package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.Bean.CommentsShowBean;
import org.wxy.weibo.cosmos.Bean.CreateBean;

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
}
