package org.wxy.weibo.cosmos.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.ActionbarActvity;
import org.wxy.weibo.cosmos.ui.fragment.ByMeFragment;
import org.wxy.weibo.cosmos.ui.fragment.MentionsFragment;
import org.wxy.weibo.cosmos.ui.fragment.MentionswbFragment;
import org.wxy.weibo.cosmos.ui.fragment.ToMeFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.ViewPager1Adapter;

import java.util.ArrayList;
import java.util.List;

public class MentionsAcvtivity extends ActionbarActvity {
    private SlidingTabLayout tab;
    private ViewPager viewpager;
    private List<Fragment> list;
    private ViewPager1Adapter adapter;
    private String[] strings=new String[]{"@我的评论","@我的微博"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContTitle("@我");
    }
    @Override
    protected int getContLayoutID() {
        return R.layout.activity_mentions;
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
        list.add(new MentionsFragment());
        list.add(new MentionswbFragment());
    }

    @Override
    protected void initdata() {
        super.initdata();
        adapter=new ViewPager1Adapter(getSupportFragmentManager(),list);
        viewpager.setAdapter(adapter);
        tab.setViewPager(viewpager,strings);
    }
}
