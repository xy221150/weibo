package org.wxy.weibo.cosmos.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.wxy.weibo.cosmos.R;

/**
 * Created by Administrator on 2018/4/12.
 */

public abstract class WidgetActivity extends BaseActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        initView();
        init();
        initdata();
    }
    private void initLayout(){
        setContentView(getLayoutID());
    }
    protected abstract int getLayoutID();
    protected void init(){}
    protected void initdata(){}
    protected void initView(){}


}
