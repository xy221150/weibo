package org.wxy.weibo.cosmos.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.wxy.weibo.cosmos.Constants;
import org.wxy.weibo.cosmos.network.json.JsonItem;
import org.wxy.weibo.cosmos.network.json.JsonItemArray;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
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

    public  static RequestBody getBody(String content){
        return RequestBody.create(MediaType.parse("application/json"),content);
    }
    public  static RequestBody getBody(JsonItem...jsonItems){
        JSONObject jo = new JSONObject();
        if (null != jsonItems && jsonItems.length>0){
            for (JsonItem jsonItem : jsonItems){
                try {
                    if (jsonItem == null){
                        continue;
                    }
                    jo.put(jsonItem.getKey(),jsonItem.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return  RequestBody.create(MediaType.parse("application/json"),jo.toString());
    }
    public static RequestBody getBody(JsonItemArray itemArray, JsonItem... jsonItemArray) {
        JSONObject jsonObject = new JSONObject();

        if (itemArray == null) {
            return getBody(jsonItemArray);
        }
        String key = itemArray.getKey();
        String[] strArray = itemArray.getStrList();
        JsonItem[][] jsonItemArrays = itemArray.getJsonItems();
        JSONArray jsonArray = new JSONArray();


        if (strArray != null && strArray.length > 0) {
            for (String item : strArray) {
                if (item == null) {
                    continue;
                }
                jsonArray.put(item);
            }
            try {
                jsonObject.put(key, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (jsonItemArrays != null && jsonItemArrays.length > 0) {
            try {
                for (JsonItem[] jsonItems : jsonItemArrays) {
                    JSONObject itemObject = new JSONObject();
                    for (JsonItem item : jsonItems) {
                        if (item == null) {
                            continue;
                        }
                        itemObject.put(item.getKey(), item.getValue());
                    }
                    jsonArray.put(itemObject);

                }
                jsonObject.put(key, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonItemArray != null && jsonItemArray.length > 0) {
            try {
                for (JsonItem item : jsonItemArray) {
                    if (item == null) {
                        continue;
                    }
                    jsonObject.put(item.getKey(), item.getValue());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
    }

   public static MultipartBody.Part getbody(File file){
       MultipartBody.Part part=null;
        if (file!=null) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/from-data"), file);
            part = MultipartBody.Part.createFormData("flie", file.getName(), body);
        }
        else
            part=MultipartBody.Part.createFormData("flie","");
       return part;
   }
    public static MultipartBody.Part getbody(String pic){
        MultipartBody.Part part=null;
        if (pic!=null) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/from-data"), pic);
            part = MultipartBody.Part.createFormData("pic", pic, body);
        }
        else
            part=MultipartBody.Part.createFormData("pic","");
        return part;
    }
}
