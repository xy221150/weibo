package org.wxy.weibo.cosmos.ui.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.SQLite.SQLite;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;

public class SettingActivity extends ActionbarActvity {
    private TextView end;
    private SQLiteDatabase db;
    private SQLite SQLite;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("设置");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        end=findViewById(R.id.end);
    }

    @Override
    protected void init() {
        super.init();
        SQLite =new SQLite(this);
        db= SQLite.getWritableDatabase();
    }

    @Override
    protected void initdata() {
        super.initdata();
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }
    private void delete(){
       if (User.user().getToken()!=null||!User.user().getToken().equals(""))
       {
           db.delete("info","token=?",new String[]{User.user().getToken()});
           db.close();
           User.user().delete();
           showToast("登出成功");
           Activity.getStack().finishAllActivity();
           starActivity(MainActivity.class);
       }
       else
           showToast("暂无授权无法登出");
    }
}
