package org.wxy.weibo.cosmos.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.wxy.weibo.cosmos.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/25.
 */

public class RetrofitHelper {
    private static RetrofitHelper retrofitHelper;
    private Retrofit mRetrofit;
    private RetrofitHelper(){

    }
    public static RetrofitHelper getRetrofitHelper(){
        if(retrofitHelper==null){
            retrofitHelper=new RetrofitHelper();
        }
        return  retrofitHelper;
    }
    public Retrofit getmRetrofit(){
        if (mRetrofit==null){
            synchronized (RetrofitHelper.class){
                if (mRetrofit==null){
                    //1.OkHttpClient
                    OkHttpClient.Builder OkHttpClientBuilder=new OkHttpClient.Builder();
                    OkHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
                    OkHttpClientBuilder.readTimeout(10,TimeUnit.SECONDS);
//                    OkHttpClient okHttpClient=OkHttpClientBuilder.build();
                    //2.生成GsonConverterFactory对象
                    GsonBuilder gsonBuilder=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                    Gson gson=gsonBuilder.create();
                    GsonConverterFactory gsonConverterFactory=GsonConverterFactory.create(gson);
                    //3.生成Retrofit对象
                    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.SERIVER).addConverterFactory(gsonConverterFactory);

                   retroitMachining(OkHttpClientBuilder);
                    mRetrofit= builder.client(OkHttpClientBuilder.build()).build();
                }
            }
        }
        return mRetrofit;
    }

    protected void retroitMachining(OkHttpClient.Builder clientBuilder)
    {
        clientBuilder.addInterceptor(new LogInterceptor());
    }
    public  static <T> T create(Class<T> cls){
        return getRetrofitHelper().getmRetrofit().create(cls);
    }

}
