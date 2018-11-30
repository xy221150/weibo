package org.wxy.weibo.cosmos.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.wxy.weibo.cosmos.R;


/**
 * Created by Administrator on 2018/4/12.
 */

public abstract class ActionbarActvity extends WidgetActivity {
    private FrameLayout mContFrameView;
    private TextView mContTilte;
    private TextView Right_Title;
    private Toolbar mContToolbar;
    private View mContView;
    @Override
    protected int getLayoutID() {
        return R.layout.toolbar;
    }
    protected abstract int getContLayoutID();
    @Override
    protected void initView() {
        super.initView();
       mContFrameView=findViewById(R.id.FrameLayout);
       mContTilte=findViewById(R.id.Tool_Title);
       mContToolbar=findViewById(R.id.Tool_bar);
       Right_Title=findViewById(R.id.Right_Title);
       setSupportActionBar(mContToolbar);
       mContView= getLayoutInflater().inflate(getContLayoutID(),mContFrameView,false);
       if (mContView==null)
       {
           mContView=mContFrameView;
       }
       else
       {
           mContFrameView.addView(mContView);
       }
       mContToolbar.setTitle("");
       mContToolbar.setSubtitle("");
       setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        mContToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected  void setContTitle(String msg)
    {
        mContTilte.setText(msg);
    }

    protected  void setRight_Title(String msg)
    {
        Right_Title.setText(msg);
    }
    protected  void setRight_TitleColor(String color)
    {
        Right_Title.setTextColor(Color.parseColor(color));
    }
    protected  void Right_TitleIntent(final Class<?extends Activity> cls)
    {
        Right_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),cls);
                startActivity(intent);
            }
        });
    }

    private void OnContToolbar(){
        finish();
    }


}
