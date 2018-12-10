package org.wxy.weibo.cosmos.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

public class MentionsFragment extends BaseFragment {
    private PtrClassicFrameLayout ptr;
    private RecyclerView list;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mentions;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ptr=view.findViewById(R.id.ptr);
        list=view.findViewById(R.id.list);
    }
}
