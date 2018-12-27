package org.wxy.weibo.cosmos.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wxy.weibo.cosmos.Activity;
import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.bean.ThemeBean;
import org.wxy.weibo.cosmos.ui.adapter.ThemeAdapter;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;
import org.wxy.weibo.cosmos.utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("NewApi")
public class ThemeActivity extends ActionbarActvity {
    private List<ThemeBean> themes;
    private RecyclerView list;
    private ThemeAdapter adapter;
    private int ThemeNumbre;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("切换主题");
        ThemeNumbre=ThemeUtil.getTheme(this);
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initView() {
        super.initView();
        list=findViewById(R.id.list);
    }

    @Override
    protected void init() {
        super.init();
        themes=new ArrayList<>();
        ThemeBean bean=new ThemeBean("默认", Color.parseColor("#1a237e"));
        themes.add(bean);
        ThemeBean bean1=new ThemeBean("哔哩粉",Color.parseColor("#ec5387"));
        themes.add(bean1);
        ThemeBean bean2=new ThemeBean("网易红",Color.parseColor("#D33B30"));
        themes.add(bean2);
        ThemeBean bean3=new ThemeBean("基佬紫",Color.parseColor("#8e24aa"));
        themes.add(bean3);
        ThemeBean bean4=new ThemeBean("碧海蓝",Color.parseColor("#59b7c3"));
        themes.add(bean4);
        ThemeBean bean5=new ThemeBean("樱草绿",Color.parseColor("#89c348"));
        themes.add(bean5);
        ThemeBean bean6=new ThemeBean("古铜棕",Color.parseColor("#75655a"));
        themes.add(bean6);
        ThemeBean bean7=new ThemeBean("柠檬橙",Color.parseColor("#D88100"));
        themes.add(bean7);
        ThemeBean bean8=new ThemeBean("星空灰",Color.parseColor("#9e9e9e"));
        themes.add(bean8);
        ThemeBean bean9=new ThemeBean("夜间模式",Color.parseColor("#3b3b3b"));
        themes.add(bean9);
        initRecycler(list,new GridLayoutManager(this,4));
        adapter=new ThemeAdapter(themes);
        list.setAdapter(adapter);
        adapter.OnClickListener(new ThemeAdapter.OnClickListener() {

            @Override
            public void OnClickListener(int i) {
                ThemeUtil.setTheme(Activity.mainActivity(),i);
                setColor(themes.get(i).getColor());
                ThemeActivity.this.getWindow().setStatusBarColor(themes.get(i).getColor());
            }
        });
    }

    @Override
    protected void initdata() {
        super.initdata();
        setNavigationOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (ThemeNumbre==ThemeUtil.getTheme(ThemeActivity.this))
                        finish();
                    else
                    {
                        finish();
                        starActivity(MainActivity.class);
                    }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ThemeNumbre==ThemeUtil.getTheme(ThemeActivity.this))
            finish();
        else
        {
            finish();
            starActivity(MainActivity.class);
        }

    }
}
