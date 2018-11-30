package org.wxy.weibo.cosmos.ui.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.wxy.weibo.cosmos.MainActivity;
import org.wxy.weibo.cosmos.R;

import java.util.List;

/**
 * Created by wxy on 2018/7/4.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager fragmentManager;
    private List<Fragment> fragments;
    private Fragment fragment;
    private int icon[];
    private List<String> Text;
    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments,int icon[],List<String> TabText) {
        super(fm);
        this.fragments=fragments;
        this.icon=icon;
        this.Text=TabText;
    }
    public View getTabView(int i){
           View view=View.inflate(MainActivity.mainActivity(),R.layout.item_tabview,null);
           TextView TabText=view.findViewById(R.id.itme_tabview_text);
           TabText.setText(Text.get(i));
           ImageView TabImg=view.findViewById(R.id.itme_tabview_img);
           TabImg.setImageResource(icon[i]);
           if (i==0)
           {
               TabText.setTextColor(view.getResources().getColor(R.color.blue1));
           }
           return view;
    }
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
