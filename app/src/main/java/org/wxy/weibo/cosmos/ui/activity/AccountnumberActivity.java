package org.wxy.weibo.cosmos.ui.activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.wxy.weibo.cosmos.Bean.AccountnumberBean;
import org.wxy.weibo.cosmos.Bean.Userbean;
import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.SQLite.SqlLite;
import org.wxy.weibo.cosmos.network.RetrofitHelper;
import org.wxy.weibo.cosmos.network.api.IUser;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.activity.adapter.AccountnumberAdapter;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountnumberActivity extends ActionbarActvity {
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private SQLiteDatabase db;
    private SqlLite sqlLite;
    private TextView add;
    private RecyclerView list;
    private List<AccountnumberBean> activities;
    private AccountnumberBean bean;
    private AccountnumberAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("账号管理");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_accountnumber;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void initView() {
        super.initView();
        add=findViewById(R.id.add);
        list=findViewById(R.id.list);
    }

    @Override
    protected void init() {
        super.init();
        sqlLite=new SqlLite(this);
        mSsoHandler=new SsoHandler(this);
        db=sqlLite.getWritableDatabase();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mSsoHandler.authorizeWeb(new SelfWbAuthListener());
            }
        });
        Query();


    }

    @Override
    protected void initdata() {
        super.initdata();
        initRecycler(list,new LinearLayoutManager(this));
        list.setAdapter(adapter);
        if (User.user().getToken().equals("")&&activities.size()>0||User.user().getToken()==""&&activities.size()>0)
        {
            User.user().setToken(activities.get(0).getToken());
            User.user().setUid(activities.get(0).getUid());
            User.user().setName(activities.get(0).getName());
        }


        adapter.OnClickListener(new AccountnumberAdapter.OnClickListener() {
            @Override
            public void ClickListener(int i) {
               ChangeAccount(activities.get(i).getName(),
                       activities.get(i).getToken(),
                       activities.get(i).getUid());
            }
        });
    }

    //获取账号
    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            AccountnumberActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(AccountnumberActivity.this, mAccessToken);
                        User.user().setToken(mAccessToken.getToken());
                        User.user().setUid(mAccessToken.getUid());
                        IUser iUser=RetrofitHelper.create(IUser.class);
                        Map<String,String> map=new HashMap<>();
                        map.put("access_token",mAccessToken.getToken());
                        map.put("uid",mAccessToken.getUid());
                        iUser.getuser1(map)
                                .enqueue(new Callback<Userbean>() {
                                    @Override
                                    public void onResponse(Call<Userbean> call, Response<Userbean> response) {
                                       if (response.code()==200)
                                       {
                                           Add(response.body().getName(),
                                                   mAccessToken.getToken(),
                                                   mAccessToken.getUid(),
                                                   response.body().getAvatar_large());
                                       }
                                    }

                                    @Override
                                    public void onFailure(Call<Userbean> call, Throwable t) {

                                    }
                                });
                        finish();
                        starActivity(AccountnumberActivity.class);
                    }
                }
            });
        }
        @Override
        public void cancel() {
            showToast("授权失败");
        }

        @Override
        public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
            showToast(""+wbConnectErrorMessage.getErrorMessage());
        }
    }

    //添加
    public void Add(String name,String token,String uid,String url){
        Cursor cursor = db.rawQuery("select * from info where name = ?",new String[]{name});
        if (cursor.getCount()>0)
         showToast("账号已存在请勿重复添加");
        else
            db.execSQL("insert into info(name,token,uid,url) values(?,?,?,?)",
                    new Object[] {name,token,uid,url});

        Log.d("TAG", "Add: "+cursor.getCount());

    }

    //查询
    public void Query() {
        activities=new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from info", null);
        cursor.moveToFirst();
        //循环读取数据
        while(!cursor.isAfterLast()){
            //获得当前行的标签
            int nameIndex = cursor.getColumnIndex("name");
            int tokenIndex = cursor.getColumnIndex("token");
            int uidIndex=cursor.getColumnIndex("uid");
            int urlIndex=cursor.getColumnIndex("url");
            //获得对应的数据
            String name = cursor.getString(nameIndex);
            String token = cursor.getString(tokenIndex);
            String uid = cursor.getString(uidIndex);
            String url = cursor.getString(urlIndex);
            bean=new AccountnumberBean(name,token,uid,url);
            Log.d("TAG", "Query: "+name+token+uid+url);
            activities.add(bean);
            //游标移到下一行
            cursor.moveToNext();
        }
            adapter=new AccountnumberAdapter(activities);
    }

    //提示框
    private void ChangeAccount(final String name, final String token, final String uid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("您确定把账号更换到"+name)
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        User.user().setToken(token);
                        User.user().setUid(uid);
                        User.user().setName(name);
                        MainActivity.getStack().finishAllActivity();
                        starActivity(HomeActivity.class);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .setCanceledOnTouchOutside(false);
        builder.show();

    }
}
