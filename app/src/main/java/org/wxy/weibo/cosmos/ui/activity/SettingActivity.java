package org.wxy.weibo.cosmos.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.sharepreferences.User;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;

public class SettingActivity extends ActionbarActvity {
    private TextView end;

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
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.user().setToken("");
                MainActivity.getStack().finishAllActivity();
            }
        });
    }
}
