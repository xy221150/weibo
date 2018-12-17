package org.wxy.weibo.cosmos.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.ui.fragment.ByMeFragment;
import org.wxy.weibo.cosmos.ui.fragment.ToMeFragment;
import org.wxy.weibo.cosmos.ui.adapter.ViewPager1Adapter;

import java.util.ArrayList;
import java.util.List;

public class ByToMeActivity extends ActionbarActvity {
    private SlidingTabLayout tab;
    private ViewPager viewpager;
    private List<Fragment> list;
    private String[] strings=new String[]{"发出的评论","收到的评论"};
    private ViewPager1Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("评论");
    }

    @Override
    protected int getContLayoutID() {
        return R.layout.activity_bytome;
    }

    @Override
    protected void initView() {
        super.initView();
        tab=findViewById(R.id.tab);
        viewpager=findViewById(R.id.viewpager);
    }

    @Override
    protected void init() {
        super.init();
        list=new ArrayList<>();
        list.add(new ByMeFragment());
        list.add(new ToMeFragment());
    }

    @Override
    protected void initdata() {
        super.initdata();
        adapter=new ViewPager1Adapter(getSupportFragmentManager(),list);
        viewpager.setAdapter(adapter);
        tab.setViewPager(viewpager,strings);
    }
}
