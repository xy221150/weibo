package org.wxy.weibo.cosmos.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import org.wxy.weibo.cosmos.R;
import org.wxy.weibo.cosmos.ui.activity.ByToMeActivity;
import org.wxy.weibo.cosmos.ui.activity.MentionsAcvtivity;
import org.wxy.weibo.cosmos.ui.base.BaseFragment;

/**
 * Created by wxy on 2018/7/4.
 */

public class NoticeFragment extends BaseFragment {
    private LinearLayout bm_comment_linear;
    private LinearLayout at_weibo_Linear;
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        bm_comment_linear=view.findViewById(R.id.bm_comment_linear);
        at_weibo_Linear=view.findViewById(R.id.at_weibo_Linear);
    }

    @Override
    protected void init() {
        super.init();
        at_weibo_Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starActivity(MentionsAcvtivity.class);
            }
        });
        bm_comment_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starActivity(ByToMeActivity.class);
            }
        });
    }
}
