package org.wxy.weibo.cosmos.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.view.X5WebView;

import java.util.HashMap;

public class WebActivity extends ActionbarActvity {
    private X5WebView web;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
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
        web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (title!=null)
                    setContTitle(title);
            }
        });
    }

    public void getUrl(Context context,String url){
        Intent intent=new Intent(context,WebActivity.class);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (web!=null)
        {
            web.destroy();
            web=null;
        }
    }
}
