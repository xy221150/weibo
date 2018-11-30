package org.wxy.weibo.cosmos.network.api;

import org.wxy.weibo.cosmos.Bean.Friendshipsbean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IFriendships {
    @GET("2/friendships/friends.json")
    Call<Friendshipsbean> friend(@Query("access_token")String access_token,
                                 @Query("uid")String uid,
                                 @Query("cursor")int cursor);
    @GET("2/friendships/followers.json")
    Call<Friendshipsbean> followers(@Query("access_token")String access_token,
                                    @Query("uid")String uid);
}
