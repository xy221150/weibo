package org.wxy.weibo.cosmos.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.WidgetActivity;
import org.wxy.weibo.cosmos.ui.fragment.IndexFragment;
import org.wxy.weibo.cosmos.ui.fragment.MyFragment;
import org.wxy.weibo.cosmos.ui.fragment.NoticeFragment;
import org.wxy.weibo.cosmos.ui.fragment.adapter.ViewPagerAdapter;
import org.wxy.weibo.cosmos.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wxy on 2018/7/4.
 */

public class HomeActivity extends WidgetActivity{
    private TabLayout mHomeTabLayout;
    private CustomViewPager mHomeViewPager;
    private List<Fragment> fragments;
    private ViewPagerAdapter adapter;
    private List<String> TabText;
    private int iconimg[]={R.drawable.ic_weibo_grey,R.drawable.ic_notification_gray_24,R.drawable.ic_contacts};
    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }
    @Override
    protected void initView() {
        super.initView();
        mHomeTabLayout=findViewById(R.id.Home_Tab);
        mHomeViewPager=findViewById(R.id.Home_ViewPager);
        fragments=new ArrayList<>();
        TabText=new ArrayList<String>();
        fragments.add(new IndexFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new MyFragment());
        TabText.add("微博");
        TabText.add("通知");
        TabText.add("我的");
    }
    @Override
    protected void init() {
        super.init();
        adapter=new ViewPagerAdapter(getSupportFragmentManager(),fragments,iconimg,TabText);
        mHomeViewPager.setAdapter(adapter);
        mHomeViewPager.setCurrentItem(0);
        mHomeViewPager.setScanScroll(false);
        mHomeTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mHomeTabLayout.setupWithViewPager(mHomeViewPager);
        resetTablayout();
    }
    private void resetTablayout(){
       for (int i=0;i<TabText.size();i++)
       {
           mHomeTabLayout.getTabAt(i).setText(TabText.get(i));
       }
       for (int i=0;i<mHomeTabLayout.getTabCount();i++)
       {
           TabLayout.Tab tab=mHomeTabLayout.getTabAt(i);
           if (tab!=null)
           {
                 tab.setCustomView(adapter.getTabView(i));
           }
       }
        mHomeTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.itme_tabview_text);
                textView.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.itme_tabview_text);
                textView.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
