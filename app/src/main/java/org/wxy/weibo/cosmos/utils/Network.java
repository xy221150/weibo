package org.wxy.weibo.cosmos.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Network{
        //上下文
        private Context mContext;
        //请求的url
        private String       mUrl;
        //通过单例类得到的OkHttpClient 对象
        private OkHttpClient mClient;
        //解析服务器返回数据的Gson
        private Gson mGson;
        private Handler mHandler;
        //登录之后返回的Token字符串
        private String       mToken;

        public Network(Context context, String url) {
            this.mContext = context;
            this.mUrl = url;
            mClient = OkHttpClientInstance.getInstance();
            mGson = new Gson();
            mHandler = new Handler();
        }

        /**
         * 执行普通的post请求，参数集合全部转为json
         *
         * @param map  传入的参数集合
         * @param netCallBack  回调接口
         */
        public void performPost(Map<String, String> map, final NetCallBack netCallBack) {
            String params = mGson.toJson(map);
            MediaType JSON = MediaType.parse("application/json");
            RequestBody body = setRequestBody(map);
            final Request request = new Request.Builder()
                    .url(mUrl)
                    .header("Content-Type","multipart/form-data")
                    .header("Accepe","application/json")
                    .addHeader("Authorization", "Bearer" + mToken)
                    .post(body)
                    .build();
            mClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    doFailure(netCallBack);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    doResponse(response, netCallBack);
                }
            });
        }
        private void doFailure(final NetCallBack netCallBack) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                netCallBack.onNetFailure();
            }
        });
    }
    public void performGet(final NetCallBack netCallBack) {
        final Request request = new Request.Builder()
                .url(mUrl)
                .header("Content-Type","multipart/form-data")
                .header("Accepe","application/json")
                .addHeader("Authorization", "Bearer" + mToken)
                .get()
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                doFailure(netCallBack);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                doResponse(response, netCallBack);
            }
        });
    }
    /**
     * 处理成功
     *
     * @param response
     * @param netCallBack
     * @throws IOException
     */
    private void doResponse(Response response, final NetCallBack netCallBack) throws IOException {
        final String result = response.body().string();
        if (!TextUtils.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String success = jsonObject.getString("success");

                if ("true".equals(success)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onSuccess(result);
                        }
                    });
                } else if ("false".equals(success)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            netCallBack.onError(result);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //服务器返回错误
                        netCallBack.onError(result);
                    }
                });
            }
        }
    }
    private RequestBody setRequestBody(Map<String, String> BodyParams){
        RequestBody body=null;
        okhttp3.FormBody.Builder formEncodingBuilder=new okhttp3.FormBody.Builder();
        if(BodyParams != null){
            Iterator<String> iterator = BodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next().toString();
                formEncodingBuilder.add(key, BodyParams.get(key));
                Log.d("post http", "post_Params==="+key+"===="+BodyParams.get(key));
            }
        }
        body=formEncodingBuilder.build();
        return body;

    }
}
