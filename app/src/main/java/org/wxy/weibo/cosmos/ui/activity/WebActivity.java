package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.view.ProgressWebView;

public class WebActivity extends ActionbarActvity {
    private ProgressWebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("网页详情");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        super.initView();
        web=findViewById(R.id.web);
    }

    @Override
    protected void init() {
        super.init();
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        web.loadUrl(url);
    }

    public void getUrl(Context context,String url){
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }
}
